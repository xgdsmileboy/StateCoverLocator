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
        'expr',
        1,
        'model/',
        'input/',
        'output/',
        10
    )

    cluster = Cluster(config)
    var_encoder = cluster.get_var_encoder()

    xgvar = XGVar(config)
    xgvar.run_predict_vars(var_encoder, 8)

    xgexpr = XGExpr(config)
    xgexpr.run_gen_exprs(var_encoder, 6)

    join_prob(config)
