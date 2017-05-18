package org.jfree.chart;
import java.awt.Color;
import java.awt.Paint;
/** 
 * Class to extend the number of Colors available to the charts. This extends the java.awt.Color object and extends the number of final Colors publically accessible.
 */
public class ChartColor extends Color {
  /** 
 * A very dark red color. 
 */
  public static final Color VERY_DARK_RED=new Color(0x80,0x00,0x00);
  /** 
 * A dark red color. 
 */
  public static final Color DARK_RED=new Color(0xc0,0x00,0x00);
  /** 
 * A light red color. 
 */
  public static final Color LIGHT_RED=new Color(0xFF,0x40,0x40);
  /** 
 * A very light red color. 
 */
  public static final Color VERY_LIGHT_RED=new Color(0xFF,0x80,0x80);
  /** 
 * A very dark yellow color. 
 */
  public static final Color VERY_DARK_YELLOW=new Color(0x80,0x80,0x00);
  /** 
 * A dark yellow color. 
 */
  public static final Color DARK_YELLOW=new Color(0xC0,0xC0,0x00);
  /** 
 * A light yellow color. 
 */
  public static final Color LIGHT_YELLOW=new Color(0xFF,0xFF,0x40);
  /** 
 * A very light yellow color. 
 */
  public static final Color VERY_LIGHT_YELLOW=new Color(0xFF,0xFF,0x80);
  /** 
 * A very dark green color. 
 */
  public static final Color VERY_DARK_GREEN=new Color(0x00,0x80,0x00);
  /** 
 * A dark green color. 
 */
  public static final Color DARK_GREEN=new Color(0x00,0xC0,0x00);
  /** 
 * A light green color. 
 */
  public static final Color LIGHT_GREEN=new Color(0x40,0xFF,0x40);
  /** 
 * A very light green color. 
 */
  public static final Color VERY_LIGHT_GREEN=new Color(0x80,0xFF,0x80);
  /** 
 * A very dark cyan color. 
 */
  public static final Color VERY_DARK_CYAN=new Color(0x00,0x80,0x80);
  /** 
 * A dark cyan color. 
 */
  public static final Color DARK_CYAN=new Color(0x00,0xC0,0xC0);
  /** 
 * A light cyan color. 
 */
  public static final Color LIGHT_CYAN=new Color(0x40,0xFF,0xFF);
  /** 
 * Aa very light cyan color. 
 */
  public static final Color VERY_LIGHT_CYAN=new Color(0x80,0xFF,0xFF);
  /** 
 * A very dark blue color. 
 */
  public static final Color VERY_DARK_BLUE=new Color(0x00,0x00,0x80);
  /** 
 * A dark blue color. 
 */
  public static final Color DARK_BLUE=new Color(0x00,0x00,0xC0);
  /** 
 * A light blue color. 
 */
  public static final Color LIGHT_BLUE=new Color(0x40,0x40,0xFF);
  /** 
 * A very light blue color. 
 */
  public static final Color VERY_LIGHT_BLUE=new Color(0x80,0x80,0xFF);
  /** 
 * A very dark magenta/purple color. 
 */
  public static final Color VERY_DARK_MAGENTA=new Color(0x80,0x00,0x80);
  /** 
 * A dark magenta color. 
 */
  public static final Color DARK_MAGENTA=new Color(0xC0,0x00,0xC0);
  /** 
 * A light magenta color. 
 */
  public static final Color LIGHT_MAGENTA=new Color(0xFF,0x40,0xFF);
  /** 
 * A very light magenta color. 
 */
  public static final Color VERY_LIGHT_MAGENTA=new Color(0xFF,0x80,0xFF);
  /** 
 * Creates a Color with an opaque sRGB with red, green and blue values in range 0-255.
 * @param r  the red component in range 0x00-0xFF.
 * @param g  the green component in range 0x00-0xFF.
 * @param b  the blue component in range 0x00-0xFF.
 */
  public ChartColor(  int r,  int g,  int b){
    super(r,g,b);
  }
  /** 
 * Convenience method to return an array of <code>Paint</code> objects that represent the pre-defined colors in the <code>Color<code> and <code>ChartColor</code> objects.
 * @return An array of objects with the <code>Paint</code> interface.
 */
  public static Paint[] createDefaultPaintArray(){
    return new Paint[]{new Color(0xFF,0x55,0x55),new Color(0x55,0x55,0xFF),new Color(0x55,0xFF,0x55),new Color(0xFF,0xFF,0x55),new Color(0xFF,0x55,0xFF),new Color(0x55,0xFF,0xFF),Color.pink,Color.gray,ChartColor.DARK_RED,ChartColor.DARK_BLUE,ChartColor.DARK_GREEN,ChartColor.DARK_YELLOW,ChartColor.DARK_MAGENTA,ChartColor.DARK_CYAN,Color.darkGray,ChartColor.LIGHT_RED,ChartColor.LIGHT_BLUE,ChartColor.LIGHT_GREEN,ChartColor.LIGHT_YELLOW,ChartColor.LIGHT_MAGENTA,ChartColor.LIGHT_CYAN,Color.lightGray,ChartColor.VERY_DARK_RED,ChartColor.VERY_DARK_BLUE,ChartColor.VERY_DARK_GREEN,ChartColor.VERY_DARK_YELLOW,ChartColor.VERY_DARK_MAGENTA,ChartColor.VERY_DARK_CYAN,ChartColor.VERY_LIGHT_RED,ChartColor.VERY_LIGHT_BLUE,ChartColor.VERY_LIGHT_GREEN,ChartColor.VERY_LIGHT_YELLOW,ChartColor.VERY_LIGHT_MAGENTA,ChartColor.VERY_LIGHT_CYAN};
  }
}
