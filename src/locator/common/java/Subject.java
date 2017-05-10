/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

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

	public Subject(String name, int id, String ssrc, String tsrc, String sbin, String tbin) {
		_name = name;
		_id = id;
		_ssrc = ssrc;
		_tsrc = tsrc;
		_sbin = sbin;
		_tbin = tbin;
	}

	public String getName() {
		return _name;
	}

	public int getId() {
		return _id;
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
	
	public String getHome(){
		return Constant.PROJECT_HOME + "/" + _name + "/" + _name + "_" + _id + "_buggy";
	}


	@Override
	public String toString() {
		return "Subject [_name=" + _name + ", _id=" + _id + ", _ssrc=" + _ssrc + ", _tsrc=" + _tsrc + ", _sbin=" + _sbin
				+ ", _tbin=" + _tbin + "]";
	}
}
