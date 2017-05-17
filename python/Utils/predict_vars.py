import pickle as pk
import pandas as pd
import os
import xgboost as xgb
import numpy as np

### predict vars with highest proba in if, and generate corrsponding exors

## read the model and the predict data, get a list of ranked vars
def predict_vars(project_bugid, var_model_path, output_path, raw_var_path, encoded_raw_var):
    # encoded_oracle_var =  oracle[0:-4]+ '.var_encoded.csv'
    var_predicted = output_path + project_bugid + '.var_pred.csv'

    # load unencoded training data
    # oracle_data = pd.read_csv(oracle, sep='\t', header=None)
    # oracle_values = oracle_data.values
    raw_var = pd.read_csv(raw_var_path, sep='\t', header=0)
    raw_var_values = raw_var.values
    # print(oracle_values.shape)

    ## convert the data to encoded ones(by matching in the original files)
    # match with if_ids and varnames
    # locators = oracle_values[:, 0:2]
    # locators = np.concatenate((oracle_values[:, 0:1], oracle_values[:, 5:6]), axis=1)
    # row_num = list()

    # raw_locators = np.concatenate((raw_var_values[:,0:1], raw_var_values[:,5:6]),axis=1)
    # for l in locators:
    #     for i in range(0, raw_locators.shape[0]):
    #         rl = raw_locators[i]
    #         if rl[0]==l[0] and rl[1]==l[1]:
    #             row_num.append(i)

    # print('Matched rows:{}'.format(row_num))
    # read the encoded data and write them into the predicting file
    encoded_var = pd.read_csv(encoded_raw_var, sep=',', header=None)
    encoded_var_values = encoded_var.values
    # if not os.path.exists(encoded_oracle_var):
    #     with open(encoded_oracle_var, 'a+') as f:
    #         for r in row_num:
    #             row_encoded = encoded_var_values[r,:]
    #             for x in row_encoded:
    #                 f.write('%s,'%x)
    #             f.write('\n')

    encoded_rows = list()
    varnames = list()
    if_ids = list()
    for r in range(0, encoded_var_values.shape[0]):
        row_encoded = encoded_var_values[r, :]
        encoded_rows.append(row_encoded)
        if_ids.append(raw_var_values[r, 0])
        varnames.append(raw_var_values[r, 5])
    # load the model from file
    if not os.path.exists(var_model_path):
        print('Model file does not exist!')
        os._exit(0)
    with open(var_model_path, 'r') as f:
        model = pk.load(f)
        print('Model loaded from {}'.format(var_model_path))
        print(model)

    encoded_rows_array = np.array(encoded_rows)
    # print(encoded_rows_array.shape)
    X_pred = encoded_rows_array[:, 0:8]
    y_pred = encoded_rows_array[:, 8]
    M_pred = xgb.DMatrix(X_pred, label=y_pred)
    y_prob = model.predict(M_pred)

    if os.path.exists(var_predicted):
        os.remove(var_predicted)

    with open(var_predicted, 'a+') as f:
        for i in range(0, X_pred.shape[0]):
            f.write('%s\t' % if_ids[i])
            f.write('%s\t' % varnames[i])
            f.write('%.4f' % y_prob[i])
            f.write('\n')

            ## diable sorting now
            # var_prob = dict()
            # for i in range(0, X_pred.shape[0]):
            #     x = X_pred[i] # one row of x
            #     varname = x[2] # get the varname
            #     var_prob[varname]=y_prob[i]
            #
            # print(var_prob)
            # for i in range(0, encoded_rows_array.shape[0]):
            #     row = encoded_rows_array[i]
            #     varnames.append(row[2])


def run_predict_vars(params):
    project = params['project']
    bugid = params['bugid']
    project_bugid = project+'_'+bugid

    print('Predicting var for {}...'.format(project_bugid))

    var_model_path = params['model_path']+project_bugid+'.var_model.pkl'
    output_path = params['output_path']+project+'/'

    # oracle = params['input_path']+project+'/pred/'+project_bugid+'.csv'
    raw_var_path = params['input_path']+project+'/var/'+project_bugid+ '.var.csv'

    encoded_raw_var = params['input_path']+project+'/var/'+project_bugid+'.var_encoded.csv'
    # get the predicted varnames
    predict_vars(project_bugid, var_model_path, output_path, raw_var_path, encoded_raw_var)

if __name__ == '__main__':
    params={
        'project':'time',
        'bugid': '15'
    }
    run_predict_vars(params)