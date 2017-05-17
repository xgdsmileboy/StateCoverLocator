import numpy as np
import os
import datetime
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split

def preprocess(summary_file, data_file_path, feature_num):
    start_time = datetime.datetime.now()
    training_file = data_file_path[0:-4]+'_train.csv'
    validating_file = data_file_path[0:-4]+'_valid.csv'
    # also save the encoded data for later use
    encoded_data_file = data_file_path[0:-4]+'_encoded.csv'
    # get all the positive examples and test them
    testing_file = data_file_path[0:-4]+'_test.csv'

    # load data from a csv file
    data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')
    dataset = data.values
    print('Dataset size: {}'.format(dataset.shape))
    # split data into X and y
    # 3 to 11: 8
    X = dataset[:, 3:3+feature_num]
    X = X.astype(str)
    Y = dataset[:, 3+feature_num]

    Y = Y.astype(str)
    classes = np.unique(Y)
    class_num = len(classes)
    print('Class number: %d' % class_num)

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
    # encoded targets = original targets
    y_encoder = LabelEncoder()
    encoded_y = y_encoder.fit_transform(Y)

    # split the data into training and validating set
    X_train, X_valid, y_train, y_valid = train_test_split(encoded_x, encoded_y, test_size=0.2, random_state=7)
    print('Training set size: {}'.format(X_train.shape))
    print('Validating set size: {}'.format(X_valid.shape))

    # write the encoded data into 2 files
    if not os.path.exists(training_file):
        with open(training_file, 'a+') as f:
            for i in range(0, X_train.shape[0]):
                for x in X_train[i]:
                    f.write('%s,'% x)
                f.write('%s' % y_train[i])
                f.write('\n')
    if not os.path.exists(validating_file):
        with open(validating_file, 'a+') as f:
            for i in range(0, y_valid.shape[0]):
                for x in X_valid[i]:
                    f.write('%s,'% x)
                f.write('%s' % y_valid[i])
                f.write('\n')
    if not os.path.exists(encoded_data_file):
        ### write the encoded data into 1 file
        with open(encoded_data_file, 'a+') as f:
            for i in range(0, X.shape[0]):
                for x in encoded_x[i]:
                    f.write("%s," % x)
                f.write("%s" % encoded_y[i])
                f.write('\n')

    ### get all the encoded pos examples and
    # the row indexes in raw file and encoded file are the same haha

    p_num = 0
    n_num = 0
    for i in range(Y.shape[0]):
        if Y[i] == '1':
            p_num+=1
        elif Y[i] == '0':
            n_num+=1
    print('Positive examples : {}'.format(p_num))
    print('Negative examples : {}'.format(n_num))

    if not os.path.exists(testing_file):
        p_num = 0
        n_num = 0
        with open(testing_file, 'a+') as f:
            for i in range(X.shape[0]):
                if Y[i] == '1':
                    # print(Y[i])
                    p_num+=1
                    if p_num > 5000:
                        break
                    else:
                        f.write('%s,'%i)# row num in the original file : 0
                        f.write('%s,'%Y[i])# target : 1
                        for x in encoded_x[i]:
                            f.write('%s,'%x)
                        f.write('\n')
            for i in range(X.shape[0]):
                if Y[i] == '0':
                    n_num+=1
                    if n_num > 5000:
                        break
                    else:
                        f.write('%s,'%i)
                        f.write('%s,'%Y[i])
                        for x in encoded_x[i]:
                            f.write('%s,'%x)
                        f.write('\n')


    with open(summary_file, 'a+') as f:
        f.write('%s,' % class_num)
        f.write('{},'.format(X.shape[0]))
        f.write('{},'.format('5000+5000-'))


    end_time = datetime.datetime.now()
    run_time = end_time - start_time
    print('Preprossing finished, time cost : {}'.format(run_time))
    print('Data splited and saved in {} and {}'.format(training_file, validating_file))
    return (x_encoders, y_encoder)