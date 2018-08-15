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

    def train_classifier(selfs, feature_num):

        return classifier

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

    def predict(self, X, feature_num, class_num, is_var):

        classifier = classifier_model.get_dnn_classifier(feature_num, class_num, self.__configure__.get_var_nn_model_dir())

        predict_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X}, y=None, num_epochs=1, shuffle=False)
        predictions = list(classifier.predict(input_fn = predict_input_fn))
        y_prob = [p['probabilities'] for p in predictions]
        return y_prob