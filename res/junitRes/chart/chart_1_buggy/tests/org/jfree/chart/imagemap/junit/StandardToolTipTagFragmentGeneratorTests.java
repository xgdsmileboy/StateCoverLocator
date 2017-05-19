package org.jfree.chart.imagemap.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;
/** 
 * Tests for the             {@link StandardToolTipTagFragmentGeneratorTests} class.
 */
public class StandardToolTipTagFragmentGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardToolTipTagFragmentGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardToolTipTagFragmentGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the generateURLFragment() method.
 */
  public void testGenerateURLFragment(){
    StandardToolTipTagFragmentGenerator g=new StandardToolTipTagFragmentGenerator();
    assertEquals(" title=\"abc\" alt=\"\"",g.generateToolTipFragment("abc"));
    assertEquals(" title=\"Series &quot;A&quot;, 100.0\" alt=\"\"",g.generateToolTipFragment("Series \"A\", 100.0"));
  }
}
