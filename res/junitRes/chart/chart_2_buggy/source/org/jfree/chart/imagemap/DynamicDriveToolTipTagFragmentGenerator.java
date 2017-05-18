package org.jfree.chart.imagemap;
/** 
 * Generates tooltips using the Dynamic Drive DHTML Tip Message library (http://www.dynamicdrive.com).
 */
public class DynamicDriveToolTipTagFragmentGenerator implements ToolTipTagFragmentGenerator {
  /** 
 * The title, empty string not to display 
 */
  protected String title="";
  /** 
 * The style number 
 */
  protected int style=1;
  /** 
 * Blank constructor.
 */
  public DynamicDriveToolTipTagFragmentGenerator(){
    super();
  }
  /** 
 * Creates a new generator with specific title and style settings.
 * @param title  title for use in all tooltips, use empty String not todisplay a title.
 * @param style  style number, see http://www.dynamicdrive.com for moreinformation.
 */
  public DynamicDriveToolTipTagFragmentGenerator(  String title,  int style){
    this.title=title;
    this.style=style;
  }
  /** 
 * Generates a tooltip string to go in an HTML image map.
 * @param toolTipText  the tooltip.
 * @return The formatted HTML area tag attribute(s).
 */
  public String generateToolTipFragment(  String toolTipText){
    return " onMouseOver=\"return stm(['" + ImageMapUtilities.javascriptEscape(this.title) + "','"+ ImageMapUtilities.javascriptEscape(toolTipText)+ "'],Style["+ this.style+ "]);\""+ " onMouseOut=\"return htm();\"";
  }
}
