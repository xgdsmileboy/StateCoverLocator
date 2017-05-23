from Utils.join_prob import *
from XGBoost_expr.main import *
from XGBoost_var.main import *
from XGBoost_expr.gen_exprs import *
from Utils.predict_vars import *
from XGBoost_expr.encode_expr import *
from XGBoost_var.encode_var import *
from clustering.cluster import *
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
    # run_expr(params)
    # run_var(params)
    var_encoder = get_var_encoder(params)
    encode_var(params, var_encoder)
    encode_expr(params, var_encoder)
    run_predict_vars(params, var_encoder)
    run_gen_exprs(params, var_encoder)
    join_prob(params)
