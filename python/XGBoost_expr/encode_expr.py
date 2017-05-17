from preprocessing import *

def encode_expr(params):
    ## construct the path strings with params
    data_file_path = params['input_path']+params['project']+'/expr/'+params['project']+'_'+params['bugid']+'.expr.csv'
    encode_file_path = params['input_path']+params['project']+'/expr/'+params['project']+'_'+params['bugid']+'.expr_encoded.csv'

    model_saved_path = params['model_path']
    result_path = params['output_path']+params['project']+'/expr/'

    # feature_num = # cols - 1(only one target)
    feature_num = 6
    frequency = params['expr_frequency']
    # preprocess, encode-
    # preprocess(data_file_path, feature_num, frequency)
    original_data_file_path = params['input_path']+params['project']+'/expr/'+params['project']+'_'+params['bugid']+'.expr.back.csv'

    # load data from a csv file
    original_data = pd.read_csv(original_data_file_path, sep='\t', header=0, encoding='utf-8')
    original_dataset = original_data.values
    print('Raw data size: {}'.format(original_dataset.shape))
    # split data into X and y
    X = original_dataset[:, 3:3+feature_num]
    X = X.astype(str)
    
    # encoding string as integers
    encoded_x = None
    x_encoders = [None] * feature_num
    for i in range(0, X.shape[1]):
        x_encoders[i] = LabelEncoder()
        feature = x_encoders[i].fit_transform(X[:,i])

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