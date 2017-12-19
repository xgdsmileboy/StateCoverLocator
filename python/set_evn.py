import sys
import os
import commands
import ConfigParser

pred_config = ConfigParser.ConfigParser()
pred_config.read("env.conf")



home_path = os.environ['HOME']

predictor_path = home_path + pred_config.get('PATH', 'predictor_path')
all_csv_path = home_path + pred_config.get('PATH', 'all_csv_path')
project_root = home_path + pred_config.get('PATH', 'project_root')

def execute_cmd(cmd, exitonerr=True):
    status, msg = commands.getstatusoutput(cmd)
    if status != 0:
        sys.stderr.write('WRONG CMD: ' + cmd + '\n')
        sys.stderr.write(msg + '\n\n')
        sys.stderr.write(os.getcwd() + '\n\n')
        if exitonerr:
            exit(-1)
    sys.stdout.write(msg)
    return

def copy_csv(pro_name, id):
    # mkdir output
    output_path = predictor_path + 'input/' + pro_name + '/' + pro_name + '_' + str(id)
    if not os.path.exists(output_path):
        os.makedirs(output_path)

    # mkdir input/[var, expr, pred]
    for mission in ['/var/', '/expr/', '/pred/']:
        pro_dir = predictor_path + 'input/' + pro_name + '/' + pro_name + '_' + str(id) + mission
        if not os.path.exists(pro_dir):
            os.makedirs(pro_dir)

        #copy from tmp/res/ to var/ and expr/
        cp_cmd = 'cp '
        if mission == '/var/':
            cp_cmd += all_csv_path + '/' + pro_name + '_' + str(id) + '.var.csv ' + pro_dir
            execute_cmd(cp_cmd)
        elif mission == '/expr/':
            cp_cmd_0 = cp_cmd + all_csv_path + '/' + pro_name + '_' + str(id) + '.expr.csv ' + pro_dir
            execute_cmd(cp_cmd_0)
            cp_cmd_1 = cp_cmd + all_csv_path + '/' + pro_name + '_' + str(id) + '.allpred.csv ' + pro_dir
            execute_cmd(cp_cmd_1)

    return


def train_model(pro_name, id, overwrite=False):
    var_mod = predictor_path + 'model/' + pro_name + '_' + str(id) + '.var_model.pkl'
    expr_mod = predictor_path + 'model/' + pro_name + '_' + str(id) + '.expr_model.pkl'

    if not overwrite and os.path.exists(var_mod) and os.path.exists(expr_mod):
        print '#### BOTH MODEL EXIST'
        return
    os.chdir(predictor_path)
    train_cmd = 'python train_model.py ' + pro_name + ' ' + str(id)
    print '\n#### TRAINING MODEL CMD: ' + train_cmd
    # execute_cmd(train_cmd)
    print '\n#### TRAINING FINISH'
    return


def clear_out(pro_name, id):
    os.chdir(predictor_path)
    res_root = predictor_path + 'output/' + pro_name + '/' + pro_name + '_' + str(id) + '/'
    rm_cmd = "rm -f " + res_root + "*.csv"
    print "#### CLEAR OUTPUT DIR: " + rm_cmd
    execute_cmd(rm_cmd)


def predict_expr(pro_name, id, tested_bug, bug_no):
    var_mod = predictor_path + 'model/' + pro_name + '_' + str(id) + '.var_model.pkl'
    expr_mod = predictor_path + 'model/' + pro_name + '_' + str(id) + '.expr_model.pkl'
    if not os.path.exists(var_mod) or not os.path.exists(expr_mod):
        sys.stderr.write('NO MODEL OF ' + pro_name + '_' + str(id) + '\n')
    os.chdir(predictor_path)

    res_root = predictor_path + 'output/' + pro_name + '/' + pro_name + '_' + str(id) + '/'

    pred_cmd = 'python run_predict.py ' + pro_name + ' ' + str(id)
    execute_cmd(pred_cmd)

    for post_fix in ['.var_pred.csv', '.expr_pred.csv', '.joint.csv']:
        cp_cmd = 'cp -f ' + res_root + pro_name + '_' + str(id) + post_fix + ' ' + res_root + pro_name + '_' + str(tested_bug) + '_' + str(bug_no) + post_fix
        execute_cmd(cp_cmd)
        #rm_cmd = 'rm ' + res_root + pro_name + '_' + str(id) + post_fix
        #execute_cmd(rm_cmd, exitonerr=False)
    return





if __name__ == '__main__':
    if len(sys.argv) != 6:
        print("\nWrong argument number\n!")
        sys.exit(1)

    mode, pro_name, id, tested_bug, bug_no = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5]
    #print mode, pro_name, id, tested_bug, bug_no

    os.chdir(predictor_path)
    if mode == 'train':
        print "#### TRAINiNG %s_%s ####" %(pro_name, id)
        copy_csv(pro_name, id)
        train_model(pro_name, id)
        # clear_out(pro_name, id)
    elif mode == 'pred':
        print "#### PRED (%s_%s) BY MODEL OF (%s_%s) ####" % (pro_name, tested_bug, pro_name, id)
        predict_expr(pro_name, id, tested_bug, bug_no)








