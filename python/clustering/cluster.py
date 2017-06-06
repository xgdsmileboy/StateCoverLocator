import pandas as pd
import numpy as np
from math import isnan
from sklearn.cluster import KMeans
from sklearn.cluster import AgglomerativeClustering
import os
from Utils.config import *
from sklearn.externals import joblib

class Cluster(object):

	def __init__(self, configure):
		self.__configure__ = configure

	@staticmethod
	def var_to_vec(v):
		result = np.array(np.zeros(27 * 27 + 1))
		if len(v) == 1:
			result[26 * 26] = 1
		else:
			for j in range(0, len(v) - 1):
				first = ord(v[j]) - ord('a')
				second = ord(v[j + 1]) - ord('a')
				if first < 0 or first >= 26:
					first = 26
				if second < 0 or second >= 26:
					second = 26
				# print(first)
				# print(second)
				pos = first * 27 + second
				# print(pos)
				result[pos] = 1
		return result

	def cluster_var(self):
		# config file path
		var_file_path = self.__configure__.get_raw_var_train_in_file()
		expr_file_path = self.__configure__.get_raw_expr_train_in_file()
		output_file_path = self.__configure__.get_cluster_file()

		# read data
		var_data = pd.read_csv(var_file_path, sep='\t', header=0, encoding='utf-8')
		expr_data = pd.read_csv(expr_file_path, sep='\t', header=0, encoding='utf-8')

		var_dataset = var_data.values
		expr_dataset = expr_data.values

		all_var = np.row_stack((var_dataset[:, 5:6], expr_dataset[:, 5:6]))

		# all_var = np.array([['len'], ['length'], ['cross'], ['clockwise']])
		all_var = all_var.astype(str)

		unique_var = set()

		for i in range(0, all_var.shape[0]):
			unique_var.add(str(all_var[i][0]).lower())

		X = np.mat(np.zeros((len(unique_var), 27 * 27 + 1)))
		varnames = list()

		i = 0
		for v in unique_var:
			X[i] = self.var_to_vec(v)
			varnames.append(v)
			i = i + 1

		print len(unique_var) / 20

		kmeans = KMeans(n_clusters=len(unique_var) / 20, random_state=0).fit(X)
		# ac = AgglomerativeClustering(n_clusters=len(unique_var) / 20, linkage="complete").fit(X)

		# export model
		joblib.dump(kmeans, self.__configure__.get_cluster_model_file())

		result = {}
		if (os.path.exists(output_file_path)):
			os.remove(output_file_path)
		with open(output_file_path, 'w+') as f:
			for i in range(0, X.shape[0]):
				result[varnames[i]] = kmeans.labels_[i]
				f.write('%s\t' % varnames[i])
				f.write('%d\n' % kmeans.labels_[i])
		return result, var_data.shape[1], expr_data.shape[1]

	def get_var_cluster(self):
		result = {}
		# train_file_path = params['input_path'] + params['project'] + "/" + params['project'] + '_' + params['bugid']
		# cluster_file_path = train_file_path + '/cluster/' + params['project'] + '_'+params['bugid']+'.cluster.csv';
		cluster_file_path = self.__configure__.get_cluster_file()
		data = pd.read_csv(cluster_file_path, sep='\t', header=None, encoding='utf-8')
		dataset = data.values
		for i in range(0, dataset.shape[0]):
			result[str(dataset[i, 0])] = dataset[i, 1]

		kmeans = joblib.load(self.__configure__.get_cluster_model_file())
		return result, kmeans