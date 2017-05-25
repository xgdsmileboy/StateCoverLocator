from training import *
from Utils.config import *
import pandas as pd


class XGVar(object):

    def __init__(self,  configure):
        """
        @type configure: Configure
        @param configure: Configure
        """
        self.__configure__ = configure;

    def train_var(self, var_encoder):
        print('Training var model for {}_{}...'.\
              format(self.__configure__.get_project_name(), self.__configure__.get_bug_id()))

        # feature_num = # cols - 1(only one target)
        feature_num = 8
        train = Train(self.__configure__)
        train.train(feature_num, 'binary:logistic', var_encoder)


    def predict_vars(self, encoded_var, feature_num):
        # encoded_oracle_var =  oracle[0:-4]+ '.var_encoded.csv'
        var_predicted = self.__configure__.get_var_pred_out_file()

        raw_var_path = self.__configure__.get_raw_var_pred_in_file()
        raw_var = pd.read_csv(raw_var_path, sep='\t', header=0)
        raw_var_values = raw_var.values

        varnames = list()
        if_ids = list()
        for r in range(0, encoded_var.shape[0]):
            if_ids.append(raw_var_values[r, 0])
            varnames.append(raw_var_values[r, 5])

        # load the model from file
        var_model_path = self.__configure__.get_var_model_file()
        if not os.path.exists(var_model_path):
            print('Model file does not exist!')
            os._exit(0)
        with open(var_model_path, 'r') as f:
            model = pk.load(f)
            print('Model loaded from {}'.format(var_model_path))
            print(model)

        encoded_rows_array = np.array(encoded_var)
        # print(encoded_rows_array.shape)
        X_pred = encoded_rows_array[:, 0:feature_num]
        X_pred = X_pred.astype(float)

        y_pred = encoded_rows_array[:, feature_num]

        y_pred = y_pred.astype(float)

        M_pred = xgb.DMatrix(X_pred, label=y_pred)
        y_prob = model.predict(M_pred)


        if os.path.exists(var_predicted):
            os.remove(var_predicted)

        with open(var_predicted, 'w') as f:
            for i in range(0, X_pred.shape[0]):
                f.write('%s\t' % if_ids[i])
                f.write('%s\t' % varnames[i])
                f.write('%.4f' % y_prob[i])
                f.write('\n')


    def run_predict_vars(self, var_encoder, feature_num):

        print('Predicting var for {}...'.format(self.__configure__.get_bug_id()))

        encoded_var = self.encode_var(var_encoder, feature_num)
        # get the predicted varnames
        self.predict_vars(encoded_var, feature_num)


    def encode_var(self, var_encoder, feature_num):
        data_file_path = self.__configure__.get_raw_var_pred_in_file()

        original_data_file_path = self.__configure__.get_raw_var_train_in_file()
        # load data from a csv file
        original_data = pd.read_csv(original_data_file_path, sep='\t', header=0, encoding='utf-8')
        original_dataset = original_data.values
        print('Dataset size: {}'.format(original_dataset.shape))
        # split data into X and y
        # 3 to 11: 8
        X = original_dataset[:, 3:3 + feature_num]
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
                if j == 2:
                    feature.append(var_encoder[str(dataset[i, 3 + j]).lower()])
                else:
                    feature.append(x_encoders[j].transform([str(dataset[i, 3 + j])])[0])
            feature.append('0')
            encoded_feature.append(feature)

        return pd.DataFrame(encoded_feature)