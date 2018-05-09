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
    str_encoder, kmeans_model, unique_words = cluster.get_cluster()

    xgvar = XGVar(config)
    xgvar.run_predict_vars(str_encoder, kmeans_model, unique_words)

    xgexpr = XGExpr(config)
    xgexpr.run_gen_exprs(str_encoder, kmeans_model, unique_words)

    join_prob(config)

    if config.get_model_type() == 'randomforest':
        if os.path.exists(config.get_expr_model_file()):
            os.remove(config.get_expr_model_file())
        if os.path.exists(config.get_var_model_file()):
            os.remove(config.get_var_model_file())