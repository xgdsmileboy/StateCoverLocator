import unittest

import numpy as np

from Utils.bag_of_character import BagOfCharacter

class TestBagOfCharacter(unittest.TestCase):

    def setUp(self):
        beg = ord('a')
        end = ord('z') + 1
        self.letters = [chr(i) for i in range(beg, end)]

        #print self.letters

        beg = ord('0')
        end = ord('9') + 1
        self.numbers = [chr(i) for i in range(beg, end)]

        #print self.numbers

        beg = ord(' ')
        end = ord('/') + 1
        self.symbols = [chr(i) for i in range(beg, end)]

        beg = ord(':')
        end = ord('@') + 1
        self.symbols.extend([chr(i) for i in range(beg, end)])
        beg = ord('[')
        end = ord('_') + 1
        self.symbols.extend([chr(i) for i in range(beg, end)])

        #print self.symbols
        alf = list()
        alf.extend(self.letters)
        alf.extend(self.numbers)
        alf.extend(self.symbols)
        self.alpahbet = alf

        print "ALL THE SIZE: ", len(alf)

    def test_bag_of_two(self):
        bag_of_2_character = BagOfCharacter(2, self.numbers)

        dicts = bag_of_2_character.dicts
        print dicts
        self.assertEqual(len(dicts), 10*10 + 10)


    def test_bag_of_three(self):
        bag_of_3_ch = BagOfCharacter(3, self.letters)
        dicts = bag_of_3_ch.dicts
        print dicts
        self.assertEqual(len(dicts), 26*26*26 + 26*26 + 26)


    def test_get_bag_of_two(self):
        bag_of_2_ch = BagOfCharacter(2, self.letters)
        print bag_of_2_ch.dicts
        res = bag_of_2_ch.get_bag('abcd')
        self.assertEqual(np.sum(res), 3)

        res = bag_of_2_ch.get_bag('aaab')
        self.assertEqual(np.sum(res), 2)

        res = bag_of_2_ch.get_bag('a')
        self.assertEqual(res[0], 1)

    def test_get_bag_of_three(self):
        bag_of_2_ch = BagOfCharacter(3, self.letters)
        print bag_of_2_ch.dicts
        res = bag_of_2_ch.get_bag('abcde')
        self.assertEqual(np.sum(res), 3)


if __name__ == '__main__':
   unittest.main()