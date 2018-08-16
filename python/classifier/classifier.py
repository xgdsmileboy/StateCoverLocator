from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import os
from Utils.config import *
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn import metrics
from classifier import classifier_model

import tensorflow as tf
import shutil as su
import Queue
import datetime
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from sklearn.preprocessing import OneHotEncoder
from clustering.cluster import *

class Classifier(object):

    def __init__(self, configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure

    @staticmethod
    def print_all_accuracy_precision_recall_f1(y_true, y_pred, label):
    	print("\n{0} Accuracy: {1:f}\n".format(label, metrics.accuracy_score(y_true, y_pred)))
        Classifier.print_precision_recall_f1(y_true, y_pred, 'micro', label)
        Classifier.print_precision_recall_f1(y_true, y_pred, 'macro', label)
        Classifier.print_precision_recall_f1(y_true, y_pred, 'weighted', label)

    @staticmethod
    def print_precision_recall_f1(y_true, y_pred, average_type, label):
        metric = metrics.precision_recall_fscore_support(y_true, y_pred, average=average_type)
        print("{0} Precision {2}: {1:f}\n".format(label, metric[0], average_type))
        print("{0} Recall {2}: {1:f}\n".format(label, metric[1], average_type))
        print("{0} F1score {2}: {1:f}\n".format(label, metric[2], average_type))

    def train_classifier(self, str_encoder, feature_num, evaluate):
        start_time = datetime.datetime.now()

        data_file_path = self.__configure__.get_raw_classifier_train_in_file()

        # load data from a csv file
        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
        dataset = data.values
        print('Dataset size: {}'.format(dataset.shape))
        # split data into X and y
        # 3 to 11: 8
        X = dataset[:, 2:2 + feature_num]
        X = X.astype(str)
        Y = dataset[:, 2 + feature_num]
        Y = Y.astype(str)
        classes = np.unique(Y)
        class_num = len(classes)
        print('Class number: %d' % class_num)

        # encoding string as integers
        encoded_X = None
        x_encoders = [None] * feature_num
        for i in range(0, X.shape[1]):
            feature = np.zeros((X.shape[0], 1))
            if i <= 3:
                x_encoders[i] = LabelEncoder()
                feature = x_encoders[i].fit_transform(X[:, i])
                feature = feature.reshape(X.shape[0], 1)
                if i == 0:
                    # file name
                    for j in range(0, X.shape[0]):
                        feature[j] = str_encoder['file'][str(X[j, i])]
                elif i == 1:
                    # function name
                    for j in range(0, X.shape[0]):
                        feature[j] = str_encoder['func'][str(X[j, i])]
                elif i == 2:
                    # variable name
                    for j in range(0, X.shape[0]):
                        feature[j] = str_encoder['var'][str(X[j, i]).lower()]
                # elif i == 5:
                # dist0
                # for j in range(0, X.shape[0]):
                # feature[j] = int(X[j, i])
                one_hot_encoder = OneHotEncoder(sparse=False)
                feature = one_hot_encoder.fit_transform(feature)
            elif i == 7: # predicate
                for j in range(0, X.shape[0]):
                    feature[j] = str_encoder['pred'][str(X[j, i]).lower()]
                one_hot_encoder = OneHotEncoder(sparse=False)
                feature = one_hot_encoder.fit_transform(feature)
            else:
                for j in range(0, X.shape[0]):
                    feature[j] = int(X[j, i])
            if encoded_X is None:
                encoded_X = feature
            else:
                encoded_X = np.concatenate((encoded_X, feature), axis=1)
        # encoded targets = original targets
        y_encoder = LabelEncoder()
        encoded_Y = y_encoder.fit_transform(Y)

        model_file = self.__configure__.get_classifier_model_dir()

        # init the model
        if os.path.exists(model_file):
            print('Model file already exists and be removed.')
            su.rmtree(model_file)

        print('Training the model...')

        if evaluate:
            self.evaluate(encoded_X, encoded_Y, encoded_X.shape[1], y_encoder.classes_.shape[0], True)
        else:
            print(encoded_X.shape[1])
            classifier = self.train(encoded_X, encoded_Y, encoded_X.shape[1], y_encoder.classes_.shape[0], model_file)

        end_time = datetime.datetime.now()
        run_time = end_time - start_time
        print('Training fininshed, time cost : {}'.format(run_time))

    def train(self, X, Y, feature_num, class_num, model_dir_):
        X_train, X_valid, y_train, y_valid = train_test_split(X, Y, test_size = 0.25, random_state = 7)

        default_model = '/tmp/tmp_classifier'
        if os.path.exists(default_model):
            su.rmtree(default_model)

        classifier = classifier_model.get_dnn_classifier(feature_num, class_num, default_model)

        train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=y_train, num_epochs=1, shuffle=True)
        # test_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_valid}, y=y_valid, num_epochs=1, shuffle=True)
        same_train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=y_train, num_epochs=1, shuffle=False)
        valid_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_valid}, y=y_valid, num_epochs=1, shuffle=False)

        INIT_EPOCHS = 20
        TEST_EPOCHS = 20
        # Train 20 epochs first
        init_train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=y_train, num_epochs=INIT_EPOCHS, shuffle=True)
        classifier.train(input_fn=init_train_input_fn)

        i = INIT_EPOCHS + 1
        min_loss = -1;
        min_loss_model = ""
        epoch_num = 21
        ll_losses = Queue.Queue(maxsize = TEST_EPOCHS)
        l_losses = Queue.Queue(maxsize = TEST_EPOCHS)
        ll_losses_t = Queue.Queue(maxsize = TEST_EPOCHS)
        l_losses_t = Queue.Queue(maxsize = TEST_EPOCHS)
        ll_total = 0
        l_total = 0
        ll_total_t = 0
        l_total_t = 0

        while True:
            classifier.train(input_fn=train_input_fn)
            train_evaluate_result = classifier.evaluate(input_fn=same_train_input_fn)
            loss_train = train_evaluate_result["loss"]
            accuracy_train = train_evaluate_result["accuracy"]
            valid_evaluate_result = classifier.evaluate(input_fn=valid_input_fn)
            loss_valid = valid_evaluate_result["loss"]
            accuracy_valid = valid_evaluate_result["accuracy"]
            if loss_valid < min_loss or min_loss < 0:
                min_loss = loss_valid
                if os.path.exists(model_dir_):
                    su.rmtree(model_dir_)
                su.copytree(default_model, model_dir_)
                epoch_num = i
            print("Epoch {0} Train loss: {1:f}, accuracy: {2:f}. Valid loss: {3:f}, accuracy: {4:f}".format(i, loss_train, accuracy_train, loss_valid, accuracy_valid))
            if i <= INIT_EPOCHS + TEST_EPOCHS:
                ll_total = ll_total + loss_valid
                ll_losses.put(loss_valid)
                ll_total_t = ll_total_t + loss_train
                ll_losses_t.put(loss_train)
            elif i <= INIT_EPOCHS + 2 * TEST_EPOCHS:
                l_total = l_total + loss_valid
                l_losses.put(loss_valid)
                l_total_t = l_total_t + loss_train
                l_losses_t.put(loss_train)
            else:
                l_rm = l_losses.get()
                l_losses.put(loss_valid)
                l_total = l_total - l_rm + loss_valid
                ll_rm = ll_losses.get()
                ll_losses.put(l_rm)
                ll_total = ll_total - ll_rm + l_rm

                l_rm = l_losses_t.get()
                l_losses_t.put(loss_train)
                l_total_t = l_total_t - l_rm + loss_train
                ll_rm = ll_losses_t.get()
                ll_losses_t.put(l_rm)
                ll_total_t = ll_total_t - ll_rm + l_rm
                if l_total > ll_total and l_total_t < ll_total_t:
                    print("Stop for valid last: {0:f}, last last: {1:f}, train last: {2:f}, last last: {3:f}. Use model with loss {4:f}, epoch {5}\n"
                        .format(l_total, ll_total, l_total_t, ll_total_t, min_loss, epoch_num))
                    break
            i = i + 1

        classifier = classifier_model.get_dnn_classifier(feature_num, class_num, model_dir_)

        print(classifier.evaluate(input_fn=valid_input_fn))

        return classifier


    def evaluate(self, X, Y, feature_num, class_num):
        X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=0.2, random_state = 7)
        default_eval_model = '/tmp/tmp_classifier_val'

        classifier = self.train(X_train, y_train, feature_num, class_num, default_eval_model)

        test_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_test}, y=None, num_epochs=1, shuffle=False)

        test_predictions = list(classifier.predict(input_fn = test_input_fn))
        y_classes = [int(p['classes'][0]) for p in test_predictions]

        self.print_all_accuracy_precision_recall_f1(y_test, y_classes, 'Test')

        train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=None, num_epochs=1, shuffle=False)
        train_predictions = list(classifier.predict(input_fn = train_input_fn))
        y_classes = [int(p['classes'][0]) for p in train_predictions]

        self.print_all_accuracy_precision_recall_f1(y_train, y_classes, 'Train')

        if os.path.exists(default_eval_model):
            su.rmtree(default_eval_model)


    def classify(self, str_encoder, kmeans_model, unique_words):

        print('Predicting var for {}...'.format(self.__configure__.get_bug_id()))

        encoded_var, feature_num = self.encode_str(str_encoder, kmeans_model, unique_words)
        # get the predicted varnames
        self.predict_vars(encoded_var, feature_num)

    def encode_str(self, str_encoder, kmeans_model, unique_words):
        data_file_path = self.__configure__.get_raw_classify_pred_in_file()

        original_data_file_path = self.__configure__.get_raw_classifier_train_in_file()
        # load data from a csv file
        original_data = pd.read_csv(original_data_file_path, sep='\t', header=0, encoding='utf-8')
        original_dataset = original_data.values
        print('Dataset size: {}'.format(original_dataset.shape))
        feature_num = original_data.shape[1] - 3
        # split data into X and y
        # 3 to 11: 8
        X = original_dataset[:, 2:-1]
        X = X.astype(str)

        # encoding string as integers
        x_encoders = [None] * feature_num
        one_hot_encoder = [None] * feature_num
        for i in range(0, X.shape[1]):
            if i <= 3:
                x_encoders[i] = LabelEncoder()
                feature = x_encoders[i].fit_transform(X[:, i])
                feature = feature.reshape(X.shape[0], 1)
                if i == 0:
                    # file name
                    for j in range(0, X.shape[0]):
                        feature[j] = str_encoder['file'][str(X[j, i])]
                elif i == 1:
                    # function name
                    for j in range(0, X.shape[0]):
                        feature[j] = str_encoder['func'][str(X[j, i])]
                elif i == 2:
                    # variable name
                    for j in range(0, X.shape[0]):
                        feature[j] = str_encoder['var'][str(X[j, i]).lower()]
                one_hot_encoder[i] = OneHotEncoder(sparse=False)
                one_hot_encoder[i].fit(feature)
            if i == 7:
                # predicates
                for j in range(0, X.shape[0]):
                    feature[j] = str_encoder['pred'][str(X[j, i]).lower()]
                one_hot_encoder[i] = OneHotEncoder(sparse=False)
                one_hot_encoder[i].fit(feature)

        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
        dataset = data.values

        encoded_feature = list()
        new_feature_num = feature_num
        start = 2
        for i in range(0, dataset.shape[0]):
            feature = list()
            for j in range(0, feature_num):
                # TODO : if the key does not exit in the encoder, how to transform
                try:
                    if j == 0:
                        tmp = str_encoder['file'][str(dataset[i, start + j])]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis=0)
                    elif j == 1:
                        tmp = str_encoder['func'][str(dataset[i, start + j])]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis=0)
                    elif j == 2:
                        tmp = str_encoder['var'][str(dataset[i, start + j]).lower()]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis=0)
                    elif j == 7:
                        tmp = str_encoder['pred'][str(dataset[i, start + j]).lower()]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis=0)
                    # elif j == 5:
                    # feature = np.append(feature, int(dataset[i, 3 + j]))
                    elif j == 3:
                        tmp = x_encoders[j].transform([str(dataset[i, start + j])])[0]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis=0)
                    else:
                        feature = np.append(feature, int(dataset[i, start + j]))
                except Exception as e:
                    if j == 7:
                        X_0 = np.mat(np.zeros((1, 42 * 42 + 1)))
                        X_0[0] = Cluster.predicate_to_vector(str(dataset[i, start + j]).lower())
                        pred = kmeans_model['pred'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis=0)
                    elif j == 2:
                        # feature.append(len(var_encoder))
                        X_0 = np.mat(np.zeros((1, 27 * 27 + 1)))
                        X_0[0] = Cluster.var_to_vec(str(dataset[i, start + j]).lower())
                        pred = kmeans_model['var'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis=0)
                    elif j == 0:
                        X_0 = np.mat(np.zeros((1, len(unique_words['file']))))
                        X_0[0] = Cluster.name_to_vec(str(dataset[i, start + j]), unique_words['file'])
                        pred = kmeans_model['file'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis=0)
                    elif j == 1:
                        X_0 = np.mat(np.zeros((1, len(unique_words['func']))))
                        X_0[0] = Cluster.name_to_vec(str(dataset[i, start + j]), unique_words['func'])
                        pred = kmeans_model['func'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis=0)
                    else:
                        feature = np.concatenate((feature, np.zeros(one_hot_encoder[j].n_values_)), axis=0)

            new_feature_num = len(feature)
            feature = np.append(feature, 0)
            encoded_feature.append(feature)

        return (pd.DataFrame(encoded_feature), new_feature_num)

    def predict_vars(self, encoded_var, feature_num):
        # encoded_oracle_var =  oracle[0:-4]+ '.var_encoded.csv'
        class_predicted = self.__configure__.get_joint_predict_file()
        if os.path.exists(class_predicted):
            os.remove(class_predicted)

        raw_var_path = self.__configure__.get_raw_classify_pred_in_file()
        raw_var = pd.read_csv(raw_var_path, sep='\t', header=0)
        raw_var_values = raw_var.values

        predicates = list()
        line_ids = list()
        for r in range(0, encoded_var.shape[0]):
            line_ids.append(raw_var_values[r, 2] + "::" + str(raw_var_values[r, 0]) + "::" + raw_var_values[r, 4])
            predicates.append(raw_var_values[r, 9])

        encoded_rows_array = np.array(encoded_var)
        # print(encoded_rows_array.shape)
        X_pred = encoded_rows_array[:, 0:-1]
        X_pred = X_pred.astype(float)

        y_pred = encoded_rows_array[:, -1]

        y_pred = y_pred.astype(float)

        print(feature_num)
        y_prob = self.predict(X_pred, feature_num, 2)
        print(y_prob)
        with open(class_predicted, 'w') as f:
            for i in range(0, X_pred.shape[0]):
                f.write('%s\t' % line_ids[i])
                f.write('%s\t' % predicates[i])
                f.write('%.4f' % y_prob[i][1])
                f.write('\n')

    def predict(self, X, feature_num, class_num):

        classifier = classifier_model.get_dnn_classifier(feature_num, class_num, self.__configure__.get_classifier_model_dir())

        predict_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X}, y=None, num_epochs=1, shuffle=False)
        predictions = list(classifier.predict(input_fn = predict_input_fn))
        y_prob = [p['probabilities'] for p in predictions]
        return y_prob