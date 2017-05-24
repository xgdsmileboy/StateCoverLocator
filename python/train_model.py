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
        5,
        'model/',
        'input/',
        'output/',
        10
    )
    var_encoder = cluster_var(config)
    run_var(config, var_encoder)
    run_expr(config, var_encoder)
