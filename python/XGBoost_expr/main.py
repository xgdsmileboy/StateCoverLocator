from training import *
import datetime
from Utils.config import *
import heapq as hq
from clustering.cluster import *
from DNN.dnn import *

import tensorflow as tf

class XGExpr(object):

    def __init__(self, configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure

    def train_expr(self, str_encoder, feature_num, evaluate):
        print('Training expr model for {}_{}...'.\
              format(self.__configure__.get_project_name(), self.__configure__.get_bug_id()))
        ## construct the path strings with params
        trainExpr = TrainExpr(self.__configure__)
        # train the model
        trainExpr.train(feature_num, 'multi:softprob', str_encoder, evaluate)


    def run_gen_exprs(self, str_encoder, kmeans_model, unique_words):
        print('Predicting expr for {}...'.format(self.__configure__.get_bug_id()))

        expr_predicted = self.__configure__.get_expr_pred_out_file()
        expr_model_path = self.__configure__.get_expr_model_file()
        if os.path.exists(expr_predicted):
            os.remove(expr_predicted)

        top = self.__configure__.get_gen_expr_top()

        dataset, encoded_x, encoded_y, x_encoders, y_encoder, feature_num = self.encode_expr(str_encoder, kmeans_model, unique_words)
        X_pred = encoded_x
        y_prob = list()
        classes = list()
        if (self.__configure__.get_model_type() != 'dnn'):
            ## load the model
            if not os.path.exists(expr_model_path):
                print('Model file does not exist!')
                os._exit(0)
            with open(expr_model_path, 'r') as f:
                model = pk.load(f)
                print('Model loaded from {}'.format(expr_model_path))
                print(model)

            if (self.__configure__.get_model_type() == 'xgboost'):
                M_pred = xgb.DMatrix(X_pred)
                y_prob = model.predict(M_pred)
            # elif (self.__configure__.get_model_type() == 'svm'):
                # y_prob = model.predict_log_proba(X_pred)
            elif (self.__configure__.get_model_type() == 'randomforest' or self.__configure__.get_model_type() == 'tree'):
                y_prob = model.predict_proba(X_pred)
                classes = model.classes_
        else:
            dnn_model = DNN(self.__configure__)
            y_prob = dnn_model.predict(np.array(X_pred.values), feature_num, y_encoder.classes_.shape[0], False)

        ## save the result
        with open(expr_predicted, 'w') as f:
            for i in range(0, X_pred.shape[0]):
                key = dataset[i, 3] + "::" + str(dataset[i, 1]) + "::" + dataset[i, 5] 
                f.write('%s\t' % key)
                f.write('%s\t' % dataset[i, 5])
                # f.write('%s' % y_prob[i])
                line = y_prob[i]
                # the predicted indices, ordered with the classes in alphabet order
                # so classes = np.unique(Y) required to decode

                #fix bug of max
                if top > line.shape[0]:
                    top = line.shape[0]

                alts = hq.nlargest(top, range(len(line)), line.__getitem__)
                for j in range(top):
                    #if line[alts[j]] == 0:
                    #    break
                    if j != 0:
                        f.write('\t\t')
                    # tag_pred = classes[alts[j]]
                    if len(classes) > 0:
                        tag_pred = classes[alts[j]]
                    else:
                        tag_pred = alts[j]
                    original = y_encoder.inverse_transform(tag_pred)
                    f.write('{}'.format(original))  # predicate
                    f.write('\t%f' % line[alts[j]])
                    f.write('\n')

    def encode_expr(self, str_encoder, kmeans_model, unique_words):

        data_file_path = self.__configure__.get_raw_expr_pred_in_file()
        original_data_file_path = self.__configure__.get_raw_expr_train_in_file()

        # load data from a csv file
        original_data = pd.read_csv(original_data_file_path, sep='\t', header=0, encoding='utf-8')
        original_dataset = original_data.values
        print('Raw data size: {}'.format(original_dataset.shape))
        # split data into X and y
        X = original_dataset[:, 3:-1]
        X = X.astype(str)
        Y = original_dataset[:, -1]

        y_encoder = LabelEncoder()
        encoded_y = y_encoder.fit_transform(Y)

        feature_num = original_data.shape[1] - 4
        # encoding string as integers
        x_encoders = [None] * feature_num
        one_hot_encoder = [None] * feature_num
        for i in range(0, X.shape[1]):
            x_encoders[i] = LabelEncoder()
            feature = x_encoders[i].fit_transform(X[:, i])
            feature = feature.reshape(X.shape[0], 1)
            if i <= 4 or i == 9 or i == 10 or i == 11:
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


        encoded_x = list()
        new_feature_num = feature_num
        for i in range(0, dataset.shape[0]):
            feature = list()
            for j in range(0, feature_num):
                try:
                    if j == 0:
                        tmp = str_encoder['file'][str(dataset[i, 3 + j])]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    elif j == 1:
                        tmp = str_encoder['func'][str(dataset[i, 3 + j])]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    elif j == 2:
                        tmp = str_encoder['var'][str(dataset[i, 3 + j]).lower()]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    #elif j == 5:
                        #feature = np.append(feature, int(dataset[i, 3 + j]))
                    elif j == 3 or j == 4 or j == 9 or j == 10 or j == 11:
                        tmp = x_encoders[j].transform([str(dataset[i, 3 + j])])[0]
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[tmp]])[0]), axis = 0)
                    else:
                        feature = np.append(feature, int(dataset[i, 3 + j]))
                except Exception as e:
                    print(e)
                    if j == 2:
                        # feature.append(len(var_encoder))
                        X_0 = np.mat(np.zeros((1, 27 * 27 + 1)))
                        X_0[0] = Cluster.var_to_vec(str(dataset[i, 3 + j]).lower())
                        pred = kmeans_model['var'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis = 0)
                    elif j == 0:
                        X_0 = np.mat(np.zeros((1, len(unique_words['file']))))
                        X_0[0] = Cluster.name_to_vec(str(dataset[i, 3 + j]), unique_words['file'])
                        pred = kmeans_model['file'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis = 0)
                    elif j == 1:
                        X_0 = np.mat(np.zeros((1, len(unique_words['func']))))
                        X_0[0] = Cluster.name_to_vec(str(dataset[i, 3 + j]), unique_words['func'])
                        pred = kmeans_model['func'].predict(X_0)
                        feature = np.concatenate((feature, one_hot_encoder[j].transform([[pred[0]]])[0]), axis = 0)
                    else:
                        feature = np.concatenate((feature, np.zeros(one_hot_encoder[j].n_values_)), axis = 0)
            new_feature_num = len(feature)
            encoded_x.append(feature)

        encoded_x = pd.DataFrame(encoded_x)
        encoded_y = pd.DataFrame(encoded_y)
        return (dataset, encoded_x, encoded_y, x_encoders, y_encoder, new_feature_num)