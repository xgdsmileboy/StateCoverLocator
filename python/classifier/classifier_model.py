import tensorflow as tf

def get_dnn_classifier(feature_num, class_num, model_dir_):
	feature_columns = [tf.feature_column.numeric_column("x", shape=[feature_num])]
	return tf.estimator.DNNClassifier(feature_columns = feature_columns,
                                            hidden_units = [20, 20],
                                            n_classes = class_num,
                                            optimizer = tf.train.ProximalAdagradOptimizer(
                                                learning_rate=0.05,
                                                l2_regularization_strength=0.0001,
                                              ),
                                            model_dir = model_dir_)