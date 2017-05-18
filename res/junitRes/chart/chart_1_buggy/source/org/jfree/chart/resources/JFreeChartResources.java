package org.jfree.chart.resources;
import java.util.ListResourceBundle;
/** 
 * Localised resources for JFreeChart.
 */
public class JFreeChartResources extends ListResourceBundle {
  /** 
 * Returns the array of strings in the resource bundle.
 * @return The resources.
 */
  public Object[][] getContents(){
    return CONTENTS;
  }
  /** 
 * The resources to be localised. 
 */
  private static final Object[][] CONTENTS={{"project.name","JFreeChart"},{"project.version","1.2.0-pre"},{"project.info","http://www.jfree.org/jfreechart/index.html"},{"project.copyright","(C)opyright 2000-2008, by Object Refinery Limited and Contributors"}};
}
