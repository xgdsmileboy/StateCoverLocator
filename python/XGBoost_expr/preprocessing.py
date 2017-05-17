import numpy as np
import os
import datetime
import collections
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split

def preprocess(data_file_path, feature_num, frequency):
    start_time = datetime.datetime.now()
    training_file = data_file_path[0:-4]+'_train.csv'
    validating_file = data_file_path[0:-4]+'_valid.csv'
    # also save the encoded data for later use
    encoded_data_file = data_file_path[0:-4]+'_encoded.csv'
    frequent_encoded_data_file = data_file_path[0:-4] + '_frequent.csv'

    # load data from a csv file
    data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
    dataset = data.values
    print('Raw data size: {}'.format(dataset.shape))
    # split data into X and y
    X = dataset[:, 3:3+feature_num]
    X = X.astype(str)
    Y = dataset[:, 3+feature_num]
    # Y = dataset[:, target_col:target_col+1]
    # print(Y)
    # encoding string as integers
    encoded_x = None
    x_encoders = [None] * feature_num
    for i in range(0, X.shape[1]):
        x_encoders[i] = LabelEncoder()
        feature = x_encoders[i].fit_transform(X[:,i])
        feature = feature.reshape(X.shape[0],1)
        if encoded_x is None:
            encoded_x = feature
        else:
            encoded_x = np.concatenate((encoded_x, feature), axis=1)

    y_encoder = LabelEncoder()
    encoded_y = y_encoder.fit_transform(Y)
    ### write the encoded raw data into file
    if not os.path.exists(encoded_data_file):
        with open(encoded_data_file, 'a+') as f:
            for i in range(0, X.shape[0]):
                for x in encoded_x[i]:
                    f.write("%s," % x)
                f.write("%s" % encoded_y[i])
                f.write('\n')


    classes = np.unique(Y)
    class_num = len(classes)
    print('All class num: %d' % class_num)
    # count the examples and drop the infrequent ones
    counter = collections.Counter(Y)
    frequent_y = list()
    for c in counter.items():
        if c[1]> frequency :
            frequent_y.append(c[0])
    frequent_class_num = len(frequent_y)
    print('Frequent class(>{}) num: {}'.format(frequency, frequent_class_num))
    frequent_indices = list()
    for i in range(0, Y.shape[0]):
        if Y[i] in frequent_y:
            frequent_indices.append(i)
    frequent_samples = len(frequent_indices)
    print('Frequent rows num: {}'.format(frequent_samples))

    # if not os.path.exists(frequent_encoded_data_file):
    #     with open(frequent_encoded_data_file, 'a+') as f:
    #         for i in frequent_indices:
    #             for x in encoded_x[i]:
    #                 f.write('%s,'%(x))
    #             f.write('%s'%(encoded_y[i]))
    #             f.write('\n')

    # frequent_data=pd.read_csv(frequent_encoded_data_file, sep=',', header=None, encoding='utf-8')
    # frequent_values=frequent_data.values
    # frequent_x = frequent_values[:, 0:6]
    # frequent_y = frequent_values[:, 6]
    # split the data into training and validing set
    # X_train, X_valid, y_train, y_valid = train_test_split(frequent_x, frequent_y, test_size=0.1, random_state=7)
    # print('Training set size: {}'.format(y_train.shape))
    # print('Validation set size: {}'.format(y_valid.shape))
    # write the encoded data into 2 files
    # if not os.path.exists(training_file):
    #     with open(training_file, 'a+') as f:
    #         for i in range(0, X_train.shape[0]):
    #             for x in X_train[i]:
    #                 f.write('%s,'% x)
    #             f.write('%s' % y_train[i])
    #             f.write('\n')
    # if not os.path.exists(validating_file):
    #     with open(validating_file, 'a+') as f:
    #         for i in range(0, X_valid.shape[0]):
    #             for x in X_valid[i]:
    #                 f.write('%s,'% x)
    #             f.write('%s' % y_valid[i])
    #             f.write('\n')

    # with open(summary_file, 'a+') as f:
    #     f.write('%s,' % class_num)
    #     f.write('{},'.format(X.shape[0]))
    #     f.write('{},'.format(frequency))
    #     f.write('{},'.format(frequent_class_num))
    #     f.write('{},'.format(frequent_samples))

    end_time = datetime.datetime.now()
    run_time = end_time - start_time
    print('Preprossing finished, time cost : {}'.format(run_time))
    # print('Data splited and saved in {} and {}'.format(training_file, validating_file))
    return (classes, x_encoders, y_encoder)