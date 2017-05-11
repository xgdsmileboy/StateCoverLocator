/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package locator.common.java;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Identifier;

/**
 * @author Jiajun
 * @date May 11, 2017
 */
public class MethodTest {
	
	@Test
	public void test_equals(){
		String methodString = "org.jfree.data.ComparableObjectItem#?#ComparableObjectItem#?,Comparable,Object";
		int id = Identifier.getIdentifier(methodString);
		Method method = new Method(id);
		Method another = new Method(id);
		Assert.assertTrue(method.equals(another));
	}
	
	@Test
	public void test_mapContaion(){
		String methodString = "org.jfree.data.ComparableObjectItem#?#ComparableObjectItem#?,Comparable,Object";
		int id = Identifier.getIdentifier(methodString);
		Method method = new Method(id);
		Map<Method, Integer> map = new HashMap<>();
		map.put(method, 0);
		
		Method another = new Method(id);
		Integer value = map.get(another);
		Assert.assertNotNull(value);
	}
	
}
