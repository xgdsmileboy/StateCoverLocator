/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.core;

import java.util.ArrayList;
import java.util.List;

import locator.common.util.LevelLogger;

/**
 * @author Jiajun
 * @date Jun 13, 2017
 */
public class CList {
	private List<Double> _data = null;
	private int _length = 0;
	
	public CList(int length) {
		_length = length;
		_data = new ArrayList<>();
		for(int i = 0; i < length; i++){
			_data.add(0d);
		}
	}
	
	public int size(){
		return _length;
	}
	
	public void set(int index, Double f){
		if(index >= _length){
			LevelLogger.error("@Clist #set index out of boundary.");
		} else {
			_data.set(index, f);
		}
	}
	
	public Double get(int index){
		if(index >= _length){
			LevelLogger.error("@Clist #set index out of boundary.");
			return Double.valueOf(0f);
		} else {
			return _data.get(index);
		}
	}
	
}
