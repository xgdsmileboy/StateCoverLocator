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
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.model_selection import cross_val_score
from sklearn import svm
from sklearn.ensemble import RandomForestClassifier
from sklearn import metrics
from XGBoost_var import var_model
from sklearn.preprocessing import OneHotEncoder

import tensorflow as tf
import shutil as su



class Train(object):

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

        train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=y_train, num_epochs=100, shuffle=True)
        test_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_valid}, y=y_valid, num_epochs=1, shuffle=True)
        classifier.train(input_fn=train_input_fn)
        accuracy_score = classifier.evaluate(input_fn=test_input_fn)["accuracy"]
        print("\nTest Accuracy: {0:f}\n".format(accuracy_score))

    def train_dnn(self, X, Y, feature_num, class_num):
        su.rmtree(self.__configure__.get_var_nn_model_dir())
        classifier = var_model.get_dnn_classifier(feature_num, class_num, self.__configure__.get_var_nn_model_dir())

        train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X}, y=Y, num_epochs=100, shuffle=True)
        classifier.train(input_fn=train_input_fn)

    def train(self, feature_num, training_objective, str_encoder, evaluate):
        start_time = datetime.datetime.now()

        data_file_path = self.__configure__.get_raw_var_train_in_file()

        # load data from a csv file
        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
        dataset = data.values
        print('Dataset size: {}'.format(dataset.shape))
        # split data into X and y
        # 3 to 11: 8
        X = dataset[:, 3:3 + feature_num]
        X = X.astype(str)
        Y = dataset[:, 3 + feature_num]
        Y = Y.astype(str)
        classes = np.unique(Y)
        class_num = len(classes)
        print('Class number: %d' % class_num)

        # encoding string as integers
        encoded_X = None
        x_encoders = [None] * feature_num
        for i in range(0, X.shape[1]):
            feature = np.zeros((X.shape[0], 1))
            if i <= 4 or i == 10 or i == 11:
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
            if encoded_X is None:
                encoded_X = feature
            else:
                encoded_X = np.concatenate((encoded_X, feature), axis=1)
        # encoded targets = original targets
        y_encoder = LabelEncoder()
        encoded_Y = y_encoder.fit_transform(Y)

        model_file = self.__configure__.get_var_model_file()

        # init the model
        if os.path.exists(model_file):
            print('Model file already exists and be removed.')
            os.remove(model_file)

        print('Training the model...')

        if (self.__configure__.get_model_type() == 'xgboost'):
            if evaluate:
                X_train, X_valid, y_train, y_valid = train_test_split(encoded_X, encoded_Y, test_size=0.3, random_state=7)
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
                    'eval_metric': 'error',
                    # 'eval_metric': 'rmse',
                    'silent': 1,
                    'lambda': 2
                }
                num_round = 700
                early_stop = 50
                learning_rates = [(num_round - i) / (num_round * 10.0) for i in range(num_round)]

                watchlist = [(M_train, 'train'), (M_test, 'eval')]
                model = xgb.train(params, M_train, num_boost_round=num_round, evals=watchlist,
                            early_stopping_rounds=early_stop, learning_rates=learning_rates)
                M_valid = xgb.DMatrix(X_valid)
                y_prob = model.predict(M_valid)
                def to_classes(x):
                    if x >= 0.5:
                        return 1
                    else:
                        return 0

                y_classes = map(to_classes, y_prob)
                print("\nTest Accuracy: {0:f}\n".format(metrics.accuracy_score(y_valid, y_classes)))
            else:
                # split the data into training and validating set
                X_train, X_valid, y_train, y_valid = train_test_split(encoded_X, encoded_Y, test_size=0.2, random_state=7)
                print('Training set size: {}'.format(X_train.shape))
                print('Validating set size: {}'.format(X_valid.shape))

                ### another way to train the model
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
                    'eval_metric': 'error',
                    # 'eval_metric': 'rmse',
                    'silent': 1,
                    'lambda': 2
                }
                num_round = 700
                early_stop = 50
                learning_rates = [(num_round - i) / (num_round * 10.0) for i in range(num_round)]

                watchlist = [(M_train, 'train'), (M_valid, 'eval')]
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
            #         sc = cross_val_score(model, encoded_X, encoded_Y, scoring = 'neg_mean_squared_error')
            #         if sc.mean() < min_score:
            #             best_model = model
            #             min_score = sc.mean()
            #     best_model.fit(encoded_X, encoded_Y);

            #     # save the best model on the disk
            #     with open(model_file, 'w') as f:
            #         pk.dump(best_model, f)
            #         print('Model saved in {}'.format(model_file))
        elif (self.__configure__.get_model_type() == 'randomforest'):
            model = RandomForestClassifier(random_state = 0)
            model.fit(encoded_X, encoded_Y)
            with open(model_file, 'w') as f:
                pk.dump(model, f)
                print('Model saved in {}'.format(model_file))
        elif (self.__configure__.get_model_type() == 'dnn'):
            if evaluate:
                self.evaluate_dnn(encoded_X, encoded_Y, encoded_X.shape[1], y_encoder.classes_.shape[0])
            else:
                # encoded_input = pd.DataFrame(np.concatenate((encoded_X, encoded_Y.reshape(encoded_Y.shape[0], 1)), axis=1))
                # encoded_input.to_csv(self.__configure__.get_var_nn_training_file(), index = False, header = False)

            # # add header for tensorflow csv
            # csv_format_data = None
            # with open(self.__configure__.get_var_nn_training_file(), 'r') as f:
            #     csv_format_data = f.read()

            # with open(self.__configure__.get_var_nn_training_file(), 'w') as f:
            #     f.write('%d,' % encoded_X.shape[0])
            #     f.write('%d,' % feature_num)
            #     f.write('0,1\n')
            #     f.write('%s' % csv_format_data)
                print(encoded_X.shape[1])
                self.train_dnn(encoded_X, encoded_Y, encoded_X.shape[1], y_encoder.classes_.shape[0])

        end_time = datetime.datetime.now()
        run_time = end_time-start_time
        print('Training fininshed, time cost : {}'.format(run_time))