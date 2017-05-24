import os
import sys

class Configure:

    input_base_path = ''

    def __init__(self, project_name, bug_id, type, expr_freq, model_path, input_path, output_path, gen_expr_top):
        self.project_name = project_name
        self.bug_id = bug_id
        self.type = type
        self.expr_freq = expr_freq
        self.model_path = model_path
        self.input_path = input_path
        self.output_path = output_path
        self.gen_expr_top = gen_expr_top
        self.input_base_path = input_path + project_name + '/' + project_name + '_' + bug_id

    def get_var_train_in_file(self):
        # python/input/math/math_1/var/math_1.var.csv
        train_file_path = self.input_base_path + '/var/' + self.project_name + '_' + self.bug_id + '.var.csv'
        return train_file_path

    def get_expr_train_in_file(self):
        # python/input/math/math_1/var/math_1.expr.csv
        train_file_path = self.input_base_path + '/expr/' + self.project_name + '_' + self.bug_id + '.expr.csv'
        return train_file_path

    def get_cluster_file(self):
        # python/input/math/math_1/cluster/math_1.cluster.csv
        cluster_file_path = self.input_base_path + '/cluster/' + self.project_name + '_' + self.bug_id + '.cluster.csv'
        return cluster_file_path

    def get_var_pred_in_file(self):
        # python/input/math/math_1/pred/math_1.var.csv
        var_path = self.input_base_path + '/pred/' + self.project_name + '_' + self.bug_id + '.var.csv'
        return var_path

    def get_expr_pred_in_file(self):
        # python/input/math/math_1/pred/math_1.expr.csv
        expr_path = self.input_base_path + '/pred/' + self.project_name + '_' + self.bug_id + '.expr.csv'
        return expr_path

    def get_var_pred_out_file(self):
        # python/output/math/var/math_1.var.csv
        var_path = self.output_path + self.project_name + '/var/' + self.project_name + '_' + self.bug_id + '.var.csv'
        return var_path

    def get_expr_pred_out_file(self):
        # python/output/math/expr/math_1.expr.csv
        var_path = self.output_path + self.project_name + '/expr/' + self.project_name + '_' + self.bug_id + '.expr.csv'
        return var_path

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

    print conf.get_expr_train_in_file()
    print conf.get_expr_pred_in_file()
    print conf.get_expr_pred_out_file()
    print conf.get_var_train_in_file()
    print conf.get_var_pred_in_file()
    print conf.get_var_pred_out_file()
    print conf.get_cluster_file()