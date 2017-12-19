import sys

from XGBoost_expr.ExprWithPCA import *
from XGBoost_var.VarWithPCA import *
import time


if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Wrong argument number!")
        sys.exit(1)

    config = Configure(
        sys.argv[1],
        sys.argv[2],
        0,
        'model/',
        'input/',
        'output/',
        10
    )

    logging.info('######################## BEGIN TRAINING FOR ' + config.prognm_and_id +' ########################')
    all_time_start = time.time()

    #train position 0 var
    var_trainer = VarWithPCA(config)
    var_trainer.train(is_v0=True)

    # train all position var
    var_trainer.train(is_v0=False)

    # train expr
    expr_trainer = ExprWithPCA(config)
    expr_trainer.train()
    
    time_end = time.time()
    logging.info('######################## TOTAL TRAINING TIME ' + str(float(time_end - all_time_start)/60) + ' m ########################\n')
