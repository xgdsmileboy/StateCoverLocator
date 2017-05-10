/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

import java.util.ArrayList;
import java.util.List;

import locator.common.config.Identifier;


public class TestMethod extends Method{
	
	/**
	 * record the execution path info of this test method, contain duplicated
	 * methods if run more than one time
	 */
	private List<Method> _path = null;
	/**
	 * record the test statement number, such as there are two lines of test in
	 * the test case, this field may be 1 or 2
	 */
	private int _whichStatement = 1;
	
	public TestMethod(int methodID){
		super(methodID);
		_path = new ArrayList<>();
	}
	
	public void setTestStatementNumber(int whichStatement) {
		_whichStatement = whichStatement;
	}

	public int getTestStatementNumber() {
		return _whichStatement;
	}

	public List<Method> getExecutionPath() {
		return _path;
	}
	
	public void addExecutedMethod(Method method) {
		Method newPath = new Method(method.getMethodID());
		_path.add(method);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof TestMethod)){
			return false;
		}
		TestMethod other = (TestMethod)obj;
		if(other.getMethodID() != getMethodID()){
			return false;
		}
			
		return true;
	}

	@Override
	public String toString() {
		String methodString = Identifier.getMessage(getMethodID());
		return methodString + "#" + _whichStatement;
	}
}
