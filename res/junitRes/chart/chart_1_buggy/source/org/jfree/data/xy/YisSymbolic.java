package org.jfree.data.xy;
/** 
 * Represent a data set where Y is a symbolic values. Each symbolic value is linked with an Integer.
 */
public interface YisSymbolic {
  /** 
 * Returns the list of symbolic values.
 * @return The symbolic values.
 */
  public String[] getYSymbolicValues();
  /** 
 * Returns the symbolic value of the data set specified by <CODE>series</CODE> and <CODE>item</CODE> parameters.
 * @param series  the series index (zero-based).
 * @param item  the item index (zero-based).
 * @return The symbolic value.
 */
  public String getYSymbolicValue(  int series,  int item);
  /** 
 * Returns the symbolic value linked with the specified <CODE>Integer</CODE>.
 * @param val  value of the integer linked with the symbolic value.
 * @return The symbolic value.
 */
  public String getYSymbolicValue(  Integer val);
}
