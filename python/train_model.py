from XGBoost_expr.main import *
from XGBoost_var.main import *

import os
import sys

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Wrong argument number!")
        sys.exit(1)
    params ={
        'project': sys.argv[1],
        'bugid': sys.argv[2],
        'type': 'expr',
        'expr_frequency': 1,
        'model_path': 'model/',
        'input_path':'input/',
        'output_path':'output/',
        'gen_expr_top': 10
    }
    run_expr(params)
    run_var(params)
