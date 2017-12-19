from abc import ABCMeta, abstractmethod

from Utils.config import Configure

class PCAForXgb(object):

    __metaclass__ = ABCMeta

    def __init__(self, configure):
        """
        @:param configure: Configure
        """
        assert isinstance(configure, Configure)
        self.__configure__ = configure

    def encoder_column(self, series, encoder_dict):
        """
        @:param series: pandas.Series
        @:param encoder: dict, str->int
        """
        series = series.copy()
        for index, val in series.iteritems():
            if val not in encoder_dict:
                encoder_dict[val] = len(encoder_dict)
            series[index] = encoder_dict[val]
            # print series[index], type(series[index])
        return series.astype('int')


    @abstractmethod
    def get_matrics(self, y_true, y_pred):
        pass

    @abstractmethod
    def train(self):
        pass

    @abstractmethod
    def prepreocess(self, ori_file_data, all_pca_added_df, is_training=False):
        pass

    @abstractmethod
    def predict(self):
        pass

    @abstractmethod
    def get_predicated_label(self, y_pred):
        pass