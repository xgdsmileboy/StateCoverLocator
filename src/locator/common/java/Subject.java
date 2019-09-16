/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
	private String _src_level = "1.6";
	private String _class_level = "1.6";
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
		this(name, id, ssrc, tsrc, sbin, tbin, new LinkedList<String>());
		_classpath = obtainClasspath(name);
	}
	
	public Subject(String name, int id, String ssrc, String tsrc, String sbin, String tbin, List<String> classpath) {
		_name = name;
		_id = id;
		_ssrc = ssrc;
		_tsrc = tsrc;
		_sbin = sbin;
		_tbin = tbin;
		_classpath = classpath;
		// Special case
		if(name.equals("chart")) {
			_src_level = "1.4";
			_class_level = "1.4";
		}
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
	
	public String getSourceLevel() {
		return _src_level;
	}
	
	public String getTargetLevel() {
		return _class_level;
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
	
	private List<String> obtainClasspath(String projName) {
		List<String> classpath = new LinkedList<>();
		switch(projName) {
		case "math":
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/hamcrest-core-1.3.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/junit-4.11.jar");
			break;
		case "chart":
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/junit-4.11.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/iText-2.1.4.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/servlet.jar");
			break;
		case "lang":
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/cglib-nodep-2.2.2.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/commons-io-2.4.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/easymock-3.1.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/hamcrest-core-1.3.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/junit-4.11.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/objenesis-1.2.jar");
			break;
		case "closure":
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/caja-r4314.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/jarjar.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/ant.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/ant-launcher.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/args4j.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/jsr305.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/guava.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/json.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/protobuf-java.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/junit-4.11.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/rhino.jar");
			break;
		case "time":
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/junit-4.11.jar");
			classpath.add(Constant.STR_PROJ_DEPENDCE_PATH + "/joda-convert-1.2.jar");
			break;
		case "mockito":
			break;
		default :
			System.err.println("UNKNOWN project name : " + projName);
		}
		return classpath;
	}

	/**
	 * get absolute home path for subject
	 * 
	 * @return e.g., "/home/user/chart/chart_1_buggy"
	 */
	public String getHome() {
		return Constant.PROJECT_HOME + "/" + _name + "/" + _name + "_" + _id + "_buggy";
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
