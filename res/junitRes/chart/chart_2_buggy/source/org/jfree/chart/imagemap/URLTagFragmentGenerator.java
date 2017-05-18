package org.jfree.chart.imagemap;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.chart.urls.XYZURLGenerator;
/** 
 * Interface for generating the URL fragment of an HTML image map area tag.
 */
public interface URLTagFragmentGenerator {
  /** 
 * Generates a URL string to go in an HTML image map. <br><br> Note that the <code>urlText</code> will be created by a URL generator (such as                                                                                               {@link CategoryURLGenerator},                                                                                               {@link PieURLGenerator},                                                                                              {@link XYURLGenerator} or {@link XYZURLGenerator}) and that generator is responsible for ensuring that the URL text is correctly escaped.
 * @param urlText  the URL text (fully escaped).
 * @return The formatted HTML area tag attribute(s).
 */
  public String generateURLFragment(  String urlText);
}
