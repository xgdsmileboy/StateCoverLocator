package org.jfree.data.general.junit;
/** 
 * A data holder for a value and an associated range or interval.  Used by the TestIntervalCategoryDataset class.
 */
public class IntervalDataItem {
  private Number value;
  private Number lowerBound;
  private Number upperBound;
  public IntervalDataItem(  Number value,  Number lowerBound,  Number upperBound){
    this.value=value;
    this.lowerBound=lowerBound;
    this.upperBound=upperBound;
  }
  public Number getLowerBound(){
    return lowerBound;
  }
  public Number getUpperBound(){
    return upperBound;
  }
  public Number getValue(){
    return value;
  }
}
