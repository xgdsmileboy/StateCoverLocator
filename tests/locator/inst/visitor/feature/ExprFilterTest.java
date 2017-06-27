package locator.inst.visitor.feature;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import locator.common.config.Constant;
import locator.common.java.Subject;

public class ExprFilterTest {

//	@Test
//	public void test_isLegal(){
//		Constant.PROJECT_HOME = "res/junitRes";
//		Subject subject = new Subject("chart", 1, "/source", "/tests", "", "");
//		ExprFilter.init(subject);
//		//primitive type
//		Assert.assertTrue(ExprFilter.isLegalExpr("int", "a", "a > 4"));
//		//non-existing function
//		//org.jfree.data.general.AbstractDataset
//		Assert.assertFalse(ExprFilter.isLegalExpr("AbstractDataset", "var", "var.mFun() > 6"));
//		//existing function
//		Assert.assertTrue(ExprFilter.isLegalExpr("AbstractDataset", "var", "var.hasListener() == null"));
//		//existing function
//		//org.jfree.data.pie.DefaultPieDataset
//		Assert.assertTrue(ExprFilter.isLegalExpr("DefaultPieDataset", "var", "var.getItemCount() == 1"));
//		//function in extended class
//		Assert.assertTrue(ExprFilter.isLegalExpr("DefaultPieDataset", "var", "var.getSelectionState() == null"));
//	}
	
	@Test
	public void test_newIsLegal(){
		Map<String, String> localLegalVar = new HashMap<>();
		localLegalVar.put("a", "int");
		localLegalVar.put("b", "int");
		localLegalVar.put("c", "float");
		localLegalVar.put("d", "double");
		localLegalVar.put("m", "boolean");
		localLegalVar.put("arr", "int[]");
		Assert.assertTrue(NewExprFilter.filter("int", "a", "a < b", localLegalVar, null).equals("a >= b"));
		Assert.assertTrue(NewExprFilter.filter("int", "b", "1.2 > b", localLegalVar, null).equals("2 > b"));
		Assert.assertTrue(NewExprFilter.filter("float", "c", "c <= b", localLegalVar, null).equals("c > b"));
		Assert.assertTrue(NewExprFilter.filter("double", "d", "d > 1.0", localLegalVar, null).equals("d > 1.0"));
		Assert.assertTrue(NewExprFilter.filter("int[]", "arr", "arr[b] > 1.0", localLegalVar, null).equals("arr[b] > 1.0"));
		Assert.assertTrue(NewExprFilter.filter("double", "d", "Double.isNaN(d)", localLegalVar, null).equals("Double.isNaN(d)"));
		Assert.assertTrue(NewExprFilter.filter("double", "d", "d.length > 1", localLegalVar, null) == null);
		Assert.assertTrue(NewExprFilter.filter("int", "d", "arr[d] > 1.0", localLegalVar, null) == null);
		Assert.assertTrue(NewExprFilter.filter("int", "f", "f < b", localLegalVar, null) == null);
		Assert.assertTrue(NewExprFilter.filter("boolean", "m", "m < 1.0", localLegalVar, null) == null);
	}
}
