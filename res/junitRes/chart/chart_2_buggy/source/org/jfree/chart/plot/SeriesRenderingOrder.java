package org.jfree.chart.plot;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Defines the tokens that indicate the rendering order for series in a                                                                                              {@link org.jfree.chart.plot.XYPlot}.
 */
public final class SeriesRenderingOrder implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=209336477448807735L;
  /** 
 * Render series in the order 0, 1, 2, ..., N-1, where N is the number of series.
 */
  public static final SeriesRenderingOrder FORWARD=new SeriesRenderingOrder("SeriesRenderingOrder.FORWARD");
  /** 
 * Render series in the order N-1, N-2, ..., 2, 1, 0, where N is the number of series.
 */
  public static final SeriesRenderingOrder REVERSE=new SeriesRenderingOrder("SeriesRenderingOrder.REVERSE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private SeriesRenderingOrder(  String name){
    this.name=name;
  }
  /** 
 * Returns a string representing the object.
 * @return The string (never <code>null</code>).
 */
  public String toString(){
    return this.name;
  }
  /** 
 * Returns <code>true</code> if this object is equal to the specified object, and <code>false</code> otherwise.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SeriesRenderingOrder)) {
      return false;
    }
    SeriesRenderingOrder order=(SeriesRenderingOrder)obj;
    if (!this.name.equals(order.toString())) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    return this.name.hashCode();
  }
  /** 
 * Ensures that serialization returns the unique instances.
 * @return The object.
 * @throws ObjectStreamException if there is a problem.
 */
  private Object readResolve() throws ObjectStreamException {
    if (this.equals(SeriesRenderingOrder.FORWARD)) {
      return SeriesRenderingOrder.FORWARD;
    }
 else {
      if (this.equals(SeriesRenderingOrder.REVERSE)) {
        return SeriesRenderingOrder.REVERSE;
      }
    }
    return null;
  }
}
