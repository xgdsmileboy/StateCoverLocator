import time
import logging

from XGBoost_expr.ExprWithPCA import ExprWithPCA
from XGBoost_expr.main import *
from XGBoost_var.VarWithPCA import VarWithPCA
from XGBoost_var.main import *
from clustering.cluster import *
from Utils.config import *

import os
import sys

if __name__ == '__main__':
    if len(sys.argv) != 4 or len(sys.argv) != 5:
        print("Wrong argument number!")
        sys.exit(1)
    config = Configure(
        sys.argv[1],
        sys.argv[2],
        1,
        'model/',
        'input/',
        'output/',
        10,
        sys.argv[3]
    )
    if sys.argv[3] == "l2s" or sys.argv[3] == "l2sdnn":
        use_dnn = False
        if sys.argv[3] == 'l2sdnn':
            use_dnn = True
        evaluate = False
        if len(sys.argv) == 5 and sys.argv[4] == "evaluate":
            evaluate = True
        config.set_expr_freq(freq=0)
        logging.info(
            '######################## BEGIN TRAINING FOR ' + config.prognm_and_id + ' ########################')
        all_time_start = time.time()

        # train position 0 var
        var_trainer = VarWithPCA(config)
        var_trainer.train(is_v0=True, use_dnn, evaluate)

        # train all position var
        var_trainer.train(is_v0=False, use_dnn, evaluate)

        # train expr
        expr_trainer = ExprWithPCA(config)
        expr_trainer.train(use_dnn, evaluate)

        time_end = time.time()
        logging.info('######################## TOTAL TRAINING TIME ' + str(
            float(time_end - all_time_start) / 60) + ' m ########################\n')
    else:
        cluster = Cluster(config)
        str_encoder, var_column, expr_column  = cluster.cluster_string()

        xgvar = XGVar(config)
        var_feature_num = var_column - 4
        xgvar.train_var(str_encoder, var_feature_num)

        xgexpr = XGExpr(config)
        expr_feature_num = expr_column - 4
        xgexpr.train_expr(str_encoder, expr_feature_num)
