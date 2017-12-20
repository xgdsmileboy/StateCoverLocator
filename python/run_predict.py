import time

from Utils.join_prob import *
from XGBoost_expr.ExprWithPCA import ExprWithPCA
from XGBoost_expr.main import *
from XGBoost_var.VarWithPCA import VarWithPCA
from XGBoost_var.main import *
from clustering.cluster import *
from Utils.config import *
import os
import sys

if __name__ == '__main__':
    if len(sys.argv) < 4:
        print("Wrong argument number!")
        sys.exit(1)
    config = Configure(
        sys.argv[1],
        sys.argv[2],
        1,
        'model/',
        'input/',
        'output/',
        200,     #TOP: 10, 100, sys.maxint
        sys.argv[3]
    )

    if config.get_model_type() == 'l2s':
        misson_type = sys.argv[4]
        assert misson_type == 'expr' or misson_type == 'var' or misson_type == 'v0'

        start = time.time()
        if misson_type == 'expr':
            if len(sys.argv) == 6:
                config.__gen_expr_top__ = int(sys.argv[5])

            expr_predictor = ExprWithPCA(config)
            expr_predictor.predict()
            end = time.time()
            print 'EXPR PRED TIME: ', (end - start)

        elif misson_type == 'v0':
            var_predictor = VarWithPCA(config)
            var_predictor.predict(True)
            end = time.time()
            print 'V0 PRED TIME: ', (end - start)
        else:
            var_predictor = VarWithPCA(config)
            var_predictor.predict(False)
            end = time.time()
            print 'ALL VAR PRED TIME: ', (end - start)
    else:
        cluster = Cluster(config)
        str_encoder, kmeans_model = cluster.get_cluster()

        xgvar = XGVar(config)
        xgvar.run_predict_vars(str_encoder, kmeans_model)

        xgexpr = XGExpr(config)
        xgexpr.run_gen_exprs(str_encoder, kmeans_model)

        join_prob(config)
