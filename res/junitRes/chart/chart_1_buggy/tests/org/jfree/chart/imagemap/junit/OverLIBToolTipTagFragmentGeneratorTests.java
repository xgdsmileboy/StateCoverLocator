package org.jfree.chart.imagemap.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.imagemap.OverLIBToolTipTagFragmentGenerator;
/** 
 * Tests for the             {@link OverLIBToolTipTagFragmentGenerator} class.
 */
public class OverLIBToolTipTagFragmentGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(OverLIBToolTipTagFragmentGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public OverLIBToolTipTagFragmentGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the generateURLFragment() method.
 */
  public void testGenerateURLFragment(){
    OverLIBToolTipTagFragmentGenerator g=new OverLIBToolTipTagFragmentGenerator();
    assertEquals(" onMouseOver=\"return overlib('abc');\"" + " onMouseOut=\"return nd();\"",g.generateToolTipFragment("abc"));
    assertEquals(" onMouseOver=\"return overlib(" + "'It\\'s \\\"A\\\", 100.0');\" onMouseOut=\"return nd();\"",g.generateToolTipFragment("It\'s \"A\", 100.0"));
  }
}
