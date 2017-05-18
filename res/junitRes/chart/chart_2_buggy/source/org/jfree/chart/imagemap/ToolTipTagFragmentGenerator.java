package org.jfree.chart.imagemap;
/** 
 * Interface for generating the tooltip fragment of an HTML image map area tag. The fragment should be <code>XHTML 1.0</code> compliant.
 */
public interface ToolTipTagFragmentGenerator {
  /** 
 * Generates a tooltip string to go in an HTML image map.  To allow for varying standards compliance among browsers, this method is expected to return an 'alt' attribute IN ADDITION TO whatever it does to create the tooltip (often a 'title' attribute). <br><br> Note that the <code>toolTipText</code> may have been generated from user-defined data, so care should be taken to filter/escape any characters that may corrupt the HTML tag.
 * @param toolTipText  the tooltip.
 * @return The formatted HTML area tag attribute(s).
 */
  public String generateToolTipFragment(  String toolTipText);
}
