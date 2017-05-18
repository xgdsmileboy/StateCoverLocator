package org.jfree.chart.plot;
import java.util.HashMap;
import java.util.Map;
/** 
 * Records information about the state of a plot during the drawing process.
 */
public class PlotState {
  /** 
 * The shared axis states. 
 */
  private Map sharedAxisStates;
  /** 
 * Creates a new state object.
 */
  public PlotState(){
    this.sharedAxisStates=new HashMap();
  }
  /** 
 * Returns a map containing the shared axis states.
 * @return A map.
 */
  public Map getSharedAxisStates(){
    return this.sharedAxisStates;
  }
}
