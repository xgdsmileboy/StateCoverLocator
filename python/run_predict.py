from Utils.join_prob import *
from XGBoost_expr.main import *
from XGBoost_var.main import *
from clustering.cluster import *
from Utils.config import *
import os
import sys

if __name__ == '__main__':
    if len(sys.argv) != 4:
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

    cluster = Cluster(config)
    str_encoder, kmeans_model = cluster.get_cluster()

    xgvar = XGVar(config)
    xgvar.run_predict_vars(str_encoder, kmeans_model)

    xgexpr = XGExpr(config)
    xgexpr.run_gen_exprs(str_encoder, kmeans_model)

    join_prob(config)
