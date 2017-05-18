package org.jfree.chart.axis;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Calendar;
/** 
 * An enumeration of the unit types for a                                                                                               {@link DateTickUnit} instance.
 * @since 1.0.13
 */
public class DateTickUnitType implements Serializable {
  /** 
 * Year. 
 */
  public static final DateTickUnitType YEAR=new DateTickUnitType("DateTickUnitType.YEAR",Calendar.YEAR);
  /** 
 * Month. 
 */
  public static final DateTickUnitType MONTH=new DateTickUnitType("DateTickUnitType.MONTH",Calendar.MONTH);
  /** 
 * Day. 
 */
  public static final DateTickUnitType DAY=new DateTickUnitType("DateTickUnitType.DAY",Calendar.DATE);
  /** 
 * Hour. 
 */
  public static final DateTickUnitType HOUR=new DateTickUnitType("DateTickUnitType.HOUR",Calendar.HOUR_OF_DAY);
  /** 
 * Minute. 
 */
  public static final DateTickUnitType MINUTE=new DateTickUnitType("DateTickUnitType.MINUTE",Calendar.MINUTE);
  /** 
 * Second. 
 */
  public static final DateTickUnitType SECOND=new DateTickUnitType("DateTickUnitType.SECOND",Calendar.SECOND);
  /** 
 * Millisecond. 
 */
  public static final DateTickUnitType MILLISECOND=new DateTickUnitType("DateTickUnitType.MILLISECOND",Calendar.MILLISECOND);
  /** 
 * The name. 
 */
  private String name;
  /** 
 * The corresponding field value in Java's Calendar class. 
 */
  private int calendarField;
  /** 
 * Private constructor.
 * @param name  the name.
 * @param calendarField  the calendar field.
 */
  private DateTickUnitType(  String name,  int calendarField){
    this.name=name;
    this.calendarField=calendarField;
  }
  /** 
 * Returns the calendar field.
 * @return The calendar field.
 */
  public int getCalendarField(){
    return this.calendarField;
  }
  /** 
 * Returns a string representing the object.
 * @return The string.
 */
  public String toString(){
    return this.name;
  }
  /** 
 * Returns <code>true</code> if this object is equal to the specified object, and <code>false</code> otherwise.
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DateTickUnitType)) {
      return false;
    }
    DateTickUnitType t=(DateTickUnitType)obj;
    if (!this.name.equals(t.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Ensures that serialization returns the unique instances.
 * @return The object.
 * @throws ObjectStreamException if there is a problem.
 */
  private Object readResolve() throws ObjectStreamException {
    if (this.equals(DateTickUnitType.YEAR)) {
      return DateTickUnitType.YEAR;
    }
 else {
      if (this.equals(DateTickUnitType.MONTH)) {
        return DateTickUnitType.MONTH;
      }
 else {
        if (this.equals(DateTickUnitType.DAY)) {
          return DateTickUnitType.DAY;
        }
 else {
          if (this.equals(DateTickUnitType.HOUR)) {
            return DateTickUnitType.HOUR;
          }
 else {
            if (this.equals(DateTickUnitType.MINUTE)) {
              return DateTickUnitType.MINUTE;
            }
 else {
              if (this.equals(DateTickUnitType.SECOND)) {
                return DateTickUnitType.SECOND;
              }
 else {
                if (this.equals(DateTickUnitType.MILLISECOND)) {
                  return DateTickUnitType.MILLISECOND;
                }
              }
            }
          }
        }
      }
    }
    return null;
  }
}
