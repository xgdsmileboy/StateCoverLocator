package org.jfree.chart.ui;
import java.awt.Font;
import java.util.ResourceBundle;
import javax.swing.JTextField;
import org.jfree.chart.util.ResourceBundleWrapper;
/** 
 * A field for displaying a font selection.  The display field itself is read-only, to the developer must provide another mechanism to allow the user to change the font.
 */
public class FontDisplayField extends JTextField {
  /** 
 * The current font. 
 */
  private Font displayFont;
  /** 
 * The resourceBundle for the localization. 
 */
  protected static final ResourceBundle localizationResources=ResourceBundleWrapper.getBundle("org.jfree.chart.ui.LocalizationBundle");
  /** 
 * Standard constructor - builds a FontDescriptionField initialised with the specified font.
 * @param font  the font.
 */
  public FontDisplayField(  Font font){
    super("");
    setDisplayFont(font);
    setEnabled(false);
  }
  /** 
 * Returns the current font.
 * @return the font.
 */
  public Font getDisplayFont(){
    return this.displayFont;
  }
  /** 
 * Sets the font.
 * @param font  the font.
 */
  public void setDisplayFont(  Font font){
    this.displayFont=font;
    setText(fontToString(this.displayFont));
  }
  /** 
 * Returns a string representation of the specified font.
 * @param font  the font.
 * @return a string describing the font.
 */
  private String fontToString(  Font font){
    if (font != null) {
      return font.getFontName() + ", " + font.getSize();
    }
 else {
      return localizationResources.getString("No_Font_Selected");
    }
  }
}
