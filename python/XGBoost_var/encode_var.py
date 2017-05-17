from preprocessing import *
import numpy as np
import os
import pandas as pd
from sklearn.preprocessing import LabelEncoder

def encode_var(params):
    data_file_path = params['input_path']+ params['project'] + '/var/' + params['project'] + '_'+params['bugid']+'.var.csv'
    encode_file_path = params['input_path']+ params['project'] + '/var/' + params['project'] + '_'+params['bugid']+'.var_encoded.csv'
    result_path = params['output_path']+params['project']+'/var/'

    # feature_num = # cols - 1(only one target)
    feature_num = 8
    # preprocess, encode
    # preprocess(summary_file, data_file_path, feature_num)
    
    original_data_file_path = params['input_path']+ params['project'] + '/var/' + params['project'] + '_'+params['bugid']+'.var.back.csv'
    # load data from a csv file
    original_data = pd.read_csv(original_data_file_path, sep='\t', header=0, encoding='utf-8')
    original_dataset = original_data.values
    print('Dataset size: {}'.format(original_dataset.shape))
    # split data into X and y
    # 3 to 11: 8
    X = original_dataset[:, 3:3+feature_num]
    X = X.astype(str)

    # encoding string as integers
    encoded_x = None
    x_encoders = [None] * feature_num
    for i in range(0, X.shape[1]):
        x_encoders[i] = LabelEncoder()
        x_encoders[i].fit(X[:,i])

    data = pd.read_csv(data_file_path, sep='\t', header = 0, encoding = 'utf-8')
    dataset = data.values

    if (os.path.exists(encode_file_path)):
        os.remove(encode_file_path)
    with open(encode_file_path, 'w+') as f:
        for i in range(0, dataset.shape[0]):
            for j in range(0, feature_num):
                try:
                    feature = x_encoders[j].transform([dataset[i, 3 + j]])
                    f.write("%s," % feature[0])
                except Exception as e:
                    print(e)
                    f.write("%d," % x_encoders[j].classes_.shape[0])
            f.write("0\n") # meaningless
                
