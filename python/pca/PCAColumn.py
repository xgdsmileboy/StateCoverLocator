import numpy as np
import pandas as pd
import pickle as pk
import os
import gc

from Utils.string_utils import *
from Utils.bag_of_character import BagOfCharacter

from sklearn.discriminant_analysis import LinearDiscriminantAnalysis

# from sklearn.decomposition import PCA
# import matplotlib.pyplot as plt
# from mpl_toolkits.mplot3d import Axes3D

from sklearn.preprocessing import LabelEncoder


class PCAColumn(object):

    def __init__(self, configure, mission_type):
        self.__configure__ = configure
        self.__mission__ = mission_type

        self.alphabet_dict = {}

        BAG_NUM = 2

        fname_alph = BagOfCharacter.get_file_name()
        fname_bag = BagOfCharacter(BAG_NUM, fname_alph)
        self.alphabet_dict['tdname'] = fname_bag
        self.alphabet_dict['methodname'] = fname_bag

        var_name_alph = BagOfCharacter.get_var_name()
        varname_bag = BagOfCharacter(BAG_NUM, var_name_alph)
        self.alphabet_dict['varname'] = varname_bag


        all_var_alph = BagOfCharacter.get_all_var()
        all_var_bag = BagOfCharacter(BAG_NUM, all_var_alph)
        self.alphabet_dict['vartype'] = all_var_bag
        self.alphabet_dict['allloc'] = all_var_bag
        self.alphabet_dict['allloctp'] = all_var_bag

        self.alphabet_dict['allfld'] = all_var_bag
        self.alphabet_dict['allfldtp'] = all_var_bag

        syntax_alph = BagOfCharacter.get_syntax_var()
        syn_bag = BagOfCharacter(BAG_NUM, syntax_alph)
        self.alphabet_dict['befsyn'] = syn_bag
        self.alphabet_dict['bdsyn'] = syn_bag
        self.alphabet_dict['afsyn'] = syn_bag

        expr_alph = BagOfCharacter.get_expr()
        expr_bag = BagOfCharacter(BAG_NUM, expr_alph)
        self.alphabet_dict['befcd'] = expr_bag
        self.alphabet_dict['befpre'] = expr_bag
        self.alphabet_dict['pred'] = expr_bag
        self.alphabet_dict['lastpre'] = expr_bag

        self.alphabet_dict['varpre1st'] = expr_bag
        self.alphabet_dict['varpre2nd'] = expr_bag
        self.alphabet_dict['tppre1st'] = expr_bag
        self.alphabet_dict['tppre2nd'] = expr_bag
        self.alphabet_dict['tppre3rd'] = expr_bag


        self.alphabet_dict['pstmt0'] = expr_bag
        self.alphabet_dict['pstmt1'] = expr_bag
        self.alphabet_dict['nstmt0'] = expr_bag
        self.alphabet_dict['nstmt1'] = expr_bag


        self.selected_feature_num = {'tdname': 5, 'methodname': 10, 'varname': 30, 'vartype': 10,
                                    'allloc': 10, 'allloctp':5, 'allfld': 10, 'allfldtp': 5,
                                    'befsyn': 10, 'bdsyn': 10, 'afsyn':10,
                                    'varpre1st': 5, 'varpre2nd': 5, 'tppre1st': 5, 'tppre2nd': 5, 'tppre3rd': 5,
                                    'pstmt0': 30, 'pstmt1': 20, 'nstmt0': 30, 'nstmt1': 20,
                                    'befcd': 20, 'befpre': 20, 'pred': 20, 'lastpre' : 20}

    @staticmethod
    def process_col(column, tag):
        if tag == 'tdname' or  tag == 'methodname':
            column = column.apply(remove_for_fnames)

        elif tag == 'vartype' or tag == 'allloc' or tag == 'allfld' or tag == 'allloctp' or tag == 'allfldtp':
            column = column.apply(remove_for_allvar_and_allfld)    #remove space

        elif tag == 'befsyn' or tag == 'bdsyn' or tag == 'afsyn':
            column = column.apply(remove_for_syntax)

        elif tag == 'pred' or tag == 'befcd' \
                or tag == 'befpre' or tag == 'lastpre' \
                or tag == 'varpre1st' or tag == 'varpre2nd' or tag == 'tppre1st' or tag == 'tppre2nd' or tag == 'tppre3rd' \
                or tag == 'pstmt0' or tag == 'pstmt1' or tag == 'nstmt0' or tag == 'nstmt1':
            column = column.apply(remove_spcace)    #remove space

        elif tag == 'varname':
            column = column.apply(remove_for_names)

        return column


    def get_bag(self, tag):
        return self.alphabet_dict[tag]

    # For prediction
    def load_all_vector(self, data, tags):
        del_list_file = self.__configure__.get_deleted_zero_columm(self.__mission__)
        with open(del_list_file, 'r') as f:
            all_deleted_list_dict = pk.load(f)

        all_bag_encoders_dict = {}

        for tag in tags:
            col = data[tag]
            col = PCAColumn.process_col(col, tag)
            bag_of_character = self.get_bag(tag)
            cur_del_list = all_deleted_list_dict[tag]
            all_bag_encoders_dict[tag] = self.get_bag_bit_by_given_del_list(tag, col, bag_of_character, cur_del_list)

        return all_bag_encoders_dict

    def all_vector(self, data, tags):
        """
        @:param data: DataFrame
        @:param tags: list 
        """
        all_bag_encoders_dict = {}
        all_deleted_list_dict = {}

        for tag in tags:
            col = data[tag]
            bag_of_character = self.get_bag(tag)
            # do name2vector by column
            all_bag_encoders_dict[tag], all_deleted_list_dict[tag] = self.column_vector(col,
                                                                              tag,
                                                                              bag_of_character)

        del_list_file = self.__configure__.get_deleted_zero_columm(self.__mission__)
        pk.dump(all_deleted_list_dict, open(del_list_file, "wb"), protocol=2)
        del del_list_file, all_deleted_list_dict

        return all_bag_encoders_dict

    #TODO: refactor the two methods
    def get_bag_bit_by_given_del_list(self, tag, column, bag_of_two_char, del_list):
        unique_col = column.drop_duplicates()

        X = np.mat(np.zeros((unique_col.shape[0], len(bag_of_two_char.dicts))))
        i = 0
        for v in unique_col:
            content_str = str(v)
            X[i] = bag_of_two_char.get_bag(content_str.lower())
            i += 1

        X = np.delete(X, del_list, axis=1)

        bag_bits_map = {}
        i = 0
        for v in unique_col:
            value = X[i]
            bag_bits_map[v] = value
            i += 1

        X_ori = np.mat(np.zeros((column.shape[0], X.shape[1])))
        i = 0
        for v in column:
            X_ori[i] = bag_bits_map[v]
            i += 1

        X_ori = self.process_pca_err_columns(X_ori, tag)
        return X_ori

    def get_bag_bits_map(self, column, bag_of_two_char):
        unique_col = column.drop_duplicates()

        X = np.mat(np.zeros((unique_col.shape[0], len(bag_of_two_char.dicts))))

        i = 0
        for v in unique_col:
            content_str = str(v)
            X[i] = bag_of_two_char.get_bag(content_str.lower())
            i += 1

        del_list = []
        for i in range(X.shape[1]):
            itemSum = X[:, i].sum()
            if itemSum == 0 or itemSum == X.shape[0]:
                del_list.append(i)

        if del_list:
            X = np.delete(X, del_list, axis=1)


        bag_bits_map = {}
        i = 0
        for v in unique_col:
            value = X[i]
            bag_bits_map[v] = value
            i += 1

        X_ori = np.mat(np.zeros((column.shape[0], X.shape[1])))
        i = 0
        for v in column:
            X_ori[i] = bag_bits_map[v]
            i += 1

        return X_ori, del_list

    def process_pca_err_columns(self, X, tag):
        if tag == 'varpre1st' or tag == 'varpre1st' or tag == 'varpre2nd'\
                or tag == 'tppre1st' or tag == 'tppre2nd' or tag == 'tppre3rd':
            X = (X - np.mean(X)) / np.std(X)

        return X

    def column_vector(self, column, tag, bag_of_character):
        column = PCAColumn.process_col(column, tag)

        X, deleted_list = self.get_bag_bits_map(column, bag_of_character)

        X = self.process_pca_err_columns(X, tag)
        return X, deleted_list

    def transform_to_pca_df(self, tag, new_data):
        col_names = ["p_" + tag + '_' + str(i) for i in range(0, new_data.shape[1])]
        df = pd.DataFrame(new_data, columns=col_names)
        return df

    def train_model(self, tag, data, alpha, model_file, labels=None):
        print "PCA Analysing for tag \'%s\' " % tag
        # pca_model = PCA(n_components=alpha).fit(data)
        # pca_data = pca_model.fit_transform(data)

        held = self.selected_feature_num[tag]
        model = LinearDiscriminantAnalysis(n_components=held)

        encoder = LabelEncoder()
        labels = encoder.fit_transform(labels)

        model.fit(data, labels)
        new_data = model.transform(data)

        print '{} DI REDUCTION, FROM {} TO {}'.format(tag, data.shape,  new_data.shape)

        df = self.transform_to_pca_df(tag, new_data)

        pk.dump(model, open(model_file, "wb"), protocol=2)
        del model, data
        gc.collect()

        return df

    def load_model(self, tag, data, model_file):
        if not os.path.exists(model_file):
            print('Model file {} does not exist!'.format(model_file))
            os._exit(0)
        with open(model_file, 'r') as f:
            pca_model = pk.load(f)
            # print('Model loaded from {}'.format(model_file))

        new_data = pca_model.transform(data)
        df = self.transform_to_pca_df(tag, new_data)
        return df


    @staticmethod
    def get_pca_dataframe(ori_file_data, config, mission_type, tags, is_training=False):
        """
        @:param ori_file_data: DataFrame
        @:param tags: list 
        """

        pca_col = PCAColumn(config, mission_type)
        if is_training:
            # get bags
            all_bag_encoders = pca_col.all_vector(ori_file_data, tags)
        else:
            all_bag_encoders = pca_col.load_all_vector(ori_file_data, tags)

        if mission_type == 'expr':
            y = ori_file_data['pred']
        elif mission_type == 'var' or mission_type == 'v0':
            y = ori_file_data['putin']

        pca_df = None
        for tag in tags:
            pca_model_path = config.get_pca_model(mission_type, tag)

            if is_training:

                #pca analysis
                curr_df = pca_col.train_model(tag,
                                              all_bag_encoders[tag],
                                              0.8,
                                              pca_model_path,
                                              y)
                del all_bag_encoders[tag]
            else:
                curr_df = pca_col.load_model(tag,
                                             all_bag_encoders[tag],
                                             pca_model_path)

            # print 'PCA shape for ', tag, curr_df.shape

            if pca_df is None:
                pca_df = curr_df
            else:
                pca_df = pd.concat([pca_df, curr_df], axis=1)

        # print 'PCA finally shape: ', pca_df.shape

        return pca_df