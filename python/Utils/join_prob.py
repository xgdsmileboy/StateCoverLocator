import math
import os

### join the probs for var and expr by multiplying
def readFile(filePath):
    file = 0
    try:
        file = open(filePath)
    except:
        print ("read file" + filePath + " error")
        return
    lines = []
    while True:
        line = file.readline().decode("utf-8-sig").encode("utf-8")

        if not line:
            break
        line = line.strip()
        if len(line) == 0:
            break
        lines.append(line)
    return lines


def getVarProb(filePath):
    lines = readFile(filePath)
    varProbMap = {}
    for line in lines:
        words = line.split("\t")
        keyTuple = (words[0], words[1])
        value = float(words[2])
        if keyTuple in varProbMap.keys():
            print ('error keyTuple[0]=' + str(keyTuple[0]) + "|keyTuple[1]=" + str(keyTuple[1]))
        varProbMap[keyTuple] = value

    return varProbMap

def getPredicateProb(filePath):
    lines = readFile(filePath)
    predicateProbMap = {}
    keyTuple = ("0", "0")
    lines_len = len(lines)
    i = 0
    while i < lines_len:
        words = lines[i].split("\t")
        value = []
        if len(words) == 4:
            keyTuple = (words[0], words[1])
            value.append((words[2], float(words[3])))
        j = 0
        for j in  range(i + 1, lines_len):
            words = lines[j].split("\t")
            if len(words) == 2:
                value.append((words[0], float(words[1])))
            if len(words) == 4:
                i = j
                break
            if j == lines_len - 1:
                i = lines_len

        predicateProbMap[keyTuple] = value

    return predicateProbMap

def join_prob(config):
    """
    @type config: Configure
    @param config:Configure 
    :return: 
    """
    var_predicted = config.get_var_pred_out_file()
    expr_predicted = config.get_expr_pred_out_file()
    joint_file = config.get_joint_predict_file()

    print(var_predicted)
    varProbMap = getVarProb(var_predicted)
    predicateProbMap = getPredicateProb(expr_predicted)

    varPredicateProbMap = {}

    for key in varProbMap.keys():
        if key in predicateProbMap.keys():
            predicateList = predicateProbMap[key]
            for predicate in predicateList:
                mulProb = varProbMap[key] * predicate[1]
                if mulProb <= 0:
                    continue

                #prob = math.log(varProbMap[key] * predicate[1])
                #prob = varProbMap[key] * predicate[1]
                #prob = math.log(varProbMap[key]) + math.log(predicate[1])
                prob = varProbMap[key] * predicate[1]
                newkey = (key[0], key[1], predicate[0])
                varPredicateProbMap[newkey] = prob

    varPredicateProbTuple = sorted(varPredicateProbMap.items(), lambda x, y: cmp(x[1], y[1]), reverse=True)
    idConsMap = {}
    for item in varPredicateProbTuple:
        key = item[0][0]
        if key not in idConsMap.keys():
            idConsMap[key] = []
        idConsMap[key].append((item[0][1],item[0][2], item[1] ))

    if os.path.exists(joint_file):
        os.remove(joint_file)
    fOut = file(joint_file, "w")
    fOut.write("id\tvar\tpredicate\tprob\n")
    for key in idConsMap:
        valueTupleList = idConsMap[key]
        for valueTuple in valueTupleList:
            if (valueTuple[2] > 0.005):
                fOut.write(key + "\t" + valueTuple[0] + "\t" + valueTuple[1] + "\t" + str(valueTuple[2]) + "\n")
    fOut.close()





