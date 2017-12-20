import logging
import os
import sys

class Configure(object):

    def __init__(self, project_name, bug_id, expr_freq, model_path, input_path, output_path, gen_expr_top, model_type):
        self.__project_name__ = project_name
        self.__bug_id__ = bug_id
        self.__expr_freq__ = expr_freq
        self.__model_path__ = model_path
        self.__input_path__ = input_path
        self.__output_path__ = output_path
        self.__gen_expr_top__ = gen_expr_top
        self.__input_base_path__ = input_path + project_name + '/' + project_name + '_' + bug_id
        self.__output_base_path__ = output_path + project_name + '/' + project_name + '_' + bug_id
        self.__model_type__ = model_type

        self.prognm_and_id = project_name + '_' + bug_id

        # init folders
        self.get_expr_pred_out_file()

        logging.basicConfig(level=logging.DEBUG,
                            format='%(asctime)s %(levelname)-8s:\n%(message)s\n',
                            filename=self.__output_base_path__ + '/log.txt',
                            filemode='a')

        ch = logging.StreamHandler(sys.stdout)
        ch.setLevel(logging.INFO)
        logging.getLogger('').addHandler(ch)


    def set_expr_freq(self, freq):
        self.__expr_freq__ = freq

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

    def get_raw_var_train_in_file(self):
        # python/input/math/math_1/var/math_1.var.csv
        return self.__input_base_path__ + '/var/' + self.__project_name__ + '_' + self.__bug_id__ + '.var.csv'

    def get_raw_expr_train_in_file(self):
        # python/input/math/math_1/expr/math_1.expr.csv
        return self.__input_base_path__ + '/expr/' + self.__project_name__ + '_' + self.__bug_id__ + '.expr.csv'

    def get_var_nn_training_file(self):
        # python/input/math/math_1/var/math_1.var.nn.csv
        var_path = self.__input_base_path__ + '/var/' + self.__project_name__ + '_' + self.__bug_id__ + '.var.nn.csv'
        return var_path

    def get_expr_nn_training_file(self):
        # python/input/math/math_1/expr/math_1.expr.nn.csv
        expr_path = self.__input_base_path__ + '/expr/' + self.__project_name__ + '_' + self.__bug_id__ + '.expr.nn.csv'
        return expr_path

    def get_var_cluster_file(self):
        # python/input/math/math_1/cluster/math_1.var_cluster.csv
        path = self.__input_base_path__ + '/cluster/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self.__project_name__ + '_' + self.__bug_id__ + '.var_cluster.csv'

    def get_func_cluster_file(self):
        # python/input/math/math_1/cluster/math_1.func_cluster.csv
        path = self.__input_base_path__ + '/cluster/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self.__project_name__ + '_' + self.__bug_id__ + '.func_cluster.csv'

    def get_file_cluster_file(self):
        # python/input/math/math_1/cluster/math_1.file_cluster.csv
        path = self.__input_base_path__ + '/cluster/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self.__project_name__ + '_' + self.__bug_id__ + '.file_cluster.csv'

    def get_var_model_file(self):
        # python/model/math_1.var_model.pkl
        if not os.path.exists(self.__model_path__):
            os.makedirs(self.__model_path__)
        if self.__model_type__ == 'l2s':
            return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '.var_model.pkl'
        else:
            return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '_' + self.__model_type__ + '.var_model.pkl'

    def get_expr_model_file(self):
        # python/model/math_1.expr_model.pkl
        if not os.path.exists(self.__model_path__):
            os.makedirs(self.__model_path__)
        if self.__model_type__ == 'l2s':
            return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '.expr_model.pkl'
        else:
            return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '_' + self.__model_type__ + '.expr_model.pkl'

    def get_var_cluster_model_file(self):
        # python/model/math_1.var_cluster_model.pkl
        if not os.path.exists(self.__model_path__):
            os.makedirs(self.__model_path__)
        return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '.var_cluster_model.pkl'

    def get_func_cluster_model_file(self):
        # python/model/math_1.func_cluster_model.pkl
        if not os.path.exists(self.__model_path__):
            os.makedirs(self.__model_path__)
        return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '.func_cluster_model.pkl'

    def get_file_cluster_model_file(self):
        # python/model/math_1.file_cluster_model.pkl
        if not os.path.exists(self.__model_path__):
            os.makedirs(self.__model_path__)
        return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '.file_cluster_model.pkl'

    def get_var_nn_model_dir(self):
        # python/model/math_1/var/
        m_path = self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '/var/'
        if not os.path.exists(m_path):
            os.makedirs(m_path)
        return m_path

    def get_expr_nn_model_dir(self):
        # python/model/math_1/expr/
        m_path = self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '/expr/'
        if not os.path.exists(m_path):
            os.makedirs(m_path)
        return m_path

    def get_raw_var_pred_in_file(self):
        # python/input/math/math_1/pred/math_1.var.csv
        var_path = self.__input_base_path__ + '/pred/' + self.__project_name__ + '_' + self.__bug_id__ + '.var.csv'
        return var_path

    def get_raw_expr_pred_in_file(self):
        # python/input/math/math_1/pred/math_1.expr.csv
        expr_path = self.__input_base_path__ + '/pred/' + self.__project_name__ + '_' + self.__bug_id__ + '.expr.csv'
        return expr_path

    def get_var_pred_out_file(self):
        # python/output/math/math_1/math_1.var.csv
        if not os.path.exists(self.__output_base_path__):
            os.makedirs(self.__output_base_path__)
        var_path = self.__output_base_path__ + '/' + self.__project_name__ + '_' + self.__bug_id__ + '.var_pred.csv'
        return var_path

    def get_expr_pred_out_file(self):
        # python/output/math/math_1/math_1.expr.csv
        if not os.path.exists(self.__output_base_path__):
            os.makedirs(self.__output_base_path__)
        var_path = self.__output_base_path__ + '/' + self.__project_name__ + '_' + self.__bug_id__ + '.expr_pred.csv'
        return var_path

    def get_joint_predict_file(self):
        # python/output/math/math_1/math_1.joint.csv
        if not os.path.exists(self.__output_base_path__):
            os.makedirs(self.__output_base_path__)
        return self.__output_base_path__ + '/' + self.__project_name__ + '_' + self.__bug_id__ + '.joint.csv'

    ######################################
    # add for l2s model
    ######################################
    def get_raw_v0_train_in_file(self):
        return self.__input_base_path__ + '/var/' + self.__project_name__ + '_' + self.__bug_id__ + '.v0.csv'

    def get_model_path_prefix(self):
        if not os.path.exists(self.__model_path__):
            os.makedirs(self.__model_path__)
        return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__

    def get_v0_model_file(self):
        # python/model/math_1.var_model.pkl
        if not os.path.exists(self.__model_path__):
            os.makedirs(self.__model_path__)
        return self.__model_path__ + self.__project_name__ + '_' + self.__bug_id__ + '.v0_model.pkl'

    def get_raw_v0_pred_in_file(self):
        # python/input/math/math_1/pred/math_1.v0.csv
        var_path = self.__input_base_path__ + '/pred/' + self.__project_name__ + '_' + self.__bug_id__ + '.v0.csv'
        return var_path

    def get_v0_pred_out_file(self):
        # python/output/math/math_1/math_1.var.csv
        if not os.path.exists(self.__output_base_path__):
            os.makedirs(self.__output_base_path__)
        var_path = self.__output_base_path__ + '/' + self.__project_name__ + '_' + self.__bug_id__ + '.v0_pred.csv'
        return var_path

    # added for pca
    def get_pca_model(self, mission_type, tag):
        path = self.__model_path__ + 'pca/'  # + mission_type + '/'
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self.__project_name__ + '_' + self.__bug_id__ + '_' + mission_type + ".pca." + tag + ".pkl"

    def get_deleted_zero_columm(self, mission_type):
        path = self.__model_path__ + "pca/"
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self.__project_name__ + '_' + self.__bug_id__ + '_' + mission_type + ".del.pkl"

    def get_pca_csv(self, mission_type, tag):
        path = self.__model_path__ + "pca/"
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self.__project_name__ + '_' + self.__bug_id__ + '_' + mission_type + "." + tag + ".csv"

    def get_all_label_encoders(self, mission_type):
        path = self.__model_path__
        if not os.path.exists(path):
            os.makedirs(path)
        return path + self.__project_name__ + '_' + self.__bug_id__ + '.' + mission_type + '.lb.pkl'

    @staticmethod
    def get_expr_tags_for_pca():
        # expr_tags = ['tdname','methodname','allloc','allfld',
        #              'befsyn','bdsyn','afsyn',
        #              'befcd','befpre']
        expr_tags = Configure.get_v0_tags_for_pca()

        return expr_tags

    @staticmethod
    def get_v0_tags_for_pca():
        # var_tags = Configure.get_expr_tags_for_pca()
        # var_tags.extend(['pred', 'varname', 'lastpre'])

        var_tags = ['varname', 'vartype', 'tdname', 'methodname', 'allloc', 'allloctp', 'allfld', 'allfldtp',
                    'befsyn', 'bdsyn', 'afsyn',
                    # 'varpre1st', 'varpre2nd', 'tppre1st', 'tppre2nd', 'tppre3rd',
                    'pstmt0', 'pstmt1', 'nstmt0', 'nstmt1',
                    'befcd', 'befpre', 'lastpre']

        return var_tags

    @staticmethod
    def get_var_tags_for_pca():
        # var_tags = Configure.get_expr_tags_for_pca()
        # var_tags.extend(['pred', 'varname', 'lastpre'])
        var_tags = ['varname', 'vartype', 'tdname', 'methodname', 'allloc', 'allloctp', 'allfld', 'allfldtp',
                    'befsyn', 'bdsyn', 'afsyn',
                    # 'varpre1st', 'varpre2nd', 'tppre1st', 'tppre2nd', 'tppre3rd',
                    'pstmt0', 'pstmt1', 'nstmt0', 'nstmt1',
                    'befcd', 'befpre', 'lastpre', 'pred']

        return var_tags


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
