package org.jfree.chart.event;
import java.util.EventListener;
import org.jfree.chart.annotations.Annotation;
/** 
 * The interface that must be supported by classes that wish to receive notification of changes to an                               {@link Annotation}.
 * @since 1.0.14
 */
public interface AnnotationChangeListener extends EventListener {
  /** 
 * Receives notification of an annotation change event.
 * @param event  the event.
 * @since 1.0.14
 */
  public void annotationChanged(  AnnotationChangeEvent event);
}
