package org.jfree.chart.imagemap.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.imagemap.StandardURLTagFragmentGenerator;
/** 
 * Tests for the             {@link StandardURLTagFragmentGeneratorTests} class.
 */
public class StandardURLTagFragmentGeneratorTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(StandardURLTagFragmentGeneratorTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public StandardURLTagFragmentGeneratorTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the generateURLFragment() method.
 */
  public void testGenerateURLFragment(){
    StandardURLTagFragmentGenerator g=new StandardURLTagFragmentGenerator();
    assertEquals(" href=\"abc\"",g.generateURLFragment("abc"));
    assertEquals(" href=\"images/abc.png\"",g.generateURLFragment("images/abc.png"));
    assertEquals(" href=\"http://www.jfree.org/images/abc.png\"",g.generateURLFragment("http://www.jfree.org/images/abc.png"));
  }
}
