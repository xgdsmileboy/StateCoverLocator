package org.jfree.chart.imagemap;
/** 
 * Generates URLs using the HTML href attribute for image map area tags.
 */
public class StandardURLTagFragmentGenerator implements URLTagFragmentGenerator {
  /** 
 * Creates a new instance.
 */
  public StandardURLTagFragmentGenerator(){
    super();
  }
  /** 
 * Generates a URL string to go in an HTML image map.
 * @param urlText  the URL text (fully escaped).
 * @return The formatted text
 */
  public String generateURLFragment(  String urlText){
    return " href=\"" + urlText + "\"";
  }
}
