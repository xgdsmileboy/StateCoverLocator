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
from sklearn.model_selection import train_test_split

class TrainExpr(object):

    def __init__(self, configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure


    def train(self, feature_num, training_objective, var_encoder):

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
            x_encoders[i] = LabelEncoder()
            feature = x_encoders[i].fit_transform(X[:, i])
            feature = feature.reshape(X.shape[0], 1)
            if i == 2:
                # variable name
                for j in range(0, X.shape[0]):
                    feature[j] = var_encoder[str(X[j, i]).lower()]
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
        # split the data into training and validing set
        X_train, X_valid, y_train, y_valid = train_test_split(frequent_X, frequent_y, test_size=0.1, random_state=7)
        print('Training set size: {}'.format(y_train.shape))
        print('Validation set size: {}'.format(y_valid.shape))


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

        # model.save_model(model_file)
        # print('Model saved in {}'.format(model_file))

        end_time = datetime.datetime.now()
        run_time = end_time-start_time
        print('Training fininshed, time cost : {}'.format(run_time))