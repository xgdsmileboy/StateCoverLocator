package org.jfree.chart.util;
import java.awt.Stroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/** 
 * A table of      {@link Stroke} objects.
 */
public class StrokeList extends AbstractObjectList {
  /** 
 * Creates a new list.
 */
  public StrokeList(){
    super();
  }
  /** 
 * Returns a      {@link Stroke} object from the list.
 * @param index the index (zero-based).
 * @return The object.
 */
  public Stroke getStroke(  int index){
    return (Stroke)get(index);
  }
  /** 
 * Sets the      {@link Stroke} for an item in the list.  The list is expandedif necessary.
 * @param index  the index (zero-based).
 * @param stroke  the {@link Stroke}.
 */
  public void setStroke(  int index,  Stroke stroke){
    set(index,stroke);
  }
  /** 
 * Returns an independent copy of the list.
 * @return A clone.
 * @throws CloneNotSupportedException if an item in the list cannot be cloned.
 */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  /** 
 * Tests the list for equality with another object (typically also a list).
 * @param obj  the other object.
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof StrokeList) {
      return super.equals(obj);
    }
    return false;
  }
  /** 
 * Returns a hash code value for the object.
 * @return the hashcode
 */
  public int hashCode(){
    return super.hashCode();
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    int count=size();
    stream.writeInt(count);
    for (int i=0; i < count; i++) {
      Stroke stroke=getStroke(i);
      if (stroke != null) {
        stream.writeInt(i);
        SerialUtilities.writeStroke(stroke,stream);
      }
 else {
        stream.writeInt(-1);
      }
    }
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    int count=stream.readInt();
    for (int i=0; i < count; i++) {
      int index=stream.readInt();
      if (index != -1) {
        setStroke(index,SerialUtilities.readStroke(stream));
      }
    }
  }
}
