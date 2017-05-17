import pickle as pk
import pandas as pd
import os
import xgboost as xgb
import numpy as np
import heapq as hq
from preprocessing import *
## get the encoded lines with varnames and return the predicted exprs

def gen_exprs(project_bugid, expr_model_path, output_path, raw_expr_path, encoded_raw_expr, top, y_encoder):
    # encoded_oracle_expr = oracle[0:-4]+'.expr_encoded.csv'
    expr_predicted = output_path + project_bugid + '.expr_pred.csv'
    # raw_expr_formatted = '../input/chart_expr/chart_1.expr.csv'

    # oracle_data = pd.read_csv(oracle, sep='\t', header=None)
    # oracle_data_values = oracle_data.values
    raw_expr = pd.read_csv(raw_expr_path, sep='\t', header=0)
    raw_expr_values = raw_expr.values
    # raw_expr_formatted = pd.read_csv(raw_expr_formatted, sep='\t', header=None)
    # raw_expr_formatted_values = raw_expr_formatted.values

    encoded_expr = pd.read_csv(encoded_raw_expr, sep=',', header=None)
    encoded_expr_values = encoded_expr.values

    # get the if_id and the varname as the locators to match
    # locators = oracle_data_values[:, 0:2]
    # locators = np.concatenate((oracle_data_values[:,0:1], oracle_data_values[:,5:6]), axis=1)
    # row_num = list()
    # raw_locators = np.concatenate((raw_expr_values[:,0:1], raw_expr_values[:,5:6]), axis=1)
    # for l in locators:
    #     for i in range(0, raw_locators.shape[0]):
    #         rl = raw_locators[i]
    #         if rl[0]==l[0] and rl[1]==l[1]:
    #             row_num.append(i)

    ## get the classes from the raw expr
    # Y = raw_expr_values[:, 7]
    # classes = np.unique(Y)
    ## use only the varnames to match
    # row_num = list()
    # locators = oracle_data_values[:, 5]
    # raw_locators = raw_expr_values[:, 3]
    # count = -1
    # for l in locators:
    #     for i in range(0, raw_locators.shape[0]):
    #         if l==raw_locators[i]:
    #             row_num.append(i)

    # print('Matched rows:{}'.format(len(row_num)))

    # if not os.path.exists(encoded_oracle_expr):
    #     with open(encoded_oracle_expr, 'a+') as f:
    #         for r in row_num:
    #             row_encoded = encoded_expr_values[r,:]
    #             for x in row_encoded:
    #                 f.write('{},'.format(x))
    #             f.write('\n')

    ## get the encoded expr features
    encoded_rows = list()
    varnames = list()
    if_ids = list()
    raw_exprs = list()
    for r in range(0, encoded_expr_values.shape[0]):
        row_encoded = encoded_expr_values[r, :]
        encoded_rows.append(row_encoded)
        if_ids.append(raw_expr_values[r, 0])
        varnames.append(raw_expr_values[r, 5])
        raw_exprs.append(raw_expr_values[r, :])
    print(encoded_rows)
    ## load the model
    if not os.path.exists(expr_model_path):
        print('Model file does not exist!')
        os._exit(0)
    with open(expr_model_path, 'r') as f:
        model = pk.load(f)
        print('Model loaded from {}'.format(expr_model_path))
        print(model)

    ## do predicting
    encoded_rows_array = np.array(encoded_rows)
    # print(encoded_rows_array.shape)
    X_pred = encoded_rows_array[:, 0:6]
    # print(X_pred)
    y_pred = encoded_rows_array[:, 6]
    M_pred = xgb.DMatrix(X_pred)
    y_prob = model.predict(M_pred)

    # raw_exprs_array = np.array(raw_exprs)
    # X_raw = raw_exprs_array[:, 0:6]
    # Y_raw = raw_exprs_array[:, 6]

    print(y_prob.shape)

    ## save the results
    if os.path.exists(expr_predicted):
        os.remove(expr_predicted)
    with open(expr_predicted, 'a+') as f:
        for i in range(0, X_pred.shape[0]):
            f.write('%s\t' % if_ids[i])
            f.write('%s\t' % varnames[i])
            # f.write('%s' % y_prob[i])
            line = y_prob[i]
            # the predicted indices, ordered with the classes in alphabet order
            # so classes = np.unique(Y) required to decode
            alts = hq.nlargest(top, range(len(line)), line.__getitem__)
            # print(alts)
            for j in range(top):
                if j != 0:
                    f.write('\t\t')
                # tag_pred = classes[alts[j]]
                tag_pred = alts[j]
                original = y_encoder.inverse_transform(tag_pred)
                f.write('{}'.format(original))# predicate
                f.write('\t%f'%line[alts[j]])
                f.write('\n')

def run_gen_exprs(params):
    project = params['project']
    bugid = params['bugid']
    project_bugid = project+'_'+bugid

    print('Predicting expr for {}...'.format(project_bugid))

    expr_model_path = params['model_path']+project_bugid+'.expr_model.pkl'
    output_path = params['output_path']+project+'/'

    # oracle = params['input_path']+project+'/pred/'+project_bugid+'.csv'
    # raw_expr_path = params['input_path']+project+'/expr/'+project_bugid+'.expr_frequent_raw.csv'
    raw_expr_path = params['input_path']+project+'/expr/'+project_bugid+'.expr.csv'

    top=params['gen_expr_top']
    # encoded_raw_expr = params['input_path']+project+'/expr/'+project_bugid+'.expr_frequent.csv'
    encoded_raw_expr = params['input_path']+project+'/expr/'+project_bugid+'.expr_encoded.csv'

    # get the encoder
    original_data_file_path = params['input_path']+params['project']+'/expr/'+params['project']+'_'+params['bugid']+'.expr.back.csv'
    classes, x_encoders, y_encoder = preprocess(original_data_file_path, 6, params['expr_frequency'])

    # get the predicted exprs
    gen_exprs(project_bugid, expr_model_path, output_path, raw_expr_path, encoded_raw_expr, top, y_encoder)

if __name__ == '__main__':
    params={
        'project':'time',
        'bugid': '15'
    }
    # run_gen_exprs(params)