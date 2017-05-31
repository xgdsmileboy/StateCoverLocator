from Utils.join_prob import *
from XGBoost_expr.main import *
from XGBoost_var.main import *
from clustering.cluster import *
from Utils.config import *
import os
import sys

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Wrong argument number!")
        sys.exit(1)
    config = Configure(
        sys.argv[1],
        sys.argv[2],
        1,
        'model/',
        'input/',
        'output/',
        200     #TOP: 10, 100, sys.maxint
    )

    cluster = Cluster(config)
    var_encoder, kmeans = cluster.get_var_cluster()

    xgvar = XGVar(config)
    xgvar.run_predict_vars(var_encoder, kmeans)

    xgexpr = XGExpr(config)
    xgexpr.run_gen_exprs(var_encoder, kmeans)

    join_prob(config)
