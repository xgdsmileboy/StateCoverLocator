package org.jfree.chart.panel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.util.ShapeUtilities;
/** 
 * A mouse handler than performs a zooming operation on a ChartPanel.
 */
public class ZoomHandler extends AbstractMouseHandler {
  private Point2D zoomPoint;
  private Rectangle2D zoomRectangle;
  /** 
 * Creates a new instance.
 */
  public ZoomHandler(){
    super();
  }
  public void mousePressed(  MouseEvent e){
    ChartPanel chartPanel=(ChartPanel)e.getSource();
    Rectangle2D screenDataArea=chartPanel.getScreenDataArea(e.getX(),e.getY());
    if (screenDataArea != null) {
      this.zoomPoint=ShapeUtilities.getPointInRectangle(e.getX(),e.getY(),screenDataArea);
    }
 else {
      this.zoomPoint=null;
    }
  }
  public void mouseDragged(  MouseEvent e){
    if (this.zoomPoint == null) {
      return;
    }
    ChartPanel panel=(ChartPanel)e.getSource();
    Graphics2D g2=(Graphics2D)panel.getGraphics();
    if (!panel.getUseBuffer()) {
      drawZoomRectangle(panel,g2,true);
    }
    boolean hZoom=false;
    boolean vZoom=false;
    if (panel.getOrientation() == PlotOrientation.HORIZONTAL) {
      hZoom=panel.isRangeZoomable();
      vZoom=panel.isDomainZoomable();
    }
 else {
      hZoom=panel.isDomainZoomable();
      vZoom=panel.isRangeZoomable();
    }
    Rectangle2D scaledDataArea=panel.getScreenDataArea((int)this.zoomPoint.getX(),(int)this.zoomPoint.getY());
    if (hZoom && vZoom) {
      double xmax=Math.min(e.getX(),scaledDataArea.getMaxX());
      double ymax=Math.min(e.getY(),scaledDataArea.getMaxY());
      this.zoomRectangle=new Rectangle2D.Double(this.zoomPoint.getX(),this.zoomPoint.getY(),xmax - this.zoomPoint.getX(),ymax - this.zoomPoint.getY());
    }
 else {
      if (hZoom) {
        double xmax=Math.min(e.getX(),scaledDataArea.getMaxX());
        this.zoomRectangle=new Rectangle2D.Double(this.zoomPoint.getX(),scaledDataArea.getMinY(),xmax - this.zoomPoint.getX(),scaledDataArea.getHeight());
      }
 else {
        if (vZoom) {
          double ymax=Math.min(e.getY(),scaledDataArea.getMaxY());
          this.zoomRectangle=new Rectangle2D.Double(scaledDataArea.getMinX(),this.zoomPoint.getY(),scaledDataArea.getWidth(),ymax - this.zoomPoint.getY());
        }
      }
    }
    panel.setZoomRectangle(this.zoomRectangle);
    if (panel.getUseBuffer()) {
      panel.repaint();
    }
 else {
      drawZoomRectangle(panel,g2,true);
    }
    g2.dispose();
  }
  public void mouseReleased(  MouseEvent e){
    if (this.zoomRectangle == null) {
      return;
    }
    ChartPanel panel=(ChartPanel)e.getSource();
    boolean hZoom=false;
    boolean vZoom=false;
    if (panel.getOrientation() == PlotOrientation.HORIZONTAL) {
      hZoom=panel.isRangeZoomable();
      vZoom=panel.isDomainZoomable();
    }
 else {
      hZoom=panel.isDomainZoomable();
      vZoom=panel.isRangeZoomable();
    }
    boolean zoomTrigger1=hZoom && Math.abs(e.getX() - this.zoomPoint.getX()) >= panel.getZoomTriggerDistance();
    boolean zoomTrigger2=vZoom && Math.abs(e.getY() - this.zoomPoint.getY()) >= panel.getZoomTriggerDistance();
    if (zoomTrigger1 || zoomTrigger2) {
      if ((hZoom && (e.getX() < this.zoomPoint.getX())) || (vZoom && (e.getY() < this.zoomPoint.getY()))) {
        panel.restoreAutoBounds();
      }
 else {
        double x, y, w, h;
        Rectangle2D screenDataArea=panel.getScreenDataArea((int)this.zoomPoint.getX(),(int)this.zoomPoint.getY());
        double maxX=screenDataArea.getMaxX();
        double maxY=screenDataArea.getMaxY();
        if (!vZoom) {
          x=this.zoomPoint.getX();
          y=screenDataArea.getMinY();
          w=Math.min(this.zoomRectangle.getWidth(),maxX - this.zoomPoint.getX());
          h=screenDataArea.getHeight();
        }
 else {
          if (!hZoom) {
            x=screenDataArea.getMinX();
            y=this.zoomPoint.getY();
            w=screenDataArea.getWidth();
            h=Math.min(this.zoomRectangle.getHeight(),maxY - this.zoomPoint.getY());
          }
 else {
            x=this.zoomPoint.getX();
            y=this.zoomPoint.getY();
            w=Math.min(this.zoomRectangle.getWidth(),maxX - this.zoomPoint.getX());
            h=Math.min(this.zoomRectangle.getHeight(),maxY - this.zoomPoint.getY());
          }
        }
        Rectangle2D zoomArea=new Rectangle2D.Double(x,y,w,h);
        panel.zoom(zoomArea);
      }
      this.zoomPoint=null;
      this.zoomRectangle=null;
      panel.setZoomRectangle(null);
      panel.clearLiveMouseHandler();
    }
 else {
      Graphics2D g2=(Graphics2D)panel.getGraphics();
      if (panel.getUseBuffer()) {
        panel.repaint();
      }
 else {
        drawZoomRectangle(panel,g2,true);
      }
      g2.dispose();
      this.zoomPoint=null;
      this.zoomRectangle=null;
      panel.setZoomRectangle(null);
      panel.clearLiveMouseHandler();
    }
  }
  /** 
 * Draws zoom rectangle (if present). The drawing is performed in XOR mode, therefore when this method is called twice in a row, the second call will completely restore the state of the canvas.
 * @param g2 the graphics device.
 * @param xor  use XOR for drawing?
 */
  private void drawZoomRectangle(  ChartPanel panel,  Graphics2D g2,  boolean xor){
    if (this.zoomRectangle != null) {
      if (xor) {
        g2.setXORMode(Color.gray);
      }
      if (panel.getFillZoomRectangle()) {
        g2.setPaint(panel.getZoomFillPaint());
        g2.fill(this.zoomRectangle);
      }
 else {
        g2.setPaint(panel.getZoomOutlinePaint());
        g2.draw(this.zoomRectangle);
      }
      if (xor) {
        g2.setPaintMode();
      }
    }
  }
}
