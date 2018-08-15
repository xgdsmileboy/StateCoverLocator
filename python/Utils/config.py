import os
import sys

class Configure(object):

    def __init__(self, project_name, bug_id, expr_freq, model_path, input_path, output_path, gen_expr_top, model_type):
        self.__project_name__ = project_name
        self.__bug_id__ = bug_id
        self._bug_name_id = project_name + "_" + bug_id
        self.__expr_freq__ = expr_freq
        # self.__input_path__ = input_path + model_type
        # self.__output_path__ = output_path + model_type
        self.__gen_expr_top__ = gen_expr_top
        self.__model_base_path__ = model_path + model_type + '/' + project_name + '/' + self._bug_name_id
        self.__input_base_path__ = input_path + model_type + '/' + project_name + '/' + self._bug_name_id
        self.__output_base_path__ = output_path + model_type + '/' + project_name + '/' + self._bug_name_id
        self.__model_type__ = model_type

    def get_project_name(self):
        return self.__project_name__

    def get_bug_id(self):
        return self.__bug_id__

    def get_expr_frequency(self):
        return self.__expr_freq__

    def get_gen_expr_top(self):
        return self.__gen_expr_top__

    def get_model_type(self):
        return self.__model_type__

    def get_raw_classifier_train_in_file(self):
        # python/input/classifier/math/math_1/predicate/math_1.csv
        return self.__input_base_path__ + '/predicate/' + self._bug_name_id + '.csv'

    def get_raw_var_train_in_file(self):
        # python/input/dnn/math/math_1/var/math_1.var.csv
        return self.__input_base_path__ + '/var/' + self._bug_name_id + '.var.csv'

    def get_raw_expr_train_in_file(self):
        # python/input/dnn/math/math_1/expr/math_1.expr.csv
        return self.__input_base_path__ + '/expr/' + self._bug_name_id + '.expr.csv'

    def get_var_nn_training_file(self):
        # python/input/dnn/math/math_1/var/math_1.var.nn.csv
        return self.__input_base_path__ + '/var/' + self._bug_name_id + '.var.nn.csv'

    def get_expr_nn_training_file(self):
        # python/input/dnn/math/math_1/expr/math_1.expr.nn.csv
        return self.__input_base_path__ + '/expr/' + self._bug_name_id + '.expr.nn.csv'

    def get_var_cluster_file(self):
        # python/input/dnn/math/math_1/cluster/math_1.var_cluster.csv
        path = self.__input_base_path__ + '/cluster/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self._bug_name_id + '.var_cluster.csv'

    def get_func_cluster_file(self):
        # python/input/dnn/math/math_1/cluster/math_1.func_cluster.csv
        path = self.__input_base_path__ + '/cluster/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self._bug_name_id + '.func_cluster.csv'

    def get_file_cluster_file(self):
        # python/input/dnn/math/math_1/cluster/math_1.file_cluster.csv
        path = self.__input_base_path__ + '/cluster/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self._bug_name_id + '.file_cluster.csv'

    def get_predicate_cluster_file(self):
        # python/input/classifier/cluster/math/math_1/cluster/math_1.pred_cluster.csv
        path = self.__input_base_path__ + '/cluster/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self._bug_name_id + '.pred_cluster.csv'

    def get_classifier_model_file(self):
        # python/input/classifier/math/math_1/math_1.classifier_model.pkl
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.classifier_model.pkl'

    def get_var_model_file(self):
        # python/model/dnn/math/math_1/math_1.var_model.pkl
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.var_model.pkl'

    def get_expr_model_file(self):
        # python/model/dnn/math/math_1/math_1.expr_model.pkl
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.expr_model.pkl'

    def get_var_cluster_model_file(self):
        # python/model/dnn/math/math_1/math_1.var_cluster_model.pkl
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.var_cluster_model.pkl'

    def get_func_cluster_model_file(self):
        # python/model/dnn/math/math_1/math_1.func_cluster_model.pkl
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.func_cluster_model.pkl'

    def get_func_cluster_info_file(self):
        # python/model/dnn/math/math_1/math_1.func_cluster_model.info
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.func_cluster_model.info'

    def get_file_cluster_model_file(self):
        # python/model/dnn/math/math_1/math_1.file_cluster_model.pkl
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.file_cluster_model.pkl'

    def get_file_cluster_info_file(self):
        # python/model/dnn/math/math_1/math_1.file_cluster_model.info
        path = self.__model_base_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + '/' + self._bug_name_id + '.file_cluster_model.info'

    def get_var_nn_model_dir(self):
        # python/model/dnn/math/math_1/var/
        path = self.__model_base_path__ + '/var/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path

    def get_expr_nn_model_dir(self):
        # python/model/dnn/math/math_1/expr/
        path = self.__model_base_path__ + '/expr/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path

    def get_raw_var_pred_in_file(self):
        # python/input/dnn/math/math_1/pred/math_1.var.csv
        var_path = self.__input_base_path__ + '/pred/' + self._bug_name_id + '.var.csv'
        return var_path

    def get_raw_expr_pred_in_file(self):
        # python/input/dnn/math/math_1/pred/math_1.expr.csv
        expr_path = self.__input_base_path__ + '/pred/' + self._bug_name_id + '.expr.csv'
        return expr_path

    def get_var_pred_out_file(self):
        # python/output/dnn/math/math_1/math_1.var.csv
        if not os.path.exists(self.__output_base_path__):
            os.makedirs(self.__output_base_path__)
        var_path = self.__output_base_path__ + '/' + self._bug_name_id + '.var_pred.csv'
        return var_path

    def get_expr_pred_out_file(self):
        # python/output/dnn/math/math_1/math_1.expr.csv
        if not os.path.exists(self.__output_base_path__):
            os.makedirs(self.__output_base_path__)
        var_path = self.__output_base_path__ + '/' + self._bug_name_id+ '.expr_pred.csv'
        return var_path

    def get_joint_predict_file(self):
        # python/output/dnn/math/math_1/math_1.joint.csv
        if not os.path.exists(self.__output_base_path__):
            os.makedirs(self.__output_base_path__)
        return self.__output_base_path__ + '/' + self._bug_name_id + '.joint.csv'

class Dumper(object):

    def __init__(self, configure):
        self.__configure__ = configure

    def dump(self):
        print self.__configure__.get_expr_train_in_file()
        print self.__configure__.get_expr_pred_in_file()
        print self.__configure__.get_expr_pred_out_file()
        print self.__configure__.get_var_train_in_file()
        print self.__configure__.get_var_pred_in_file()
        print self.__configure__.get_var_pred_out_file()
        print self.__configure__.get_cluster_file()

if __name__ == '__main__':
    conf = Configure(
        'math',
        '1',
        'expr',
        5,
        'model/',
        'input/',
        'output/',
        10
    )
    dumper = Dumper(conf)
    dumper.dump()