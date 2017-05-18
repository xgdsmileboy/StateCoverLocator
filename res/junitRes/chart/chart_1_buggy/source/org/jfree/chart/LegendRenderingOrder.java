package org.jfree.chart;
import java.io.ObjectStreamException;
import java.io.Serializable;
/** 
 * Represents the order for rendering legend items.
 */
public final class LegendRenderingOrder implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-3832486612685808616L;
  /** 
 * In order. 
 */
  public static final LegendRenderingOrder STANDARD=new LegendRenderingOrder("LegendRenderingOrder.STANDARD");
  /** 
 * In reverse order. 
 */
  public static final LegendRenderingOrder REVERSE=new LegendRenderingOrder("LegendRenderingOrder.REVERSE");
  /** 
 * The name. 
 */
  private String name;
  /** 
 * Private constructor.
 * @param name  the name.
 */
  private LegendRenderingOrder(  String name){
    this.name=name;
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
    if (!(obj instanceof LegendRenderingOrder)) {
      return false;
    }
    LegendRenderingOrder order=(LegendRenderingOrder)obj;
    if (!this.name.equals(order.toString())) {
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
    if (this.equals(LegendRenderingOrder.STANDARD)) {
      return LegendRenderingOrder.STANDARD;
    }
 else {
      if (this.equals(LegendRenderingOrder.REVERSE)) {
        return LegendRenderingOrder.REVERSE;
      }
    }
    return null;
  }
}
