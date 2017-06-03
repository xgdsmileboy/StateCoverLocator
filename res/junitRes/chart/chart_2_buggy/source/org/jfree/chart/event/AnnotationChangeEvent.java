package org.jfree.chart.event;
import org.jfree.chart.annotations.Annotation;
/** 
 * An event that can be forwarded to any                                                                                                                                                                     {@link AnnotationChangeListener} tosignal a change to an  {@link Annotation}.
 * @since 1.0.14
 */
public class AnnotationChangeEvent extends ChartChangeEvent {
  /** 
 * The plot that generated the event. 
 */
  private Annotation annotation;
  /** 
 * Creates a new <code>AnnotationChangeEvent</code> instance.
 * @param annotation  the annotation that triggered the event(<code>null</code> not permitted).
 * @since 1.0.14
 */
  public AnnotationChangeEvent(  Object source,  Annotation annotation){
    super(source);
    this.annotation=annotation;
  }
  /** 
 * Returns the annotation that triggered the event.
 * @return The annotation that triggered the event (never <code>null</code>).
 * @since 1.0.14
 */
  public Annotation getAnnotation(){
    return this.annotation;
  }
}
