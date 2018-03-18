from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from sklearn.model_selection import cross_val_score, KFold
from xgboost import XGBClassifier
import xgboost as xgb
import pickle as pk
import os
import pandas as pd
import datetime
from Utils.config import *
import numpy as np
import collections
from sklearn.preprocessing import LabelEncoder
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split
from sklearn.model_selection import cross_val_score
from sklearn.tree import DecisionTreeClassifier
from sklearn import svm
from sklearn.ensemble import RandomForestClassifier
from XGBoost_expr import expr_model

import tensorflow as tf
import shutil as su

class TrainExpr(object):

    def __init__(self, configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure

    def evaluate_dnn(self, X, Y, feature_num, class_num):
        X_train, X_valid, y_train, y_valid = train_test_split(X, Y, test_size=0.3, random_state=7)
        feature_columns = [tf.feature_column.numeric_column("x", shape=[feature_num])]
        classifier = tf.estimator.DNNClassifier(feature_columns = feature_columns,
                                              hidden_units = [64, 64, 64, 64, 64, 64],
                                              n_classes = class_num)

        train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=y_train, num_epochs=10000, shuffle=True)
        test_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_valid}, y=y_valid, num_epochs=1, shuffle=True)
        same_train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=y_train, num_epochs=1, shuffle=True)
        classifier.train(input_fn=train_input_fn)
        accuracy_score = classifier.evaluate(input_fn=test_input_fn)["accuracy"]
        print("\nTest Accuracy: {0:f}\n".format(accuracy_score))
        accuracy_score_train = classifier.evaluate(input_fn=same_train_input_fn)["accuracy"]
        print("\nTrain Accuracy: {0:f}\n".format(accuracy_score_train))

    def train_dnn(self, X, Y, feature_num, class_num):
        su.rmtree(self.__configure__.get_expr_nn_model_dir())
        classifier = expr_model.get_dnn_classifier(feature_num, class_num, self.__configure__.get_expr_nn_model_dir())

        train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X}, y=Y, num_epochs=1000, shuffle=True)
        classifier.train(input_fn=train_input_fn)

    def train(self, feature_num, training_objective, str_encoder, evaluate):

        start_time = datetime.datetime.now()

        # data_file_path = self.__configure__.get_raw_expr_train_in_file()
        data_file_path = self.__configure__.get_raw_expr_train_in_file()
        model_file = self.__configure__.get_expr_model_file()

        # load data from a csv file
        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
        dataset = data.values
        print('Raw data size: {}'.format(dataset.shape))
        # split data into X and y
        X = dataset[:, 3:3 + feature_num]
        X = X.astype(str)
        Y = dataset[:, 3 + feature_num]
        # Y = dataset[:, target_col:target_col+1]
        # print(Y)
        # encoding string as integers
        encoded_x = None
        x_encoders = [None] * feature_num
        for i in range(0, X.shape[1]):
            feature = np.zeros((X.shape[0], 1))
            if i <= 4 or i == 9 or i == 10 or i == 11:
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
                #elif i == 5:
                    # dist0
                    #for j in range(0, X.shape[0]):
                        #feature[j] = int(X[j, i])
                one_hot_encoder = OneHotEncoder(sparse=False)
                feature = one_hot_encoder.fit_transform(feature)
            else:
                for j in range(0, X.shape[0]):
                    feature[j] = int(X[j, i])
            if encoded_x is None:
                encoded_x = feature
            else:
                encoded_x = np.concatenate((encoded_x, feature), axis=1)

        y_encoder = LabelEncoder()
        encoded_y = y_encoder.fit_transform(Y)

        classes = np.unique(Y)
        class_num = len(classes)
        print('All class num: %d' % class_num)
        # count the examples and drop the infrequent ones
        counter = collections.Counter(Y)
        frequency = self.__configure__.get_expr_frequency()

        frequent_set = list()
        for c in counter.items():
            if c[1] > frequency:
                frequent_set.append(c[0])

        frequent_class_num = len(frequent_set)
        print('Frequent class(>{}) num: {}'.format(frequency, frequent_class_num))

        frequent_X = list()
        frequent_y = list()
        for i in range(0, Y.shape[0]):
            if Y[i] in frequent_set:
                frequent_X.append(encoded_x[i])
                frequent_y.append(encoded_y[i])

        frequent_X = pd.DataFrame(frequent_X)
        frequent_y = pd.DataFrame(frequent_y)

        # init the model
        if os.path.exists(model_file):
            print('Model file already exists and be removed.')
            os.remove(model_file)

        print('Training the model...')
        # model = XGBClassifier(max_depth=6, objective=training_objective, silent=True, learning_rate=0.1)
        # # train the model
        # model.fit(X, y)
        # model.fit(X, y)

        ### another way to train the model

        if (self.__configure__.get_model_type() == 'xgboost'):
            if evaluate:
                X_train, X_valid, y_train, y_valid = train_test_split(frequent_X, frequent_y, test_size=0.3, random_state=7)
                X_train, X_test, y_train, y_test = train_test_split(X_train, y_train, test_size=0.1, random_state=7)
                ### another way to train the model
                M_train = xgb.DMatrix(X_train, label=y_train)
                M_test = xgb.DMatrix(X_test, label=y_test)

                params = {
                    'booster': 'gbtree',
                    'objective': training_objective,
                    'max_depth': 6,
                    'eta': 0.1,
                    'gamma': 0.2,
                    'subsample': 0.7,
                    'col_sample_bytree': 0.2,
                    'min_child_weight': 1,
                    'save_period': 0,
                    'eval_metric': 'merror',
                    'silent': 1,
                    'lambda': 2,
                    'num_class': class_num
                }
                num_round = 1000
                early_stop = 30
                learning_rates = [(num_round - i) / (num_round * 10.0) for i in range(num_round)]

                watchlist = [(M_train, 'train'), (M_test, 'eval')]
                model = xgb.Booster()
                model = xgb.train(params, M_train, num_boost_round=num_round, evals=watchlist,
                            early_stopping_rounds=early_stop, learning_rates=learning_rates)
                M_valid = xgb.DMatrix(X_valid)
                y_prob = model.predict(M_valid)
                print("\nTest Accuracy: {0:f}\n".format(metrics.accuracy_score(np.argmax(y_valid, axis=1), np.argmax(y_prob, axis=1))))
            else:
                # split the data into training and validing set
                X_train, X_valid, y_train, y_valid = train_test_split(frequent_X, frequent_y, test_size=0.1, random_state=7)
                print('Training set size: {}'.format(y_train.shape))
                print('Validation set size: {}'.format(y_valid.shape))

                M_train = xgb.DMatrix(X_train, label=y_train)
                M_valid = xgb.DMatrix(X_valid, label=y_valid)

                params = {
                    'booster': 'gbtree',
                    'objective': training_objective,
                    'max_depth': 6,
                    'eta': 0.1,
                    'gamma': 0.2,
                    'subsample': 0.7,
                    'col_sample_bytree': 0.2,
                    'min_child_weight': 1,
                    'save_period': 0,
                    'eval_metric': 'merror',
                    'silent': 1,
                    'lambda': 2,
                    'num_class': class_num
                }
                num_round = 1000
                early_stop = 30
                learning_rates = [(num_round - i) / (num_round * 10.0) for i in range(num_round)]

                watchlist = [(M_train, 'train'), (M_valid, 'eval')]
                model = xgb.Booster()
                model = xgb.train(params, M_train, num_boost_round=num_round, evals=watchlist,
                            early_stopping_rounds=early_stop, learning_rates=learning_rates)

                # save the best model on the disk
                with open(model_file, 'w') as f:
                    pk.dump(model, f)
                    print('Model saved in {}'.format(model_file))
            # elif (self.__configure__.get_model_type() == 'svm'):
            #     cs = (0.1, 1, 10, 100)
            #     min_score = 1
            #     best_model = svm.SVC(kernel = 'rbf', probability = True, C = 1)
            #     for c in cs:
            #         model = svm.SVC(kernel = 'rbf', probability = True, C = c)
            #         sc = cross_val_score(model, frequent_X, np.ravel(frequent_y), scoring = 'neg_mean_squared_error')
            #         if sc.mean() < min_score:
            #             best_model = model
            #             min_score = sc.mean()
            #     best_model.fit(frequent_X, np.ravel(frequent_y))

            #     # save the best model on the disk
            #     with open(model_file, 'w') as f:
            #         pk.dump(best_model, f)
            #         print('Model saved in {}'.format(model_file))

        elif (self.__configure__.get_model_type() == 'randomforest'):
            model = RandomForestClassifier(random_state = 0)
            if evaluate:
                X_train, X_valid, y_train, y_valid = train_test_split(frequent_X, frequent_y, test_size=0.3, random_state=7)
                model.fit(X_train, y_train)
                score1 = model.score(X_train, y_train)
                score2 = model.score(X_valid, y_valid)
                print("\nTest Accuracy: {0:f}\n".format(score2))
                print("\nTrain Accuracy: {0:f}\n".format(score1))
            else:
                model.fit(frequent_X, frequent_y)
                with open(model_file, 'w') as f:
                    pk.dump(model, f)
                    print('Model saved in {}'.format(model_file))
        elif (self.__configure__.get_model_type() == 'tree'):
            model = DecisionTreeClassifier(random_state = 0)
            if evaluate:
                X_train, X_valid, y_train, y_valid = train_test_split(frequent_X, frequent_y, test_size=0.3, random_state=7)
                model.fit(X_train, y_train)
                score1 = model.score(X_train, y_train)
                score2 = model.score(X_valid, y_valid)
                print("\nTest Accuracy: {0:f}\n".format(score2))
                print("\nTrain Accuracy: {0:f}\n".format(score1))
            else:
                model.fit(frequent_X, frequent_y)
                with open(model_file, 'w') as f:
                    pk.dump(model, f)
                    print('Model saved in {}'.format(model_file))
        elif (self.__configure__.get_model_type() == 'dnn'):
            X_input = frequent_X.values
            Y_input = frequent_y.values
            if evaluate:
                self.evaluate_dnn(np.array(X_input), np.array(Y_input), frequent_X.shape[1], y_encoder.classes_.shape[0])
            else:
                # encoded_input = pd.DataFrame(np.concatenate((X_input, Y_input.reshape(Y_input.shape[0], 1)), axis=1))
                # encoded_input.to_csv(self.__configure__.get_expr_nn_training_file(), index = False, header = False)

                # # add header for tensorflow csv
                # csv_format_data = None
                # with open(self.__configure__.get_var_nn_training_file(), 'r') as f:
                #     csv_format_data = f.read()

                # with open(self.__configure__.get_var_nn_training_file(), 'w') as f:
                #     f.write('%d,' % encoded_X.shape[0])
                #     f.write('%d,' % feature_num)
                #     f.write('0,1\n')
                #     f.write('%s' % csv_format_data)
                print(frequent_X.shape[1])
                self.train_dnn(np.array(X_input), np.array(Y_input), frequent_X.shape[1], y_encoder.classes_.shape[0])

        end_time = datetime.datetime.now()
        run_time = end_time-start_time
        print('Training fininshed, time cost : {}'.format(run_time))