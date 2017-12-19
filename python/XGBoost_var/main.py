from training import *
from Utils.config import *
import pandas as pd
from clustering.cluster import *

import tensorflow as tf

class XGVar(object):

    def __init__(self,  configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure;

    def train_var(self, str_encoder, feature_num):
        print('Training var model for {}_{}...'.\
              format(self.__configure__.get_project_name(), self.__configure__.get_bug_id()))

        # feature_num = # cols - 1(only one target)
        train = Train(self.__configure__)
        train.train(feature_num, 'binary:logistic', str_encoder)

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
            line_ids.append(raw_var_values[r, 3] + "::" + str(raw_var_values[r, 1]) + "::" + raw_var_values[r, 5])
            varnames.append(raw_var_values[r, 5])

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
            elif (self.__configure__.get_model_type() == 'randomforest'):
                y_prob = model.predict_proba(X_pred)
                print(y_prob)
                print(model.classes_)
                one_pos = 1 - model.classes_[0]

                with open(var_predicted, 'w') as f:
                    for i in range(0, X_pred.shape[0]):
                        f.write('%s\t' % line_ids[i])
                        f.write('%s\t' % varnames[i])
                        f.write('%.4f' % y_prob[i][one_pos])
                        f.write('\n')
        else:
            feature_columns = [tf.contrib.layers.real_valued_column('', dimension = feature_num)]
            classifier = tf.contrib.learn.DNNClassifier(feature_columns = feature_columns,
                                              hidden_units = [10, 20, 10],
                                              n_classes = 2,
                                              model_dir = self.__configure__.get_var_nn_model_dir())
            y_prob = list(classifier.predict_proba(x = X_pred))
            with open(var_predicted, 'w') as f:
                for i in range(0, X_pred.shape[0]):
                    f.write('%s\t' % line_ids[i])
                    f.write('%s\t' % varnames[i])
                    f.write('%.4f' % y_prob[i][1])
                    f.write('\n')

    def run_predict_vars(self, str_encoder, kmeans_model):

        print('Predicting var for {}...'.format(self.__configure__.get_bug_id()))

        encoded_var, feature_num = self.encode_var(str_encoder, kmeans_model)
        # get the predicted varnames
        self.predict_vars(encoded_var, feature_num)


    def encode_var(self, str_encoder, kmeans_model):
        data_file_path = self.__configure__.get_raw_var_pred_in_file()

        original_data_file_path = self.__configure__.get_raw_var_train_in_file()
        # load data from a csv file
        original_data = pd.read_csv(original_data_file_path, sep='\t', header=0, encoding='utf-8')
        original_dataset = original_data.values
        print('Dataset size: {}'.format(original_dataset.shape))
        feature_num = original_data.shape[1] - 4
        # split data into X and y
        # 3 to 11: 8
        X = original_dataset[:, 3:-1]
        X = X.astype(str)

        # encoding string as integers
        x_encoders = [None] * feature_num
        for i in range(0, X.shape[1]):
            x_encoders[i] = LabelEncoder()
            x_encoders[i].fit(X[:, i])

        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
        dataset = data.values

        encoded_feature = list()
        for i in range(0, dataset.shape[0]):
            feature = list()
            for j in range(0, feature_num):
                # TODO : if the key does not exit in the encoder, how to transform
                try:
                    if j == 0:
                        feature.append(str_encoder['file'][str(dataset[i, 3 + j])])
                    elif j == 1:
                        feature.append(str_encoder['func'][str(dataset[i, 3 + j])])
                    elif j == 2:
                        feature.append(str_encoder['var'][str(dataset[i, 3 + j]).lower()])
                    elif j == 5:
                        feature.append(int(dataset[i, 3 + j]))
                    else:
                        feature.append(x_encoders[j].transform([str(dataset[i, 3 + j])])[0])
                except Exception as e:
                    print(e)
                    if j == 2:
                        # feature.append(len(var_encoder))
                        X_0 = np.mat(np.zeros((1, 27 * 27 + 1)))
                        X_0[0] = Cluster.var_to_vec(str(dataset[i, 3 + j]).lower())
                        pred = kmeans_model['var'].predict(X_0)
                        feature.append(pred[0])
                    else:
                        feature.append(x_encoders[j].classes_.shape[0])

            feature.append(0)
            encoded_feature.append(feature)

        return (pd.DataFrame(encoded_feature), feature_num)