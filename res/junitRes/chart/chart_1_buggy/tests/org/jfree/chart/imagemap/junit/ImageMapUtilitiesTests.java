package org.jfree.chart.imagemap.junit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.imagemap.ImageMapUtilities;
/** 
 * Tests for the             {@link ImageMapUtilities} class.
 */
public class ImageMapUtilitiesTests extends TestCase {
  /** 
 * Returns the tests as a test suite.
 * @return The test suite.
 */
  public static Test suite(){
    return new TestSuite(ImageMapUtilitiesTests.class);
  }
  /** 
 * Constructs a new set of tests.
 * @param name  the name of the tests.
 */
  public ImageMapUtilitiesTests(  String name){
    super(name);
  }
  /** 
 * Some checks for the htmlEscape() method.
 */
  public void testHTMLEscape(){
    assertEquals("",ImageMapUtilities.htmlEscape(""));
    assertEquals("abc",ImageMapUtilities.htmlEscape("abc"));
    assertEquals("&amp;",ImageMapUtilities.htmlEscape("&"));
    assertEquals("&quot;",ImageMapUtilities.htmlEscape("\""));
    assertEquals("&lt;",ImageMapUtilities.htmlEscape("<"));
    assertEquals("&gt;",ImageMapUtilities.htmlEscape(">"));
    assertEquals("&#39;",ImageMapUtilities.htmlEscape("\'"));
    assertEquals("&#092;abc",ImageMapUtilities.htmlEscape("\\abc"));
    assertEquals("abc\n",ImageMapUtilities.htmlEscape("abc\n"));
  }
  /** 
 * Some checks for the javascriptEscape() method.
 */
  public void testJavascriptEscape(){
    assertEquals("",ImageMapUtilities.javascriptEscape(""));
    assertEquals("abc",ImageMapUtilities.javascriptEscape("abc"));
    assertEquals("\\\'",ImageMapUtilities.javascriptEscape("\'"));
    assertEquals("\\\"",ImageMapUtilities.javascriptEscape("\""));
    assertEquals("\\\\",ImageMapUtilities.javascriptEscape("\\"));
  }
}
