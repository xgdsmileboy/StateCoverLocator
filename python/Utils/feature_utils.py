import os
from xml.etree import ElementTree

python_dir = os.path.dirname(os.getcwd())
proj_dir = os.path.dirname(python_dir)
feature_xml_path = proj_dir + '/config/feature.xml'

class FeatureItem:
    def __init__(self, feature_tp, val_dict):
        self.feature_tp = feature_tp
        self.name = val_dict['name']
        self.tp = val_dict['type']
        self.collected = True if val_dict['collected'] == 'true' else False
        self.trained = True if val_dict['trained'] == 'true' else False
        self.bagged = True if val_dict['bagged'] == 'true' else False


def load_all_feature():
    root = ElementTree.parse(feature_xml_path)
    features = root.getiterator('context')
    features.extend(root.getiterator('var'))
    features.extend(root.getiterator('expr'))
    result = []
    for feature in features:
        val = {}
        for cld in feature.getchildren():
            val[cld.tag] = cld.text

        print feature.tag, val
        item = FeatureItem(feature.tag, val)
        result.append(item)
    return result



def load_all_bagged_feature(features):
    result = []
    for f in features:
        if f.bagged:
            result.append(f.name)

    return result


if __name__ == '__main__':
    load_all_feature()
    pass