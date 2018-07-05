/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.ReturnInstruction;

import locator.common.config.Constant;

/**
 * @author Jiajun
 * @date May 9, 2017
 */
public class Subject {

	private String _name = null;
	private int _id = 0;
	private String _ssrc = null;
	private String _tsrc = null;
	private String _sbin = null;
	private String _tbin = null;
	private List<String> _classpath;

	/**
	 * subject
	 * 
	 * @param name
	 *            : name of subject, e.g., "chart".
	 * @param id
	 *            : number of subject, e.g., 1.
	 * @param ssrc
	 *            : relative path for source folder, e.g., "/source"
	 * @param tsrc
	 *            : relative path for test folder, e.g., "/tests"
	 * @param sbin
	 *            : relative path for source byte code, e.g., "/classes"
	 * @param tbin
	 *            : relative path for test byte code, e.g., "/test-classes"
	 */
	public Subject(String name, int id, String ssrc, String tsrc, String sbin, String tbin) {
		this(name, id, ssrc, tsrc, sbin, tbin, new ArrayList<String>(0));
	}
	
	public Subject(String name, int id, String ssrc, String tsrc, String sbin, String tbin, List<String> classpath) {
		_name = name;
		_id = id;
		_ssrc = ssrc;
		_tsrc = tsrc;
		_sbin = sbin;
		_tbin = tbin;
		_classpath = classpath;
	}

	public String getName() {
		return _name;
	}

	public int getId() {
		return _id;
	}

	public String getNameAndId() {
		return _name + "_" + _id;
	}
	
	public String getSsrc() {
		return _ssrc;
	}

	public String getTsrc() {
		return _tsrc;
	}

	public String getSbin() {
		return _sbin;
	}

	public String getTbin() {
		return _tbin;
	}
	
	public void setClasspath(List<String> classpath) {
		_classpath = classpath;
	}
	
	public List<String> getClasspath() {
		return _classpath;
	}
	
	public boolean checkAndInitDir() {
		File file = new File(getHome() + getSbin());
		if(!file.exists()) {
			file.mkdirs();
		}
		file = new File(getHome() + getTbin());
		if(!file.exists()) {
			file.mkdirs();
		}
		return true;
	}

	/**
	 * get absolute home path for subject
	 * 
	 * @return e.g., "/home/user/chart/chart_1_buggy"
	 */
	public String getHome() {
		return Constant.PROJECT_HOME + "/" + _name + "/" + _name + "_" + _id + "_buggy";
	}
	
	public String getVarFeatureOutputPath() {
		String file = Constant.STR_ML_OUT_FILE_PATH + "/" + _name + "/" + _name + "_" + _id + "/pred/" + _name + "_"
				+ _id + ".var.csv";
		return file;
	}

	public String getExprFeatureOutputPath() {
		String file = Constant.STR_ML_OUT_FILE_PATH + "/" + _name + "/" + _name + "_" + _id + "/pred/" + _name + "_"
				+ _id + ".expr.csv";
		return file;
	}

	public String getPredicResultPath() {
		String file = Constant.STR_ML_PREDICT_EXP_PATH + "/" + _name + "/" + _name + "_" + _id + "/" + _name + "_" + _id
				+ ".joint.csv";
		return file;
	}
	
	public String getPredictResultDir() {
		String file = Constant.STR_ML_PREDICT_EXP_PATH + "/" + _name + "/" + _name + "_" + _id;
		return file;
	}
	
	public String getCoverageInfoPath() {
		return Constant.STR_INFO_OUT_PATH + "/" + _name + "/" + _name + "_" + _id;
	}

	@Override
	public String toString() {
		return "[_name=" + _name + ", _id=" + _id + ", _ssrc=" + _ssrc + ", _tsrc=" + _tsrc + ", _sbin=" + _sbin
				+ ", _tbin=" + _tbin + "]";
	}
}
