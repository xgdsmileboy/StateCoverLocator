from preprocessing import *
from training import *
from testing import *
from gen_exprs import  *
import datetime

def run_expr(params):
    print('Training expr model for {}_{}...'.format(params['project'], params['bugid']))
    ## construct the path strings with params
    data_file_path = params['input_path']+params['project']+'/expr/'+params['project']+'_'+params['bugid']+'.expr.csv'
    frequent_file_path = params['input_path']+params['project']+'/expr/'+params['project']+'_'+params['bugid']+'.expr_frequent.csv'

    model_saved_path = params['model_path']
    result_path = params['output_path']+params['project']+'/expr/'


    # store vital info in the summary file
    summary_file = params['output_path'] + 'summary_expr.csv'
    if not os.path.exists(summary_file):
        # write the header only once
        with open(summary_file, 'a+') as f:
            f.write('time, project_bugid, #classes, #samples, frequency, #frequent_classes, #frequent_samples, #test_samples, #top1, p(top1), #top5, p(top5), #top10, p(top10)\n')

    with open(summary_file, 'a+') as f:
        f.write('%s,' % datetime.datetime.now())
        f.write('%s,' % (params['project'] + '_' + params['bugid']))

    # feature_num = # cols - 1(only one target)
    feature_num = 6
    frequency = params['expr_frequency']
    # preprocess, encode-
    classes, x_encoders, y_encoder = preprocess(summary_file, data_file_path, feature_num, frequency)
    class_num = len(classes)

    # train the model
    train(frequent_file_path, model_saved_path, feature_num, 'multi:softprob', class_num)
    # predict
    predict(data_file_path, model_saved_path, result_path, params['output_path'], summary_file, feature_num, classes, x_encoders, y_encoder)
    # run_gen_exprs(params, y_encoder)

if __name__ == '__main__':

    params ={
        'project':'math',
        'bugid':'82',
        'type': 'expr',
        'expr_frequency': 1,
        'model_path': '../model/',
        'input_path':'../input/',
        'output_path':'../output/',
        'gen_expr_top': 10
    }
    run_expr(params)
