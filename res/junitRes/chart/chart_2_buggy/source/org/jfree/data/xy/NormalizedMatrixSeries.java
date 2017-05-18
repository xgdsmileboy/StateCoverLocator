package org.jfree.data.xy;
/** 
 * Represents a dense normalized matrix M[i,j] where each Mij item of the matrix has a value (default is 0). When a matrix item is observed using <code>getItem</code> method, it is normalized, that is, divided by the total sum of all items. It can be also be scaled by setting a scale factor.
 */
public class NormalizedMatrixSeries extends MatrixSeries {
  /** 
 * The default scale factor. 
 */
  public static final double DEFAULT_SCALE_FACTOR=1.0;
  /** 
 * A factor that multiplies each item in this series when observed using getItem method.
 */
  private double m_scaleFactor=DEFAULT_SCALE_FACTOR;
  /** 
 * The sum of all items in this matrix 
 */
  private double m_totalSum;
  /** 
 * Constructor for NormalizedMatrixSeries.
 * @param name  the series name.
 * @param rows  the number of rows.
 * @param columns  the number of columns.
 */
  public NormalizedMatrixSeries(  String name,  int rows,  int columns){
    super(name,rows,columns);
    this.m_totalSum=Double.MIN_VALUE;
  }
  /** 
 * Returns an item.
 * @param itemIndex  the index.
 * @return The value.
 * @see org.jfree.data.xy.MatrixSeries#getItem(int)
 */
  public Number getItem(  int itemIndex){
    int i=getItemRow(itemIndex);
    int j=getItemColumn(itemIndex);
    double mij=get(i,j) * this.m_scaleFactor;
    Number n=new Double(mij / this.m_totalSum);
    return n;
  }
  /** 
 * Sets the factor that multiplies each item in this series when observed using getItem mehtod.
 * @param factor new factor to set.
 * @see #DEFAULT_SCALE_FACTOR
 */
  public void setScaleFactor(  double factor){
    this.m_scaleFactor=factor;
  }
  /** 
 * Returns the factor that multiplies each item in this series when observed using getItem mehtod.
 * @return The factor
 */
  public double getScaleFactor(){
    return this.m_scaleFactor;
  }
  /** 
 * Updates the value of the specified item in this matrix series.
 * @param i the row of the item.
 * @param j the column of the item.
 * @param mij the new value for the item.
 * @see #get(int,int)
 */
  public void update(  int i,  int j,  double mij){
    this.m_totalSum-=get(i,j);
    this.m_totalSum+=mij;
    super.update(i,j,mij);
  }
  /** 
 * @see org.jfree.data.xy.MatrixSeries#zeroAll()
 */
  public void zeroAll(){
    this.m_totalSum=0;
    super.zeroAll();
  }
}
