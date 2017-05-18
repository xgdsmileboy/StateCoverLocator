package org.jfree.chart.util;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/** 
 * Utility code that relates to <code>Paint</code> objects.
 */
public class PaintUtilities {
  /** 
 * Private constructor prevents object creation.
 */
  private PaintUtilities(){
  }
  /** 
 * Returns <code>true</code> if the two <code>Paint</code> objects are equal OR both <code>null</code>.  This method handles <code>GradientPaint</code> as a special case.
 * @param p1  paint 1 (<code>null</code> permitted).
 * @param p2  paint 2 (<code>null</code> permitted).
 * @return A boolean.
 */
  public static boolean equal(  Paint p1,  Paint p2){
    if (p1 == null) {
      return (p2 == null);
    }
    if (p2 == null) {
      return false;
    }
    boolean result=false;
    if (p1 instanceof GradientPaint && p2 instanceof GradientPaint) {
      GradientPaint gp1=(GradientPaint)p1;
      GradientPaint gp2=(GradientPaint)p2;
      result=gp1.getColor1().equals(gp2.getColor1()) && gp1.getColor2().equals(gp2.getColor2()) && gp1.getPoint1().equals(gp2.getPoint1())&& gp1.getPoint2().equals(gp2.getPoint2())&& gp1.isCyclic() == gp2.isCyclic() && gp1.getTransparency() == gp1.getTransparency();
    }
 else {
      result=p1.equals(p2);
    }
    return result;
  }
  /** 
 * Converts a color into a string. If the color is equal to one of the defined constant colors, that name is returned instead. Otherwise the color is returned as hex-string.
 * @param c the color.
 * @return the string for this color.
 */
  public static String colorToString(  Color c){
    try {
      Field[] fields=Color.class.getFields();
      for (int i=0; i < fields.length; i++) {
        Field f=fields[i];
        if (Modifier.isPublic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
          String name=f.getName();
          Object oColor=f.get(null);
          if (oColor instanceof Color) {
            if (c.equals(oColor)) {
              return name;
            }
          }
        }
      }
    }
 catch (    Exception e) {
    }
    String color=Integer.toHexString(c.getRGB() & 0x00ffffff);
    StringBuffer retval=new StringBuffer(7);
    retval.append("#");
    int fillUp=6 - color.length();
    for (int i=0; i < fillUp; i++) {
      retval.append("0");
    }
    retval.append(color);
    return retval.toString();
  }
  /** 
 * Converts a given string into a color.
 * @param value the string, either a name or a hex-string.
 * @return the color.
 */
  public static Color stringToColor(  String value){
    if (value == null) {
      return Color.black;
    }
    try {
      return Color.decode(value);
    }
 catch (    NumberFormatException nfe) {
      try {
        final Field f=Color.class.getField(value);
        return (Color)f.get(null);
      }
 catch (      Exception ce) {
        return Color.black;
      }
    }
  }
}
