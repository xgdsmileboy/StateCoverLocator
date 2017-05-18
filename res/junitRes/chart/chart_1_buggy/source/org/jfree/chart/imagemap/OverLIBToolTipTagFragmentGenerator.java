package org.jfree.chart.imagemap;
/** 
 * Generates tooltips using the OverLIB library (http://www.bosrup.com/web/overlib/).
 */
public class OverLIBToolTipTagFragmentGenerator implements ToolTipTagFragmentGenerator {
  /** 
 * Creates a new instance.
 */
  public OverLIBToolTipTagFragmentGenerator(){
    super();
  }
  /** 
 * Generates a tooltip string to go in an HTML image map.
 * @param toolTipText  the tooltip text.
 * @return The formatted HTML area tag attribute(s).
 */
  public String generateToolTipFragment(  String toolTipText){
    return " onMouseOver=\"return overlib('" + ImageMapUtilities.javascriptEscape(toolTipText) + "');\" onMouseOut=\"return nd();\"";
  }
}
