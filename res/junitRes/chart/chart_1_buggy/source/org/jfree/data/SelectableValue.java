package org.jfree.data;
import java.io.Serializable;
/** 
 * A data structure for a numerical value along with selection status.
 * @since 1.2.0
 */
public class SelectableValue implements Value, Cloneable, Serializable {
  /** 
 * The value (<code>null</code> is permitted). 
 */
  private Number value;
  /** 
 * Is the item selected? 
 */
  private boolean selected;
  /** 
 * Creates a new instance with the specified value and the selection state set to <code>false</code>.
 * @param value  the value (<code>null</code> permitted).
 */
  public SelectableValue(  Number value){
    this(value,false);
  }
  /** 
 * Creates a new instance with the specified value and selection state.
 * @param value  the value (<code>null</code> permitted).
 * @param selected  the selection state.
 */
  public SelectableValue(  Number value,  boolean selected){
    this.value=value;
    this.selected=selected;
  }
  /** 
 * Returns the value for this data item.
 * @return The value (possibly <code>null</code>).
 */
  public Number getValue(){
    return this.value;
  }
  /** 
 * Returns the selection state.
 * @return The selection state.
 */
  public boolean isSelected(){
    return this.selected;
  }
  /** 
 * Sets the selection state.
 * @param selected  selected?
 */
  public void setSelected(  boolean selected){
    this.selected=selected;
  }
}
