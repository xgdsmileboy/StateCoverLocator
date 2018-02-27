import pandas as pd
from sklearn.model_selection import train_test_split
import xgboost as xgb
import numpy as np
import pickle
import heapq
import os
import time
import gc
from sklearn import metrics
import logging

from sklearn import cross_validation, metrics
from sklearn.model_selection import GridSearchCV

from pca.PCAColumn import PCAColumn
from Utils.config import Configure
from pca.PCAForXgb import PCAForXgb
import tensorflow as tf
import shutil as su
import numpy as np
from XGBoost_expr import expr_model

class ExprWithPCA(PCAForXgb):

    def prepreocess(self, ori_file_data, all_pca_added_df, is_training=False):
        """
        :param ori_file_data: DataFrame
        :param all_pca_added_df: DataFrame
        :return X: matrix of features, Y: labels
        """
        assert ori_file_data.shape[0] == all_pca_added_df.shape[0]

        ori_file_data.drop(['id', 'line', 'column'], axis=1, inplace=True)
        Y = ori_file_data['pred']
        ori_file_data.drop(['pred'], axis=1, inplace=True)
        X = pd.concat([ori_file_data, all_pca_added_df], axis=1)

        assert X.shape[0] == Y.shape[0]

        columns = [i for i in X.columns.tolist()]

        # 'mtdmod',
        label_encoded_cols = ['filename','tdname','methodname',
                'allloc', 'allloctp','allfld', 'allfldtp','bodyctl','befsyn','bdsyn','afsyn',
                'bes0','bes1','bes2','bes3','bes4','bes5','bds0','bds1','bds2','bds3','afs0','afs1',
                'afs2','afs3','lv0','lv1','lv2','lv3','befcd','befpre','pred',
                'pstmt0', 'pstmt1', 'nstmt0', 'nstmt1',
                'varname', 'vartype', 'ltt0', 'ltt1', 'ltt2', 'wd0', 'wd1', 'wd2', 'twdl0',
                'docexcp', 'docop',
                'varpre1st', 'varpre2nd', 'tppre1st', 'tppre2nd', 'tppre3rd',
                'lastassign','bodyuse', 'outuse', 'lastpre']


        #for training
        if is_training:
           all_encoders = {}
           for col in columns:
               if str(col) in label_encoded_cols:
                   all_encoders[col] = {}
                   X[col] = self.encoder_column(X[col], all_encoders[col])

           all_encoders['pred'] = {}
           Y = self.encoder_column(Y, all_encoders['pred'])
           with open(self.__configure__.get_all_label_encoders('expr'), 'wb') as f:
               pickle.dump(all_encoders, f, protocol=2)

        #for predicting
        else:
            with open(self.__configure__.get_all_label_encoders('expr'), 'r') as f:
                all_encoders = pickle.load(f)
            Y = None
            for col in columns:
                if str(col) in label_encoded_cols:
                    X[col] = self.encoder_column(X[col], all_encoders[col])

                #TODO: need to dump changed encoder for backup?

        return X, Y


    def get_predicated_label(self, y_pred):
        result = []
        for row in y_pred:
            # i'th pred item
            curr_max = 0
            max_idx = -1
            s = ''
            for idx, p in enumerate(row):
                if p > curr_max:
                    curr_max = p
                    max_idx = idx

            result.append(max_idx)

        # print result
        return result

    def add_to_frequency(self, ori_file_data, frequency=5):
        ori_line = ori_file_data.shape[0]
        uniques = ori_file_data['pred'].value_counts(sort=True)

        new_ori_row_list = []
        ori_cols = ori_file_data.columns

        for x in xrange(len(ori_file_data.index)):
            new_ori_row_list.extend(np.array(ori_file_data[x:x + 1]).tolist())
            pred = ori_file_data['pred'].iloc[x]
            curr_freq = uniques[pred]
            for i in xrange(curr_freq, frequency + 1):
                new_ori_row_list.extend(np.array(ori_file_data[x:x + 1]).tolist())

        ori_file_data = pd.DataFrame(new_ori_row_list, columns=ori_cols)

        curr_line = ori_file_data.shape[0]
        logging.info('CHANGE %d LINES TO %d LINES BY FREQ %d' % (ori_line, curr_line, frequency))

        pd.set_option('display.max_columns', None)

        # logging.info(ori_file_data.describe(include='all'))
        gc.collect()
        return ori_file_data

    def filter_by_frequency(self, ori_file_data, frequency=1):
        ori_line = ori_file_data.shape[0]

        uniques = ori_file_data['pred'].value_counts(sort=True)
        more_than_freq_time = uniques[uniques > frequency].index.values.tolist()

        new_ori_row_list = []
        ori_cols = ori_file_data.columns
        for x in xrange(len(ori_file_data.index)):
            if ori_file_data['pred'].iloc[x] in more_than_freq_time:
                new_ori_row_list.extend(np.array(ori_file_data[x:x + 1]).tolist())

        ori_file_data = pd.DataFrame(new_ori_row_list, columns=ori_cols)

        curr_line = ori_file_data.shape[0]

        logging.info('ALL THE %d LINES HAS %d LINES FREQ > %d' %(ori_line, curr_line, frequency))

        # logging.info(ori_file_data.describe(include='all'))
        gc.collect()
        return ori_file_data


    def train(self, use_dnn, evaluate):
        '''
        entrance of expr training
        :return: 
        '''
        logging.info('>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> START EXPR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>')
        pca_start_time = time.time()

        expr_file = self.__configure__.get_raw_expr_train_in_file()
        ori_file_data = pd.read_csv(expr_file, sep='\t', header=0, encoding='utf-8')

        # ori_file_data = self.filter_by_frequency(ori_file_data)
        ori_file_data = self.add_to_frequency(ori_file_data)

        all_pca_added_df = PCAColumn.get_pca_dataframe(ori_file_data,
                                                   self.__configure__,
                                                   'expr',
                                                   Configure.get_expr_tags_for_pca(),
                                                   is_training=True)
        pca_end_time = time.time()
        logging.info('EXPR TRAINING PCA TIME: {} s'.format(pca_end_time - pca_start_time))

        class_num = len(ori_file_data['pred'].value_counts())
        logging.info('UNIQUE EXPR NUM: {}'.format(class_num))

        # label encoder
        X, Y = self.prepreocess(ori_file_data, all_pca_added_df, True)
        encoder_end_time = time.time()
        logging.info('EXPR TRAINING ENCODER TIME: {} s'.format(encoder_end_time - pca_end_time))

        X_train, X_valid, y_train, y_valid = train_test_split(X, Y, test_size=0.1, random_state=7)

        gc.collect()

        if evaluate:
            self.evaluate_model(X, Y, use_dnn, class_num)
            return

        logging.info('EXPR TRAINING SET SIZE: {}'.format(X_train.shape))
        logging.info('VALIDATION SET SIZE: {}'.format(X_valid.shape))

        if not use_dnn:
            M_train = xgb.DMatrix(X_train, label=y_train)
            M_valid = xgb.DMatrix(X_valid, label=y_valid)

            # cv_model = self.xgb_cla ssifier_cv(X_train, y_train, X_valid, y_valid)
            model = self.xbg_inner(M_train, M_valid, class_num)

            model_file = self.__configure__.get_expr_model_file()
            with open(model_file, 'wb') as f:
                pickle.dump(model, f, protocol=2)
                print('EXPR MODEL SAVED AS {}'.format(model_file))

            train_end_time = time.time()

            logging.info("EXPR best score: {}".format(model.best_score))
            logging.info("EXPR XGB Training Time: {} s".format(train_end_time - pca_end_time))

            y_pred = model.predict(M_valid)
            y_pred_label = self.get_predicated_label(y_pred)
            self.get_matrics(y_valid, y_pred_label)
        else:
            feature_num = X.shape[1]
            print('Feature num: %d' % feature_num)
            self.train_dnn(np.array(X.values).astype('float64'), np.array(Y.values).astype('float64'), feature_num, class_num)

    def train_dnn(self, X, Y, feature_num, class_num):
        su.rmtree(self.__configure__.get_expr_l2snn_model_dir())
        classifier = expr_model.get_dnn_classifier(feature_num, class_num, self.__configure__.get_expr_l2snn_model_dir())

        train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X}, y=Y, num_epochs=100, shuffle=True)
        classifier.train(input_fn=train_input_fn)

    def evaluate_model(X, Y, use_dnn, class_num):
        if not use_dnn:
            X_train, X_valid, y_train, y_valid = train_test_split(X, Y, test_size=0.1, random_state=7)
            X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=0.1, random_state=7)

            M_train = xgb.DMatrix(X_train, label=y_train)
            M_valid = xgb.DMatrix(X_valid, label=y_valid)
            M_test = xgb.DMatrix(X_test, label=y_test)

            # cv_model = self.xgb_cla ssifier_cv(X_train, y_train, X_valid, y_valid)
            model = self.xbg_inner(M_train, M_test, class_num)

            #model_file = self.__configure__.get_expr_model_file()
            #with open(model_file, 'wb') as f:
            #    pickle.dump(model, f, protocol=2)
            #    print('EXPR MODEL SAVED AS {}'.format(model_file))

            #train_end_time = time.time()

            logging.info("EXPR best score: {}".format(model.best_score))
            #logging.info("EXPR XGB Training Time: {} s".format(train_end_time - pca_end_time))

            y_pred = model.predict(M_valid)
            y_pred_label = self.get_predicated_label(y_pred)
            self.get_matrics(y_valid, y_pred_label)
        else:
            feature_num = X.shape[1]
            print('Feature num: %d' % feature_num)
            self.evaluate_dnn(np.array(X.values), np.array(Y.values), feature_num, class_num)

    def xbg_inner(self, M_train, M_valid, class_num, early_stop=50):

        params = {
            'booster': 'gbtree',
            'objective': 'multi:softprob',
            'max_depth': 7,
            'eta': 0.1,
            'gamma': 0.2,
            'subsample': 0.7,
            'col_sample_bytree': 0.2,
            'min_child_weight': 1,
            'save_period': 0,
            'eval_metric': 'merror',  # merror or mlogloss
            'silent': 1,
            'lambda': 1,
            'num_class': class_num
        }
        num_round = 1000
        learning_rates = [(num_round - i) / (num_round * 5.0) for i in range(num_round)]
        watchlist = [(M_train, 'train'), (M_valid, 'eval')]
        model = xgb.train(params, M_train, num_boost_round=num_round, evals=watchlist,
                      early_stopping_rounds=early_stop, learning_rates=learning_rates)
        return model


    def xgb_classifier_cv(self, X_train, y_train, X_valid, y_valid, early_stop=30):
        params = {
            'max_depth': 7,
            'learning_rate': 0.1,
            'objective': 'multi:softprob',
            'booster': 'gbtree',
            'gamma': 0.2,
            'subsample': 0.7,
            'colsample_bytree': 0.2,
            'min_child_weight': 1,
            'silent': False,
            # 'seed': 260817,
        }

        model = xgb.XGBClassifier(**params)

        param_test1 = {
            'max_depth': range(6, 10, 2),
            # 'min_child_weight': range(1, 6, 2)
        }

        gsearch1 = GridSearchCV(estimator=model,
                                param_grid=param_test1, scoring='roc_auc', n_jobs=8, iid=False, cv=3)

        gsearch1.fit(X_train, y_train)
        print gsearch1.grid_scores_, gsearch1.best_params_, gsearch1.best_score_

        model.fit(X_train, y_train, early_stopping_rounds=early_stop, eval_metric='merror',
                    eval_set=[(X_valid, y_valid)])
        return model

    def get_matrics(self, y_true, y_pred):
        file = open(self.__configure__.get_all_label_encoders('expr'), 'r')
        all_encoders = pickle.load(file)
        Y_encoder = all_encoders['pred']
        inverse_label_encoder = dict(zip(Y_encoder.values(), Y_encoder.keys()))

        y_true_ori = []
        y_pred_ori = []
        for x,y in zip(y_true, y_pred):
            y_true_ori.append(inverse_label_encoder[x])
            y_pred_ori.append(inverse_label_encoder[y])

        classify_report = metrics.classification_report(y_true_ori, y_pred_ori)
        logging.info(classify_report)


    def predict(self, use_dnn):
        data_file_path = self.__configure__.get_raw_expr_pred_in_file()
        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')

        pca_df = PCAColumn.get_pca_dataframe(data,
                                             self.__configure__,
                                             'expr',
                                             Configure.get_expr_tags_for_pca(),
                                             is_training=False)

        # print pca_df.shape

        X, nop_Y = self.prepreocess(data, pca_df, False)

        model_file = self.__configure__.get_expr_model_file()
        if not use_dnn:
            xgb_expr_model = pickle.load(open(model_file, 'r'))

            M_pred = xgb.DMatrix(X)
            y_prob = xgb_expr_model.predict(M_pred)
        else:
            with open(self.__configure__.get_all_label_encoders('expr'), 'r') as model:
                all_encoders = pickle.load(model)
                Y_encoder = all_encoders['pred']
                classifier = expr_model.get_dnn_classifier(X.shape[1], len(Y_encoder), self.__configure__.get_expr_l2snn_model_dir())
                predict_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': np.array(X.values)}, y=None, num_epochs=1, shuffle=False)
                predictions = list(classifier.predict(input_fn = predict_input_fn))
                y_prob = [p['probabilities'] for p in predictions]

        line = y_prob[0] # only has one line

        top =  self.__configure__.get_gen_expr_top()
        if line.shape[0] < top:
            top = line.shape[0]

        #TODO learn heapq
        alts = heapq.nlargest(top, range(len(line)), line.__getitem__)

        expr_predicted = self.__configure__.get_expr_pred_out_file()
        if os.path.exists(expr_predicted):
            os.remove(expr_predicted)
        with open(expr_predicted, 'w') as output:
            with open(self.__configure__.get_all_label_encoders('expr'), 'r') as model:
                all_encoders = pickle.load(model)
                Y_encoder = all_encoders['pred']
                inverse_label_encoder = dict(zip(Y_encoder.values(), Y_encoder.keys()))
                for j in range(top):
                    label = alts[j]
                    original = inverse_label_encoder[label]

                    output.write('{}'.format(original))  # predicate
                    output.write('\t%.17f' % line[alts[j]])
                    output.write('\n')
                    # print j, '\t', original, '\t', line[alts[j]]
