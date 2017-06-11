from training import *
import datetime
from Utils.config import *
import heapq as hq
from clustering.cluster import *

class XGExpr(object):

    def __init__(self, configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure

    def train_expr(self, str_encoder, feature_num):
        print('Training expr model for {}_{}...'.\
              format(self.__configure__.get_project_name(), self.__configure__.get_bug_id()))
        ## construct the path strings with params
        trainExpr = TrainExpr(self.__configure__)
        # train the model
        trainExpr.train(feature_num, 'multi:softprob', str_encoder)


    def run_gen_exprs(self, str_encoder, kmeans_model):
        print('Predicting expr for {}...'.format(self.__configure__.get_bug_id()))

        expr_predicted = self.__configure__.get_expr_pred_out_file()
        expr_model_path = self.__configure__.get_expr_model_file()

        top = self.__configure__.get_gen_expr_top()

        dataset, encoded_x, encoded_y, x_encoders, y_encoder = self.encode_expr(str_encoder, kmeans_model)

        ## load the model
        if not os.path.exists(expr_model_path):
            print('Model file does not exist!')
            os._exit(0)
        with open(expr_model_path, 'r') as f:
            model = pk.load(f)
            print('Model loaded from {}'.format(expr_model_path))
            print(model)

        X_pred = encoded_x
        print(X_pred)

        M_pred = xgb.DMatrix(X_pred)
        y_prob = model.predict(M_pred)

        print(y_prob.shape)

        ## save the results
        if os.path.exists(expr_predicted):
            os.remove(expr_predicted)
        with open(expr_predicted, 'w') as f:
            for i in range(0, X_pred.shape[0]):
                f.write('%s\t' % dataset[i, 0])
                f.write('%s\t' % dataset[i, 5])
                # f.write('%s' % y_prob[i])
                line = y_prob[i]
                # the predicted indices, ordered with the classes in alphabet order
                # so classes = np.unique(Y) required to decode

                #fix bug of max
                if top > line.shape[0]:
                    top = line.shape[0]

                alts = hq.nlargest(top, range(len(line)), line.__getitem__)
                # print(alts)
                for j in range(top):
                    if j != 0:
                        f.write('\t\t')
                    # tag_pred = classes[alts[j]]
                    tag_pred = alts[j]
                    original = y_encoder.inverse_transform(tag_pred)
                    f.write('{}'.format(original))  # predicate
                    f.write('\t%f' % line[alts[j]])
                    f.write('\n')


    def encode_expr(self, str_encoder, kmeans_model):

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
        for i in range(0, X.shape[1]):
            x_encoders[i] = LabelEncoder()
            x_encoders[i].fit_transform(X[:, i])

        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
        dataset = data.values


        encoded_x = list()
        for i in range(0, dataset.shape[0]):
            feature = list()
            for j in range(0, feature_num):
                try:
                    if j == 0:
                        feature.append(str_encoder['file'][str(dataset[i, 3 + j])])
                    elif j == 1:
                        feature.append(str_encoder['func'][str(dataset[i, 3 + j])])
                    elif j == 2:
                        feature.append(str_encoder['var'][str(dataset[i, 3 + j]).lower()])
                    elif j == 5 or j == 6: #dis0 AND presasnum
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
            encoded_x.append(feature)

        encoded_x = pd.DataFrame(encoded_x)
        encoded_y = pd.DataFrame(encoded_y)
        return (dataset, encoded_x, encoded_y, x_encoders, y_encoder)