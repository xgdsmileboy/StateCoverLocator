#-*- coding:utf-8 -*-
import logging

if __name__ == '__main__':
    # 配置日志信息
    logging.basicConfig(level=logging.DEBUG,
              format='%(asctime)s %(levelname)-8s:\n%(message)s\n',
              # datefmt='%m-%d %H:%M',
              filename='myapp.log',
              filemode='w')
    # 定义一个Handler打印INFO及以上级别的日志到sys.stderr
    console = logging.StreamHandler()
    console.setLevel(logging.INFO)
    # 设置日志打印格式
    formatter = logging.Formatter('%(name)-12s: %(levelname)-8s %(message)s')
    console.setFormatter(formatter)
    # 将定义好的console日志handler添加到root logger
    logging.getLogger('').addHandler(console)
    logging.info('Jackdaws love my big sphinx of quartz.')
    logger1 = logging.getLogger('myapp.area1')
    logger2 = logging.getLogger('myapp.area2')
    logger1.debug('Quick zephyrs blow, vexing daft Jim.')
    logger1.info('How quickly daft jumping zebras vex.')
    logger2.warning('Jail zesty vixen who grabbed pay from quack.')
    logger2.error('The five boxing wizards jump quickly.')