from XGBoost_expr.main import *
from XGBoost_var.main import *
from clustering.cluster import *
from Utils.config import *
from classifier.classifier import *

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
        10,
        sys.argv[3]
    )

    if config.get_model_type() == "classifier":
        cluster = Cluster(config)
        str_encoder, column = cluster.cluster_classifier_string()
        feature_num = column - 3
        classifier = Classifier(config)
        classifier.train_classifier(feature_num)
    else:
        cluster = Cluster(config)
        str_encoder, var_column, expr_column  = cluster.cluster_string()

        xgvar = XGVar(config)
        var_feature_num = var_column - 3
        xgvar.train_var(str_encoder, var_feature_num, False)

        xgexpr = XGExpr(config)
        expr_feature_num = expr_column - 3
        xgexpr.train_expr(str_encoder, expr_feature_num, False)
