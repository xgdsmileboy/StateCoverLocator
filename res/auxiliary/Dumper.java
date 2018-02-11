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
	private static Set alreadyObserved = new HashSet();
	private static List coverages = new ArrayList();

	private static final long MAX_OUTPUT_FILE_SIZE = 5; // max file size in GB
//	private static final String OUT_AND_LIB_PATH = "/home/jiajun/code/space/StateCoverLocator";
	private static final String OUT_AND_LIB_PATH = "/home/lillian/git/test/StateCoverLocator";
	private static final String OUT_FILE_NAME = OUT_AND_LIB_PATH + "/out/path.out";

	public static void reset() {
		alreadyRun = new HashSet();
		alreadyObserved = new HashSet();
	}

	public static void resetTrueOrFalse() {
		statements = new ArrayList();
		coverages = new ArrayList();
		readTrueOrFalse();
	}
	
	
	public static boolean observe(String stmt) {
		if (!alreadyObserved.contains(stmt)) {
			alreadyObserved.add(stmt);

			read();

			int index = statements.indexOf(stmt);
			if (index >= 0) {
				Record record = (Record) coverage.get(index);
				record.incObserved(SUCC_TEST);
			} else {
				statements.add(stmt);
				Record record = new Record(0, 0, 0, 0);
				record.incObserved(SUCC_TEST);
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
				return false;
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
				Record record = new Record(0, 0, 0, 0);
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
				return false;
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
	
	public static boolean writeTrue(String stmt) {
		return writeTrueOrFalse(stmt, true);
	}
	
	public static boolean writeFalse(String stmt) {
		return writeTrueOrFalse(stmt, false);
	}
	
	private static boolean writeTrueOrFalse(String stmt, boolean isTrue) {
		if (!alreadyRun.contains(stmt)) { // Does not run in this execution
			alreadyRun.add(stmt);
			int index = statements.indexOf(stmt);
			ArrayList records = null;
			if (index >= 0) {
				records = (ArrayList)coverages.get(index);
			} else {
				statements.add(stmt);
				records = new ArrayList();
				coverages.add(records);
			}
			if (isTrue) {
				records.add(new PredicateRecord(1, 0, SUCC_TEST));
			} else {
				records.add(new PredicateRecord(0, 1, SUCC_TEST));
			}
		} else {
			int index = statements.indexOf(stmt);
			ArrayList records = (ArrayList) coverages.get(index);
			PredicateRecord record = (PredicateRecord) records.get(records.size() - 1);
			record.inc(isTrue);
		}
		return true;
	}
	
	public static boolean dump() {
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
				bufferedWriter.write((String) statements.get(i));
				bufferedWriter.write("\n");
				List others = (List) coverages.get(i);
				for(int j = 0; j < others.size(); j++) {
					PredicateRecord r = (PredicateRecord) others.get(j);
					bufferedWriter.write(r.getValue());
					bufferedWriter.write("\n");
				}
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	private static boolean readTrueOrFalse() {
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
			ArrayList others = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				String[] strings = line.split("\t");
				if (strings.length == 3) {
					others.add(new PredicateRecord(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
							strings[2].equals("0") ? false : true));
					continue;
				}
				statements.add(line);
				others = new ArrayList();
				coverages.add(others);
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

	private static boolean read() {
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
				if (strings.length < 5) {
					continue;
				}
				statements.add(strings[0]);
				coverage.add(new Record(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]),
					Integer.parseInt(strings[3]), Integer.parseInt(strings[4])));
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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
	private int passObserved = 0;
	private int failObserved = 0;
	
	public Record(int f, int p, int fo, int po) {
		fail = f;
		pass = p;
		failObserved = fo;
		passObserved = po;
	}
	
	public void inc(boolean passTest) {
		if (passTest) {
			pass++;
		} else {
			fail++;
		}
	}
	
	public void incObserved(boolean passTest) {
		if (passTest) {
			passObserved++;
		} else {
			failObserved++;
		}
	}

	public int getSuc() {
		return pass;
	}

	public int getFail() {
		return fail;
	}

	public String getValue() {
		return fail + "\t" + pass + "\t" + failObserved + "\t" + passObserved;
	}
}

class PredicateRecord {
	private int trueCnt = 0;
	private int falseCnt = 0;
	private boolean pass = false;
	
	public PredicateRecord(int t, int f, boolean pass) {
		trueCnt = t;
		falseCnt = f;
		this.pass = pass;
	}
	
	public void inc(boolean isTrue) {
		if (isTrue) {
			trueCnt++;
		} else {
			falseCnt++;
		}
	}
	
	public String getValue() {
		String passStr = pass ? "1" : "0";
		return trueCnt + "\t" + falseCnt + "\t" + passStr;
	}
}
