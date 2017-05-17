from Utils.join_prob import *
from XGBoost_expr.main import *
from XGBoost_var.main import *
from XGBoost_expr.gen_exprs import *
from Utils.predict_vars import *
from XGBoost_expr.encode_expr import *
from XGBoost_var.encode_var import *
import os

if __name__ == '__main__':
    params ={
        'project':'math',
        'bugid':'3',
        'type': 'expr',
        'expr_frequency': 1,
        'model_path': 'model/',
        'input_path':'input/',
        'output_path':'output/',
        'gen_expr_top': 10
    }
    # run_expr(params)
    # run_var(params)
    encode_var(params)
    encode_expr(params)
    run_predict_vars(params)
    run_gen_exprs(params)
    join_prob(params)
