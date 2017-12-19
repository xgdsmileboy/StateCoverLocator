import pickle
from Utils.config import Configure
from xgboost import *
from matplotlib import pyplot
import sys

sys.path.append("/usr/bin/dot")

if __name__ == '__main__':
    config = Configure(
        'math',
        '37',
        1,
        'model/',
        'input/',
        'output/',
        0
    )

    model_file = config.get_expr_model_file()
    # model_file = config.get_v0_model_file()
    #model_file = config.get_var_model_file()
    model = pickle.load(open(model_file, 'r'))

    plot_tree(model)
    pyplot.show()

    importance = model.get_score(importance_type='weight')

    res = sorted(importance.items(), lambda x, y: cmp(x[1], y[1]), reverse=True)
    print "======WEIGHT=============="
    for i in res:
        print i

    importance = model.get_score(importance_type='gain')
    res = sorted(importance.items(), lambda x, y: cmp(x[1], y[1]), reverse=True)
    print "======GAIN=============="
    for i in res:
        print i

    importance = model.get_score(importance_type='cover')
    res = sorted(importance.items(), lambda x, y: cmp(x[1], y[1]), reverse=True)
    print "======COVER=============="
    for i in res:
        print i


    # plot_importance(model)
    # pyplot.show()


