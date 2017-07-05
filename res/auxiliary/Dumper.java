package auxiliary;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dumper {

	public static boolean SUCC_TEST = false;
	private static List statements = new ArrayList();
	private static List coverage = new ArrayList();
	private static Set alreadyRun = new HashSet();

	private static final long MAX_OUTPUT_FILE_SIZE = 5; // max file size in GB
//	private static final String OUT_AND_LIB_PATH = "/home/jiajun/code/space/StateCoverLocator";
	private static final String OUT_AND_LIB_PATH = "/home/lillian/git/StateCoverLocator";
	private static final String OUT_FILE_NAME = OUT_AND_LIB_PATH + "/out/path.out";

	public static void reset() {
		alreadyRun = new HashSet();
	}

	public static boolean write(String stmt) {
		if (!alreadyRun.contains(stmt)) {
			alreadyRun.add(stmt);

			read();

			int index = statements.indexOf(stmt);
			if (index >= 0) {
				Record record = (Record) coverage.get(index);
				record.inc(SUCC_TEST);
			} else {
				statements.add(stmt);
				Record record = new Record(0, 0);
				record.inc(SUCC_TEST);
				coverage.add(record);
			}

			File file = new File(OUT_FILE_NAME);
			if (!file.exists()) {
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
				} catch (IOException e) {
					return false;
				}
			}
			BufferedWriter bufferedWriter = null;
			try {
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
			} catch (IOException e) {
				return false;
			}
			try {

				for (int i = 0; i < statements.size(); i++) {
					Record record = (Record) coverage.get(i);
					String content = (String) statements.get(i) + "\t" + record.getValue();
					bufferedWriter.write(content);
					bufferedWriter.write("\n");
				}
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bufferedWriter != null) {
					try {
						bufferedWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return true;
	}

	public static boolean read() {
		statements = new ArrayList();
		coverage = new ArrayList();
		File file = new File(OUT_FILE_NAME);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (IOException e) {
			return false;
		}
		try {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] strings = line.split("\t");
				if (strings.length < 3) {
					continue;
				}
				statements.add(strings[0]);
				coverage.add(new Record(Integer.parseInt(strings[1]), Integer.parseInt(strings[2])));
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}

class Record {
	private int pass = 0;
	private int fail = 0;

	public Record(int f, int p) {
		fail = f;
		pass = p;
	}

	public void inc(boolean passTest) {
		if (passTest) {
			pass++;
		} else {
			fail++;
		}
	}

	public int getSuc() {
		return pass;
	}

	public int getFail() {
		return fail;
	}

	public String getValue() {
		return fail + "\t" + pass;
	}
}
