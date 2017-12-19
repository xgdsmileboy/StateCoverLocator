import pandas as pd
from Utils.string_utils import *
import logging

if __name__ == '__main__':
    data_file = '/home/nightwish/workspace/eclipse/Condition/python/input/math/math_12/var/math_12.var.csv'
    ori_file_data = pd.read_csv(data_file, sep='\t', header=0, encoding='utf-8')
    ori_file_data['num0'].apply(preprocess_numbers)
    ori_file_data['num1'].apply(preprocess_numbers)


    log_file = "./basic_logger.log"

    logging.basicConfig(filename=log_file, level=logging.DEBUG)

    logging.debug("this is a debugmsg!")
    logging.info("this is a infomsg!")
    logging.warn("this is a warn msg!")
    logging.error("this is a error msg!")
    logging.critical("this is a critical msg!")
