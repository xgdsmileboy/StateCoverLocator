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

class Train(object):

    def __init__(self, configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure


    def train(self, feature_num, training_objective, var_encoder):
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
            x_encoders[i] = LabelEncoder()
            feature = x_encoders[i].fit_transform(X[:, i])
            feature = feature.reshape(X.shape[0], 1)
            if i == 2:
                # variable name
                for j in range(0, X.shape[0]):
                    feature[j] = var_encoder[str(X[j, i]).lower()]
            if i == 5:
                # dist0
                for j in range(0, X.shape[0]):
                    feature[j] = int(X[j, i])
            if encoded_X is None:
                encoded_X = feature
            else:
                encoded_X = np.concatenate((encoded_X, feature), axis=1)
        # encoded targets = original targets
        y_encoder = LabelEncoder()
        encoded_Y = y_encoder.fit_transform(Y)

        # split the data into training and validating set
        X_train, X_valid, y_train, y_valid = train_test_split(encoded_X, encoded_Y, test_size=0.2, random_state=7)
        print('Training set size: {}'.format(X_train.shape))
        print('Validating set size: {}'.format(X_valid.shape))

        model_file = self.__configure__.get_var_model_file()


        # init the model
        if os.path.exists(model_file):
            print('Model file already exists and be removed.')
            os.remove(model_file)

        print('Training the model...')

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

        end_time = datetime.datetime.now()
        run_time = end_time-start_time
        print('Training fininshed, time cost : {}'.format(run_time))