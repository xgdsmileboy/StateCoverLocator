package org.jfree.chart.imagemap;
/** 
 * Generates tooltips using the HTML title attribute for image map area tags.
 */
public class StandardToolTipTagFragmentGenerator implements ToolTipTagFragmentGenerator {
  /** 
 * Creates a new instance.
 */
  public StandardToolTipTagFragmentGenerator(){
    super();
  }
  /** 
 * Generates a tooltip string to go in an HTML image map.
 * @param toolTipText  the tooltip.
 * @return The formatted HTML area tag attribute(s).
 */
  public String generateToolTipFragment(  String toolTipText){
    return " title=\"" + ImageMapUtilities.htmlEscape(toolTipText) + "\" alt=\"\"";
  }
}
