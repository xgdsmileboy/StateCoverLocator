from set_evn import execute_cmd

import ConfigParser
import os

pred_config = ConfigParser.ConfigParser()
pred_config.read("env.conf")

bug_config = ConfigParser.ConfigParser()
bug_config.read("bug.conf")

home_path = os.environ['HOME']

def get_feature_config(key):
    src_root = home_path + bug_config.get(key, 'src_root')
    file_paths = bug_config.get(key, 'file_path').split(';')
    lines = bug_config.get(key, 'line').split(';')
    assert len(file_paths) == len(lines)
    return src_root, file_paths, lines



if __name__ == '__main__':
    for sec in bug_config.sections():
        sec = str(sec)
        proj = sec.split('_')[0]
        bugid = int(sec.split('_')[1])

        if proj != 'math' or (bugid > 79 or bugid <= 36):
            continue

        print sec
        src_root = home_path + bug_config.get(sec, 'src_root')
        test_root = home_path + bug_config.get(sec, 'test_root')
        all_file_paths = bug_config.get(sec, 'file_path').split(';')
        all_buggy_lines = bug_config.get(sec, 'line').split(';')
        assert len(all_file_paths) == len(all_buggy_lines)
        # print root, all_file_paths, all_buggy_lines
        for i, (file, line) in enumerate(zip(all_file_paths, all_buggy_lines)):
            java8Path = home_path + pred_config.get('PATH', 'java8')
            cmd = " ".join([java8Path, "-jar Condition.jar", sec, src_root, test_root, file, line, str(i)])
            print cmd
            execute_cmd(cmd)
            # break