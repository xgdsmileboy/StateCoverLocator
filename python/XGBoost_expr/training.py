from sklearn.model_selection import cross_val_score, KFold
from xgboost import XGBClassifier
import xgboost as xgb
import pickle as pk
import os
import pandas as pd
import datetime

def train(data_file_path, model_file_path, feature_num, training_objective, class_num):
    start_time = datetime.datetime.now()
    # clear the saved model
    data_file = data_file_path.split('/')[-1][0:-13]
    model_file = model_file_path + data_file + '_model.pkl'
    encoded_data_file = data_file_path

    # check the encoded data
    training_file = data_file_path[0:-13]+'_train.csv'
    validating_file = data_file_path[0:-13]+'_valid.csv'
    if not os.path.exists(training_file):
        print('Training set file does not exist!')
        os._exit(0)

    # load original data(for cross validation)
    data = pd.read_csv(encoded_data_file, sep=',', header=None, encoding='utf-8')
    dataset = data.values
    X = dataset[:, 0:feature_num]
    y = dataset[:, feature_num]
    print('Training data size: {}'.format(dataset.shape))
    # load encoded training set
    # training_data = pd.read_csv(training_file, sep=',', header=None, encoding='utf-8')
    # training_data_values = training_data.values
    # print('Training set size: {}'.format(training_data_values.shape))
    # X_train = training_data_values[:, 0:feature_num]
    # y_train = training_data_values[:, feature_num]
    # # load encoded validation set
    testing_data = pd.read_csv(validating_file, sep=',', header=None, encoding='utf-8')
    testing_data_values = testing_data.values
    print('Validating data size: {}'.format(testing_data_values.shape))
    X_valid = testing_data_values[:, 0:feature_num]
    y_valid = testing_data_values[:, feature_num]

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
    M_train = xgb.DMatrix(X, label=y)
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
    early_stop = 10
    learning_rates = [(num_round - i) / (num_round * 10.0) for i in range(num_round)]

    watchlist = [(M_train, 'train'), (M_valid, 'eval')]
    model = xgb.Booster()
    model = xgb.train(params, M_train, num_boost_round=num_round, evals=watchlist,
                    early_stopping_rounds=early_stop, learning_rates=learning_rates)
    # model = xgb.train(params, M_train, evals=watchlist)
    # print(model.predict(M_valid))

    ### do cv with the func
    # cv_scores = cross_val_score(model, X, y, cv=5, n_jobs=-1)
    # # # print("Cross validation score: %0.5f (+/- %0.5f)" % (cv_score.mean(), cv_score.std() * 2))
    # print('CV scores: {}'.format(cv_scores))

    ### or mannuly split the data and do a 5 cv
    # k_fold = KFold(n_splits=5)
    # for i in range(5):
    #     print('Doing No.{} CV...'.format(i))
    #     for train_indices, test_indices in k_fold.split(X):
    #         cvm = model.fit(X[train_indices], y[train_indices])

    ## save the model with pickle
    # with open(model_file, 'w') as f:
    #     pk.dump(model, f)
    #     print('Model saved in {}'.format(model_file))
    ## save the model in xgboost style
    model.save_model(model_file)
    print('Model saved in {}'.format(model_file))

    end_time = datetime.datetime.now()
    run_time = end_time-start_time
    print('Training fininshed, time cost : {}'.format(run_time))