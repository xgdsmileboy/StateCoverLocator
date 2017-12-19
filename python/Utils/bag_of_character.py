import numpy as np
import itertools
import copy

class BagOfCharacter:
    def __init__(self, bag_num, alpahbet):
        self.bag_num = bag_num
        self.alpahbet = alpahbet
        self.dicts = None
        self.gen_dictionary()

    def gen_dictionary(self):
        current_set = copy.copy(self.alpahbet)
        dicts = set(current_set)

        for i in range(0, self.bag_num - 1):
            for x in itertools.product(current_set, self.alpahbet):
                curr_word = str(x[0]) + str(x[1])
                dicts.add(curr_word)
                current_set.append(curr_word)

        self.dicts = list(dicts)
        self.dicts.sort()

    def get_bag(self, val):
        result = np.array(np.zeros(len(self.dicts)))

        if len(val) < self.bag_num:
            result[self.dicts.index(val)] = 1
            return result

        try:
            for i in range(0, len(val) - self.bag_num + 1):
                curr_bag = val[i:i + self.bag_num]
                result[self.dicts.index(curr_bag)] = 1
        except Exception as e:
            print(e)
            print val

        return result

    @staticmethod
    def get_letters():
        beg = ord('a')
        end = ord('z') + 1
        letters = [chr(i) for i in range(beg, end)]
        return letters

    @staticmethod
    def get_nums():
        beg = ord('0')
        end = ord('9') + 1
        nums = [chr(i) for i in range(beg, end)]
        return nums

    @staticmethod
    def get_symbols():
        beg = ord(' ')
        end = ord('/') + 1
        symbols = [chr(i) for i in range(beg, end)]

        beg = ord(':')
        end = ord('@') + 1
        symbols.extend([chr(i) for i in range(beg, end)])
        beg = ord('[')
        end = ord('_') + 1
        symbols.extend([chr(i) for i in range(beg, end)])

        symbols.append('|')
        return symbols

    @staticmethod
    def get_var_name():
        '''
        get a alphabet for 'varname'
        '''
        letters = BagOfCharacter.get_letters()
        numbers = BagOfCharacter.get_nums()
        naming_alpha = list(letters)
        naming_alpha.extend(numbers)
        # naming_alpha.append('.')
        # naming_alpha.append('_')
        # naming_alpha.append('$')
        naming_alpha.sort()
        return naming_alpha

    @staticmethod
    def get_file_name():
        '''
        get a alphabet for 'filename', 'tdname', 'methodname'
        '''
        letters = BagOfCharacter.get_letters()
        naming_alpha = list(letters)
        naming_alpha.append('#')
        # naming_alpha.extend(numbers)
        # naming_alpha.append('.')
        # naming_alpha.append('_')
        # naming_alpha.append('$')
        naming_alpha.sort()
        return naming_alpha

    @staticmethod
    def get_all_var():
        '''
        get a alphabet for 'allvar', 'allfld'
        '''
        letters = BagOfCharacter.get_letters()
        numbers = BagOfCharacter.get_nums()
        alpha = list(letters)
        alpha.append('#')
        # alpha.extend(numbers)
        # alpha.append(' ')
        # alpha.append('.')
        # alpha.append('_')
        # alpha.append('$')
        # alpha.append(':')
        alpha.append('[')
        alpha.append(']')
        # alpha.append('<')
        # alpha.append('>')
        alpha.sort()
        return alpha

    @staticmethod
    def get_syntax_var():
        '''
        get a alphabet for 'filename', 'tdname', 'methodname'
        '''
        letters = BagOfCharacter.get_letters()
        numbers = BagOfCharacter.get_nums()
        alpha = list(letters)
        alpha.append('#')
        # alpha.extend(numbers)
        # alpha.append(',')
        # alpha.append(' ')
        alpha.append('[')
        alpha.append(']')
        alpha.append('(')
        alpha.append(')')
        # alpha.append(':')
        alpha.sort()
        return alpha

    @staticmethod
    def get_expr():
        letters = BagOfCharacter.get_letters()
        numbers = BagOfCharacter.get_nums()
        symbols = BagOfCharacter.get_symbols()
        alpah = list(letters)
        alpah.extend(numbers)
        alpah.extend(symbols)
        alpah.sort()
        return alpah


if __name__ == '__main__':
    pass