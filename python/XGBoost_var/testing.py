import pickle as pk
import pandas as pd
from matplotlib import pyplot
from xgboost import plot_importance
import xgboost as xgb
import os
import datetime
from sklearn.metrics import accuracy_score, precision_recall_fscore_support, precision_score

### predict the true pos examples, and see whether they can be classified correctly
def predict(data_file_path, model_file_path, result_file_path, output_path, summary_file, feature_num, x_encoders, y_encoder):
    start_time = datetime.datetime.now()
    data_file_name = data_file_path.split('/')[-1][0:-4]

    model_file = model_file_path + data_file_name + '_model.pkl'
    result_file = result_file_path + data_file_name +'_results.csv'
    encoded_data_file = data_file_path[0:-4] + '_encoded.csv'

    ## make the output dirs
    project_bugid = data_file_path.split('/')[-1][0:-8]
    project = project_bugid.split('_')[0]
    output_dir = output_path + project + '/var'
    print(output_dir)
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # load the testing data from file
    testing_file = data_file_path[0:-4]+'_test.csv'
    if not os.path.exists(testing_file):
        print('Testing set file does not exist!')
        os._exit(0)
    # load encoded training data
    testing_data = pd.read_csv(testing_file, sep=',', header=None)
    testing_data_values = testing_data.values
    print('Testing set size: {}'.format(testing_data_values.shape))
    row_indices = testing_data_values[:, 0]
    y_test = testing_data_values[:, 1]
    X_test = testing_data_values[:, 2:2+feature_num]

    M_test = xgb.DMatrix(X_test, label=y_test)

    # load the model from file
    if not os.path.exists(model_file):
        print('Model file does not exist!')
        os._exit(0)
    with open(model_file, 'r') as f:
        model = pk.load(f)
        print('Model loaded from {}'.format(model_file))
    print(model)

    if os.path.exists(result_file):
        print('Result file already exists and be removed.')
        os.remove(result_file)

    # predicting the classes
    y_prob = model.predict(M_test)
    y_pred = list()
    # append the probabilities into the result_file
    with open(result_file, 'a+') as f:
        for i in range(0, y_prob.shape[0]):
            f.write('row %s,'%row_indices[i])
            f.write('%s,'%y_test[i])
            f.write('1=%.3f,'%y_prob[i])
            f.write('0=%.3f'%(1-y_prob[i]))
            f.write('\n')
            if y_prob[i] > 0.5:
                y_pred.append(1)
            else:
                y_pred.append(0)
    # If normalize == True, return the correctly classified samples (float), else it returns the number of correctly classified samples (int).
    accuracy = accuracy_score(y_test, y_pred) # average on all classes
    print('Accuracy: %.3f%%' % (accuracy*100.0))
    precision = precision_score(y_test, y_pred, average=None)
    ## print the average precision
    # print('Precision: %.3f%%' % (precision * 100.0))
    ## print the precisions for each classes(here we have 2 for binary)
    print('Precision: {}'.format(precision))
    # cerror = sum(int(y_pred[i]) != y_test[i] for i in range(len(y_test))) / float(len(y_test))

    # precision, recall, thresholds = precision_recall_curve(y_test,y_prob)
    # print(precision)
    # print(recall)
    # print(thresholds)
    p, r, f1, label_nums = precision_recall_fscore_support(y_test, y_pred)
    print('Precision: {}, Recall: {}, F1_score: {}, Label_nums: {}'.format(p,r,f1,label_nums))
    ### plot the feature importance
    ## with plt
    # pyplot.bar(range(len(model.feature_importances_)), model.feature_importances_)
    # pyplot.show()


    ## sum up the results in the summary_file
    with open(summary_file, 'a+') as f:
        f.write('{},{},{},{}\n'.format(accuracy, p, r, f1))


    ### or with xg func
    # plot feature importance
    plot_importance(model)
    pyplot.show()

    end_time = datetime.datetime.now()
    run_time = end_time - start_time
    print('Predicting finished, time cost : {}'.format(run_time))
