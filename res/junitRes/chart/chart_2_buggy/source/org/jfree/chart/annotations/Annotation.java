package org.jfree.chart.annotations;
import org.jfree.chart.event.AnnotationChangeListener;
/** 
 * The base interface for annotations.  All annotations should support the                                                                                              {@link AnnotationChangeEvent} mechanism by allowing listeners to registerand receive notification of any changes to the annotation.
 */
public interface Annotation {
  /** 
 * Registers an object for notification of changes to the annotation.
 * @param listener  the object to register.
 */
  public void addChangeListener(  AnnotationChangeListener listener);
  /** 
 * Deregisters an object for notification of changes to the annotation.
 * @param listener  the object to deregister.
 */
  public void removeChangeListener(  AnnotationChangeListener listener);
}
