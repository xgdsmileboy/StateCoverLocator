package org.jfree.chart;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.Serializable;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.Zoomable;
/** 
 * A class that handles mouse wheel events for the                                                                                                                                                                     {@link ChartPanel} class.
 * @since 1.0.13
 */
class MouseWheelHandler implements MouseWheelListener, Serializable {
  /** 
 * The chart panel. 
 */
  private ChartPanel chartPanel;
  /** 
 * The zoom factor. 
 */
  double zoomFactor;
  /** 
 * Creates a new instance for the specified chart panel.
 * @param chartPanel  the chart panel (<code>null</code> not permitted).
 */
  public MouseWheelHandler(  ChartPanel chartPanel){
    this.chartPanel=chartPanel;
    this.zoomFactor=0.10;
    this.chartPanel.addMouseWheelListener(this);
  }
  /** 
 * Returns the current zoom factor.  The default value is 0.10 (ten percent).
 * @return The zoom factor.
 * @see #setZoomFactor(double)
 */
  public double getZoomFactor(){
    return this.zoomFactor;
  }
  /** 
 * Sets the zoom factor.
 * @param zoomFactor  the zoom factor.
 * @see #getZoomFactor()
 */
  public void setZoomFactor(  double zoomFactor){
    this.zoomFactor=zoomFactor;
  }
  /** 
 * Handles a mouse wheel event from the underlying chart panel.
 * @param e  the event.
 */
  public void mouseWheelMoved(  MouseWheelEvent e){
    JFreeChart chart=this.chartPanel.getChart();
    if (chart == null) {
      return;
    }
    Plot plot=chart.getPlot();
    if (plot instanceof Zoomable) {
      Zoomable zoomable=(Zoomable)plot;
      handleZoomable(zoomable,e);
    }
  }
  /** 
 * Handle the case where a plot implements the                                                                                                                                                                     {@link Zoomable} interface.
 * @param zoomable  the zoomable plot.
 * @param e  the mouse wheel event.
 */
  private void handleZoomable(  Zoomable zoomable,  MouseWheelEvent e){
    ChartRenderingInfo info=this.chartPanel.getChartRenderingInfo();
    PlotRenderingInfo pinfo=info.getPlotInfo();
    Point2D p=this.chartPanel.translateScreenToJava2D(e.getPoint());
    if (!pinfo.getDataArea().contains(p)) {
      return;
    }
    Plot plot=(Plot)zoomable;
    boolean notifyState=plot.isNotify();
    plot.setNotify(false);
    int clicks=e.getWheelRotation();
    double zf=1.0 + this.zoomFactor;
    if (clicks < 0) {
      zf=1.0 / zf;
    }
    if (chartPanel.isDomainZoomable()) {
      zoomable.zoomDomainAxes(zf,pinfo,p,true);
    }
    if (chartPanel.isRangeZoomable()) {
      zoomable.zoomRangeAxes(zf,pinfo,p,true);
    }
    plot.setNotify(notifyState);
  }
}
