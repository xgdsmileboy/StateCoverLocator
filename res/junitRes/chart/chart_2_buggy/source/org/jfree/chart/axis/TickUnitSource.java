package org.jfree.chart.axis;
/** 
 * An interface used by the                                                                                                                                                                     {@link DateAxis} and {@link NumberAxis} classes toobtain a suitable  {@link TickUnit}.
 */
public interface TickUnitSource {
  /** 
 * Returns a tick unit that is larger than the supplied unit.
 * @param unit   the unit.
 * @return A tick unit that is larger than the supplied unit.
 */
  public TickUnit getLargerTickUnit(  TickUnit unit);
  /** 
 * Returns the tick unit in the collection that is greater than or equal to (in size) the specified unit.
 * @param unit  the unit.
 * @return A unit from the collection.
 */
  public TickUnit getCeilingTickUnit(  TickUnit unit);
  /** 
 * Returns the tick unit in the collection that is greater than or equal to the specified size.
 * @param size  the size.
 * @return A unit from the collection.
 */
  public TickUnit getCeilingTickUnit(  double size);
}
