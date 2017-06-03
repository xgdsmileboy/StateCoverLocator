package org.jfree.data.xy;
import java.io.Serializable;
import org.jfree.data.general.Series;
/** 
 * Represents a dense matrix M[i,j] where each Mij item of the matrix has a value (default is 0).
 */
public class MatrixSeries extends Series implements Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7934188527308315704L;
  /** 
 * Series matrix values 
 */
  protected double[][] data;
  /** 
 * Constructs a new matrix series. <p> By default, all matrix items are initialzed to 0. </p>
 * @param name  series name (<code>null</code> not permitted).
 * @param rows  the number of rows.
 * @param columns  the number of columns.
 */
  public MatrixSeries(  String name,  int rows,  int columns){
    super(name);
    this.data=new double[rows][columns];
    zeroAll();
  }
  /** 
 * Returns the number of columns in this matrix series.
 * @return The number of columns in this matrix series.
 */
  public int getColumnsCount(){
    return this.data[0].length;
  }
  /** 
 * Return the matrix item at the specified index.  Note that this method creates a new <code>Double</code> instance every time it is called.
 * @param itemIndex item index.
 * @return The matrix item at the specified index.
 * @see #get(int,int)
 */
  public Number getItem(  int itemIndex){
    int i=getItemRow(itemIndex);
    int j=getItemColumn(itemIndex);
    Number n=new Double(get(i,j));
    return n;
  }
  /** 
 * Returns the column of the specified item.
 * @param itemIndex the index of the item.
 * @return The column of the specified item.
 */
  public int getItemColumn(  int itemIndex){
    return itemIndex % getColumnsCount();
  }
  /** 
 * Returns the number of items in the series.
 * @return The item count.
 */
  public int getItemCount(){
    return getRowCount() * getColumnsCount();
  }
  /** 
 * Returns the row of the specified item.
 * @param itemIndex the index of the item.
 * @return The row of the specified item.
 */
  public int getItemRow(  int itemIndex){
    return itemIndex / getColumnsCount();
  }
  /** 
 * Returns the number of rows in this matrix series.
 * @return The number of rows in this matrix series.
 */
  public int getRowCount(){
    return this.data.length;
  }
  /** 
 * Returns the value of the specified item in this matrix series.
 * @param i the row of the item.
 * @param j the column of the item.
 * @return The value of the specified item in this matrix series.
 * @see #getItem(int)
 * @see #update(int,int,double)
 */
  public double get(  int i,  int j){
    return this.data[i][j];
  }
  /** 
 * Updates the value of the specified item in this matrix series.
 * @param i the row of the item.
 * @param j the column of the item.
 * @param mij the new value for the item.
 * @see #get(int,int)
 */
  public void update(  int i,  int j,  double mij){
    this.data[i][j]=mij;
    fireSeriesChanged();
  }
  /** 
 * Sets all matrix values to zero and sends a                                                                                                                                                                   {@link org.jfree.data.general.SeriesChangeEvent} to all registeredlisteners.
 */
  public void zeroAll(){
    int rows=getRowCount();
    int columns=getColumnsCount();
    for (int row=0; row < rows; row++) {
      for (int column=0; column < columns; column++) {
        this.data[row][column]=0.0;
      }
    }
    fireSeriesChanged();
  }
  /** 
 * Tests this object instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MatrixSeries)) {
      return false;
    }
    MatrixSeries that=(MatrixSeries)obj;
    if (!(getRowCount() == that.getRowCount())) {
      return false;
    }
    if (!(getColumnsCount() == that.getColumnsCount())) {
      return false;
    }
    for (int r=0; r < getRowCount(); r++) {
      for (int c=0; c < getColumnsCount(); c++) {
        if (get(r,c) != that.get(r,c)) {
          return false;
        }
      }
    }
    return super.equals(obj);
  }
}
