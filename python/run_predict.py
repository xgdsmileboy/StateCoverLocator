import sys

from XGBoost_expr.ExprWithPCA import *
from XGBoost_var.VarWithPCA import *


if __name__ == '__main__':

    config = Configure(
        sys.argv[1],
        sys.argv[2],
        1,
        'model/',
        'input/',
        'output/',
        sys.maxint     #TOP: 10, 100, sys.maxint
    )

    misson_type = sys.argv[3]
    assert misson_type == 'expr' or misson_type == 'var' or misson_type == 'v0'

    start = time.time()
    if misson_type == 'expr':
        if len(sys.argv) == 5:
            config.__gen_expr_top__ = int(sys.argv[4])

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