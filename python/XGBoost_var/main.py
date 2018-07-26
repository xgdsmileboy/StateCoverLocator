from training import *
from Utils.config import *
import pandas as pd
from clustering.cluster import *

import tensorflow as tf
from DNN.dnn import *

class XGVar(object):

    def __init__(self,  configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure;

    def train_var(self, str_encoder, feature_num, evaluate):
        print('Training var model for {}_{}...'.\
              format(self.__configure__.get_project_name(), self.__configure__.get_bug_id()))

        # feature_num = # cols - 1(only one target)
        train = Train(self.__configure__)
        train.train(feature_num, 'binary:logistic', str_encoder, evaluate)

    def predict_vars(self, encoded_var, feature_num):
        # encoded_oracle_var =  oracle[0:-4]+ '.var_encoded.csv'
        var_predicted = self.__configure__.get_var_pred_out_file()
        if os.path.exists(var_predicted):
            os.remove(var_predicted)

        raw_var_path = self.__configure__.get_raw_var_pred_in_file()
        raw_var = pd.read_csv(raw_var_path, sep='\t', header=0)
        raw_var_values = raw_var.values

        varnames = list()
        line_ids = list()
        for r in range(0, encoded_var.shape[0]):
            line_ids.append(raw_var_values[r, 2] + "::" + str(raw_var_values[r, 0]) + "::" + raw_var_values[r, 4])
            varnames.append(raw_var_values[r, 4])

        encoded_rows_array = np.array(encoded_var)
        # print(encoded_rows_array.shape)
        X_pred = encoded_rows_array[:, 0:-1]
        X_pred = X_pred.astype(float)

        y_pred = encoded_rows_array[:, -1]

        y_pred = y_pred.astype(float)

        # load the model from file
        if (self.__configure__.get_model_type() != 'dnn'):
            var_model_path = self.__configure__.get_var_model_file()
            if not os.path.exists(var_model_path):
                print('Model file does not exist!')
                os._exit(0)
            with open(var_model_path, 'r') as f:
                model = pk.load(f)
                print('Model loaded from {}'.format(var_model_path))
                print(model)

            if (self.__configure__.get_model_type() == 'xgboost'):
                M_pred = xgb.DMatrix(X_pred, label=y_pred)
                y_prob = model.predict(M_pred)

                with open(var_predicted, 'w') as f:
                    for i in range(0, X_pred.shape[0]):
                        f.write('%s\t' % line_ids[i])
                        f.write('%s\t' % varnames[i])
                        f.write('%.4f' % y_prob[i])
                        f.write('\n')
            # elif (self.__configure__.get_model_type() == 'svm'):
                # y_prob = model.predict_proba(X_pred)
            elif (self.__configure__.get_model_type() == 'randomforest' or self.__configure__.get_model_type() == 'tree'):
                y_prob = model.predict_proba(X_pred)
                one_pos = 1 - model.classes_[0]

                with open(var_predicted, 'w') as f:
                    for i in range(0, X_pred.shape[0]):
                        f.write('%s\t' % line_ids[i])
                        f.write('%s\t' % varnames[i])
                        f.write('%.4f' % y_prob[i][one_pos])
                        f.write('\n')

        else:
            print(feature_num)
            dnn_model = DNN(self.__configure__)
            y_prob = dnn_model.predict(X_pred, feature_num, 2, True)
            print(y_prob)
            with open(var_predicted, 'w') as f:
                for i in range(0, X_pred.shape[0]):
                    f.write('%s\t' % line_ids[i])
                    f.write('%s\t' % varnames[i])
                    f.write('%.4f' % y_prob[i][1])
                    f.write('\n')

    def run_predict_vars(self, str_encoder, kmeans_model, unique_words):

        print('Predicting var for {}...'.format(self.__configure__.get_bug_id()))

        encoded_var, feature_num = self.encode_var(str_encoder, kmeans_model, unique_words)
        # get the predicted varnames
        self.predict_vars(encoded_var, feature_num)


    def encode_var(self, str_encoder, kmeans_model, unique_words):
        data_file_path = self.__configure__.get_raw_var_pred_in_file()

        original_data_file_path = self.__configure__.get_raw_var_train_in_file()
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
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    elif j == 1:
                        tmp = str_encoder['func'][str(dataset[i, start + j])]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    elif j == 2:
                        tmp = str_encoder['var'][str(dataset[i, start + j]).lower()]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    #elif j == 5:
                        #feature = np.append(feature, int(dataset[i, 3 + j]))
                    elif j == 3:
                        tmp = x_encoders[j].transform([str(dataset[i, start + j])])[0]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    else:
                        feature = np.append(feature, int(dataset[i, start + j]))
                except Exception as e:
                    if j == 2:
                        # feature.append(len(var_encoder))
                        X_0 = np.mat(np.zeros((1, 27 * 27 + 1)))
                        X_0[0] = Cluster.var_to_vec(str(dataset[i, start + j]).lower())
                        pred = kmeans_model['var'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis = 0)
                    elif j == 0:
                        X_0 = np.mat(np.zeros((1, len(unique_words['file']))))
                        X_0[0] = Cluster.name_to_vec(str(dataset[i, start + j]), unique_words['file'])
                        pred = kmeans_model['file'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis = 0)
                    elif j == 1:
                        X_0 = np.mat(np.zeros((1, len(unique_words['func']))))
                        X_0[0] = Cluster.name_to_vec(str(dataset[i, start + j]), unique_words['func'])
                        pred = kmeans_model['func'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis = 0)
                    else:
                        feature = np.concatenate((feature, np.zeros(one_hot_encoder[j].n_values_)), axis = 0)

            new_feature_num = len(feature)
            feature = np.append(feature, 0)
            encoded_feature.append(feature)

        return (pd.DataFrame(encoded_feature), new_feature_num)