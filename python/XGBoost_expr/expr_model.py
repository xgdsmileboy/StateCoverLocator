import tensorflow as tf
from Utils.config import *

def get_dnn_classifier(feature_num, class_num, model_dir_):
	feature_columns = [tf.feature_column.numeric_column("x", shape=[feature_num])]
	return tf.estimator.DNNClassifier(feature_columns = feature_columns,
                                            hidden_units = [64, 64, 64, 64, 64, 64],
                                            n_classes = class_num,
                                            model_dir = model_dir_)