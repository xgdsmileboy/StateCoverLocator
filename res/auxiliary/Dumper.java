package auxiliary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
	private static int count_succ_cover = 0;
	private static int count_fail_cover = 0;
	private static List statements = new ArrayList();
	private static List coverage = new ArrayList();
	private static Set alreadyRun = new HashSet();

	private static final long MAX_OUTPUT_FILE_SIZE = 5; // max file size in GB
	private static final String OUT_AND_LIB_PATH = "/Users/Jiajun/Code/Java/fault-localization/StateCoverLocator";
	private static final String OUT_FILE_NAME = OUT_AND_LIB_PATH + "/out/path.out";


	public static void reset() {
		alreadyRun = new HashSet();
	}

	public static boolean write(String stmt) {
		if (!alreadyRun.contains(stmt)) {
			alreadyRun.add(stmt);
			int index = statements.indexOf(stmt);
			if(index >= 0){
				Record record = (Record)coverage.get(index);
				record.inc(SUCC_TEST);
			} else {
				statements.add(stmt);
				Record record = new Record();
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
				
				for(int i = 0; i < statements.size(); i++){
				    Record record = (Record) coverage.get(i);
					if(record.getFail() == 0){
						continue;
					}
					String content = (String)statements.get(i) + "\t" + record.getValue();
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

	public static boolean write() {
		if (SUCC_TEST) {
			count_succ_cover++;
		} else {
			count_fail_cover++;
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
			bufferedWriter.write(count_succ_cover);
			bufferedWriter.write("\n");
			bufferedWriter.write(count_fail_cover);
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
		return true;
	}

}

class Record {
	private int succ = 0;
	private int fail = 0;

	public void inc(boolean succTest) {
		if (succTest) {
			succ++;
		} else {
			fail++;
		}
	}

	public int getSuc() {
		return succ;
	}

	public int getFail() {
		return fail;
	}
	
	public String getValue() {
		return succ + "," + fail;
	}

}
