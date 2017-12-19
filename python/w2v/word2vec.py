from gensim.models import word2vec
import sys
import re

def train_word2vec(token_file):
    sentences = word2vec.Text8Corpus(token_file)
    model = word2vec.Word2Vec(sentences, size=1, window=10)

    print model
    y2 = model.most_similar(['p1'], topn=20)

    fltreg = re.compile(r'^[-+]?[0-9]+\.[0-9]+$')

    for item in y2:
        curr = str(item[0])
        print curr
        if curr.isdigit() or fltreg.match(curr):
            print '>>>>>>>>', item[0]

    y4 = model.doesnt_match(['entry', 'epsilon', 'maxUlps'])
    # print y4

if __name__ == '__main__':
    # if len(sys.argv) != 2:
    #     print("Wrong argument number!")
    #     sys.exit(1)

    # token_file = sys.argv[1]

    token_file = '/home/nightwish/tmp/res/math_37.token.txt'
    train_word2vec(token_file)
