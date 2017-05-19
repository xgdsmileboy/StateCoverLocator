package org.jfree.chart.axis;
import java.util.Date;
/** 
 * An interface that defines the contract for a Timeline. <P> A Timeline will present a series of values to be used for an axis. Each Timeline must provide transformation methods between domain values and timeline values. In theory many transformations are possible. This interface has been implemented completely in                              {@link org.jfree.chart.axis.SegmentedTimeline}. <P> A timeline can be used as parameter to a                              {@link org.jfree.chart.axis.DateAxis} to define the values that this axissupports. As an example, the  {@link org.jfree.chart.axis.SegmentedTimeline}implements a timeline formed by segments of equal length (ex. days, hours, minutes) where some segments can be included in the timeline and others excluded. Therefore timelines like "working days" or "working hours" can be created where non-working days or non-working hours respectively can be removed from the timeline, and therefore from the axis. This creates a smooth plot with equal separation between all included segments. <P> Because Timelines were created mainly for Date related axis, values are represented as longs instead of doubles. In this case, the domain value is just the number of milliseconds since January 1, 1970, 00:00:00 GMT as defined by the getTime() method of                               {@link java.util.Date}.
 * @see org.jfree.chart.axis.SegmentedTimeline
 * @see org.jfree.chart.axis.DateAxis
 */
public interface Timeline {
  /** 
 * Translates a millisecond (as defined by java.util.Date) into an index along this timeline.
 * @param millisecond  the millisecond.
 * @return A timeline value.
 */
  long toTimelineValue(  long millisecond);
  /** 
 * Translates a date into a value on this timeline.
 * @param date  the date.
 * @return A timeline value
 */
  long toTimelineValue(  Date date);
  /** 
 * Translates a value relative to this timeline into a domain value. The domain value obtained by this method is not always the same domain value that could have been supplied to translateDomainValueToTimelineValue(domainValue). This is because the original tranformation may not be complete reversable.
 * @see org.jfree.chart.axis.SegmentedTimeline
 * @param timelineValue  a timeline value.
 * @return A domain value.
 */
  long toMillisecond(  long timelineValue);
  /** 
 * Returns <code>true</code> if a value is contained in the timeline values.
 * @param millisecond  the millisecond.
 * @return <code>true</code> if value is contained in the timeline and<code>false</code> otherwise.
 */
  boolean containsDomainValue(  long millisecond);
  /** 
 * Returns <code>true</code> if a date is contained in the timeline values.
 * @param date  the date to verify.
 * @return <code>true</code> if value is contained in the timeline and<code>false</code>  otherwise.
 */
  boolean containsDomainValue(  Date date);
  /** 
 * Returns <code>true</code> if a range of values are contained in the timeline.
 * @param fromMillisecond  the start of the range to verify.
 * @param toMillisecond  the end of the range to verify.
 * @return <code>true</code> if the range is contained in the timeline or<code>false</code> otherwise
 */
  boolean containsDomainRange(  long fromMillisecond,  long toMillisecond);
  /** 
 * Returns <code>true</code> if a range of dates are contained in the timeline.
 * @param fromDate  the start of the range to verify.
 * @param toDate  the end of the range to verify.
 * @return <code>true</code> if the range is contained in the timeline or<code>false</code> otherwise
 */
  boolean containsDomainRange(  Date fromDate,  Date toDate);
}
