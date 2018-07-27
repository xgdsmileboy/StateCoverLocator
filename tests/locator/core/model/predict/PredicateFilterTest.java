package locator.core.model.predict;

import org.junit.Assert;
import org.junit.Test;

public class PredicateFilterTest {

	@Test
	public void test_filterType() {
		String predicate = PredicateFilter.filter("$boolean$", "name", "String");
		Assert.assertNull(predicate);
		
		predicate = PredicateFilter.filter("$Boolean$", "value", "int");
		Assert.assertNull(predicate);
		
		predicate = PredicateFilter.filter("$int$>0", "value", "double");
		Assert.assertTrue("value>0".equals(predicate));
		
		predicate = PredicateFilter.filter("$List$.size()", "value", "float");
		Assert.assertNull(predicate);
		
		predicate = PredicateFilter.filter("$Object$==null", "name", "String");
		Assert.assertTrue("name==null".equals(predicate));
	}
	
	@Test
	public void test_removeThis() {
		String predicate = PredicateFilter.filter("this.$int$ > 0", "value", "int");
		Assert.assertTrue("value>0".equals(predicate));
		
		predicate = PredicateFilter.filter("this.$Object$==null", "value", "int");
		Assert.assertNull(predicate);
	}
	
	@Test
	public void test_normalize() {
		String predicate = PredicateFilter.filter("$int$<=0", "value", "int");
		Assert.assertTrue("value>0".equals(predicate));
		
		predicate = PredicateFilter.filter("$int$ > 0", "value", "float");
		Assert.assertTrue("value>0".equals(predicate));
		
		predicate = PredicateFilter.filter("!($int$<=1.1)", "value", "int");
		Assert.assertTrue("value>1.1".equals(predicate));
		
		predicate = PredicateFilter.filter("!($int$!=1.0)", "value", "int");
		Assert.assertTrue("value==1".equals(predicate));
		
		predicate = PredicateFilter.filter("$int$<1.0||m>0", "value", "float");
		Assert.assertTrue("value < 1&&m > 0".equals(predicate));
		
		predicate = PredicateFilter.filter("!(!($int$!=24.0))", "value", "float");
		Assert.assertTrue("value==24".equals(predicate));
		
	}
	
}
