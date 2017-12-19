import re
import numpy as np

def remove_dotjava(file):
    if file.find("."):
        return file.split('.', 1)[0]
    return file

def remove_for_names(expr):
    if type(expr) is not str:
        expr = str(expr)
    expr = expr.replace('_', '')
    expr = expr.replace('$', '')
    expr = expr.replace('.', '')
    return expr

def remove_for_fnames(expr):
    if type(expr) is not str:
        expr = str(expr)
    expr = expr.replace('_', '')
    expr = expr.replace('$', '')
    expr = expr.replace('.', '')
    expr = re.sub('\d+', '#', expr)
    return expr


def remove_spcace(expr):
    if type(expr) is not str:
        expr = str(expr)
    expr = expr.replace(' ', '')
    expr = expr.replace('{', '')
    expr = expr.replace('}', '')
    expr = expr.replace(';', '')
    if len(expr) == 0:
        expr = '__'
    return expr

def remove_for_allvar_and_allfld(expr):
    expr = str(expr)
    expr = expr.replace(' ', '')
    expr = expr.replace(':', '')
    expr = expr.replace('<', '')
    expr = expr.replace('>', '')
    expr = expr.replace('?', '')
    expr = expr.replace('.', '')
    expr = expr.replace('$', '')
    expr = expr.replace(',', '')
    expr = expr.replace('_', '')
    expr = re.sub('\d+', '#', expr)
    return expr


def remove_for_syntax(expr):
    expr = str(expr)
    expr = expr.replace(' ', '')
    expr = expr.replace(':', '')
    expr = expr.replace('_', '')
    expr = expr.replace(',', '')
    expr = re.sub('\d+', '#', expr)
    return expr

def preprocess_numbers(num):
    num = str(num)
    if num == 'NIL':
        return np.nan
    if num.startswith('0x'):
        return long(num, 16)
    elif num.endswith('L') or num.endswith('l'):
        return long(num, 10)
    elif num.endswith('d') or num.endswith('D') or  num.endswith('f') or  num.endswith('F'):
        num = num[: len(num) - 1]
	# sys.stderr.write(">>>> " + num)
    return float(num)
