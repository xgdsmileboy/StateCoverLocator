import pandas as pd
import numpy as np
from math import isnan
from sklearn.cluster import KMeans
import os
from Utils.config import *

class Cluster(object):

	def __init__(self, configure):
		self.__configure__ = configure

	def cluster_var(self):
		# config file path
		var_file_path = self.__configure__.get_var_train_in_file()
		expr_file_path = self.__configure__.get_expr_train_in_file()
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
			# print(v)
			if len(v) == 1:
				X[i, 26 * 26] = 1
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
					X[i, pos] = 1
			varnames.append(v)
			i = i + 1

		print len(unique_var) / 10

		kmeans = KMeans(n_clusters=len(unique_var) / 10, random_state=0).fit(X)

		result = {}
		if (os.path.exists(output_file_path)):
			os.remove(output_file_path)
		with open(output_file_path, 'w+') as f:
			for i in range(0, X.shape[0]):
				result[varnames[i]] = kmeans.labels_[i]
				f.write('%s\t' % varnames[i])
				f.write('%d\n' % kmeans.labels_[i])
		return result

	def get_var_encoder(self):
		result = {}
		# train_file_path = params['input_path'] + params['project'] + "/" + params['project'] + '_' + params['bugid']
		# cluster_file_path = train_file_path + '/cluster/' + params['project'] + '_'+params['bugid']+'.cluster.csv';
		cluster_file_path = self.__configure__.get_cluster_file()
		data = pd.read_csv(cluster_file_path, sep='\t', header=None, encoding='utf-8')
		dataset = data.values
		for i in range(0, dataset.shape[0]):
			result[str(dataset[i, 0])] = dataset[i, 1]
		return result