package org.jfree.chart.plot;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.data.Range;
/** 
 * An extension of                               {@link XYPlot} that contains multiple subplots that share acommon range axis.
 */
public class CombinedRangeXYPlot extends XYPlot implements PlotChangeListener {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-5177814085082031168L;
  /** 
 * Storage for the subplot references. 
 */
  private List subplots;
  /** 
 * The gap between subplots. 
 */
  private double gap=5.0;
  /** 
 * Temporary storage for the subplot areas. 
 */
  private transient Rectangle2D[] subplotAreas;
  /** 
 * Default constructor.
 */
  public CombinedRangeXYPlot(){
    this(new NumberAxis());
  }
  /** 
 * Creates a new plot.
 * @param rangeAxis  the shared axis.
 */
  public CombinedRangeXYPlot(  ValueAxis rangeAxis){
    super(null,null,rangeAxis,null);
    this.subplots=new java.util.ArrayList();
  }
  /** 
 * Returns a string describing the type of plot.
 * @return The type of plot.
 */
  public String getPlotType(){
    return localizationResources.getString("Combined_Range_XYPlot");
  }
  /** 
 * Returns the space between subplots.
 * @return The gap
 */
  public double getGap(){
    return this.gap;
  }
  /** 
 * Sets the amount of space between subplots.
 * @param gap  the gap between subplots
 */
  public void setGap(  double gap){
    this.gap=gap;
  }
  /** 
 * Adds a subplot, with a default 'weight' of 1. <br><br> You must ensure that the subplot has a non-null domain axis.  The range axis for the subplot will be set to <code>null</code>.
 * @param subplot  the subplot.
 */
  public void add(  XYPlot subplot){
    add(subplot,1);
  }
  /** 
 * Adds a subplot with a particular weight (greater than or equal to one). The weight determines how much space is allocated to the subplot relative to all the other subplots. <br><br> You must ensure that the subplot has a non-null domain axis.  The range axis for the subplot will be set to <code>null</code>.
 * @param subplot  the subplot.
 * @param weight  the weight (must be 1 or greater).
 */
  public void add(  XYPlot subplot,  int weight){
    if (weight <= 0) {
      String msg="The 'weight' must be positive.";
      throw new IllegalArgumentException(msg);
    }
    subplot.setParent(this);
    subplot.setWeight(weight);
    subplot.setInsets(new RectangleInsets(0.0,0.0,0.0,0.0));
    subplot.setRangeAxis(null);
    subplot.addChangeListener(this);
    this.subplots.add(subplot);
    configureRangeAxes();
    fireChangeEvent();
  }
  /** 
 * Removes a subplot from the combined chart.
 * @param subplot  the subplot (<code>null</code> not permitted).
 */
  public void remove(  XYPlot subplot){
    if (subplot == null) {
      throw new IllegalArgumentException(" Null 'subplot' argument.");
    }
    int position=-1;
    int size=this.subplots.size();
    int i=0;
    while (position == -1 && i < size) {
      if (this.subplots.get(i) == subplot) {
        position=i;
      }
      i++;
    }
    if (position != -1) {
      this.subplots.remove(position);
      subplot.setParent(null);
      subplot.removeChangeListener(this);
      configureRangeAxes();
      fireChangeEvent();
    }
  }
  /** 
 * Returns the list of subplots.  The returned list may be empty, but is never <code>null</code>.
 * @return An unmodifiable list of subplots.
 */
  public List getSubplots(){
    if (this.subplots != null) {
      return Collections.unmodifiableList(this.subplots);
    }
 else {
      return Collections.EMPTY_LIST;
    }
  }
  /** 
 * Calculates the space required for the axes.
 * @param g2  the graphics device.
 * @param plotArea  the plot area.
 * @return The space required for the axes.
 */
  protected AxisSpace calculateAxisSpace(  Graphics2D g2,  Rectangle2D plotArea){
    AxisSpace space=new AxisSpace();
    PlotOrientation orientation=getOrientation();
    AxisSpace fixed=getFixedRangeAxisSpace();
    if (fixed != null) {
      if (orientation == PlotOrientation.VERTICAL) {
        space.setLeft(fixed.getLeft());
        space.setRight(fixed.getRight());
      }
 else {
        if (orientation == PlotOrientation.HORIZONTAL) {
          space.setTop(fixed.getTop());
          space.setBottom(fixed.getBottom());
        }
      }
    }
 else {
      ValueAxis valueAxis=getRangeAxis();
      RectangleEdge valueEdge=Plot.resolveRangeAxisLocation(getRangeAxisLocation(),orientation);
      if (valueAxis != null) {
        space=valueAxis.reserveSpace(g2,this,plotArea,valueEdge,space);
      }
    }
    Rectangle2D adjustedPlotArea=space.shrink(plotArea,null);
    int n=this.subplots.size();
    int totalWeight=0;
    for (int i=0; i < n; i++) {
      XYPlot sub=(XYPlot)this.subplots.get(i);
      totalWeight+=sub.getWeight();
    }
    this.subplotAreas=new Rectangle2D[n];
    double x=adjustedPlotArea.getX();
    double y=adjustedPlotArea.getY();
    double usableSize=0.0;
    if (orientation == PlotOrientation.VERTICAL) {
      usableSize=adjustedPlotArea.getWidth() - this.gap * (n - 1);
    }
 else {
      if (orientation == PlotOrientation.HORIZONTAL) {
        usableSize=adjustedPlotArea.getHeight() - this.gap * (n - 1);
      }
    }
    for (int i=0; i < n; i++) {
      XYPlot plot=(XYPlot)this.subplots.get(i);
      if (orientation == PlotOrientation.VERTICAL) {
        double w=usableSize * plot.getWeight() / totalWeight;
        this.subplotAreas[i]=new Rectangle2D.Double(x,y,w,adjustedPlotArea.getHeight());
        x=x + w + this.gap;
      }
 else {
        if (orientation == PlotOrientation.HORIZONTAL) {
          double h=usableSize * plot.getWeight() / totalWeight;
          this.subplotAreas[i]=new Rectangle2D.Double(x,y,adjustedPlotArea.getWidth(),h);
          y=y + h + this.gap;
        }
      }
      AxisSpace subSpace=plot.calculateDomainAxisSpace(g2,this.subplotAreas[i],null);
      space.ensureAtLeast(subSpace);
    }
    return space;
  }
  /** 
 * Draws the plot within the specified area on a graphics device.
 * @param g2  the graphics device.
 * @param area  the plot area (in Java2D space).
 * @param anchor  an anchor point in Java2D space (<code>null</code>permitted).
 * @param parentState  the state from the parent plot, if there is one(<code>null</code> permitted).
 * @param info  collects chart drawing information (<code>null</code>permitted).
 */
  public void draw(  Graphics2D g2,  Rectangle2D area,  Point2D anchor,  PlotState parentState,  PlotRenderingInfo info){
    if (info != null) {
      info.setPlotArea(area);
    }
    RectangleInsets insets=getInsets();
    insets.trim(area);
    AxisSpace space=calculateAxisSpace(g2,area);
    Rectangle2D dataArea=space.shrink(area,null);
    setFixedDomainAxisSpaceForSubplots(space);
    ValueAxis axis=getRangeAxis();
    RectangleEdge edge=getRangeAxisEdge();
    double cursor=RectangleEdge.coordinate(dataArea,edge);
    AxisState axisState=axis.draw(g2,cursor,area,dataArea,edge,info);
    if (parentState == null) {
      parentState=new PlotState();
    }
    parentState.getSharedAxisStates().put(axis,axisState);
    for (int i=0; i < this.subplots.size(); i++) {
      XYPlot plot=(XYPlot)this.subplots.get(i);
      PlotRenderingInfo subplotInfo=null;
      if (info != null) {
        subplotInfo=new PlotRenderingInfo(info.getOwner());
        info.addSubplotInfo(subplotInfo);
      }
      plot.draw(g2,this.subplotAreas[i],anchor,parentState,subplotInfo);
    }
    if (info != null) {
      info.setDataArea(dataArea);
    }
  }
  /** 
 * Returns a collection of legend items for the plot.
 * @return The legend items.
 */
  public LegendItemCollection getLegendItems(){
    LegendItemCollection result=getFixedLegendItems();
    if (result == null) {
      result=new LegendItemCollection();
      if (this.subplots != null) {
        Iterator iterator=this.subplots.iterator();
        while (iterator.hasNext()) {
          XYPlot plot=(XYPlot)iterator.next();
          LegendItemCollection more=plot.getLegendItems();
          result.addAll(more);
        }
      }
    }
    return result;
  }
  /** 
 * Multiplies the range on the domain axis/axes by the specified factor.
 * @param factor  the zoom factor.
 * @param info  the plot rendering info (<code>null</code> not permitted).
 * @param source  the source point (<code>null</code> not permitted).
 */
  public void zoomDomainAxes(  double factor,  PlotRenderingInfo info,  Point2D source){
    zoomDomainAxes(factor,info,source,false);
  }
  /** 
 * Multiplies the range on the domain axis/axes by the specified factor.
 * @param factor  the zoom factor.
 * @param info  the plot rendering info (<code>null</code> not permitted).
 * @param source  the source point (<code>null</code> not permitted).
 * @param useAnchor  zoom about the anchor point?
 */
  public void zoomDomainAxes(  double factor,  PlotRenderingInfo info,  Point2D source,  boolean useAnchor){
    XYPlot subplot=findSubplot(info,source);
    if (subplot != null) {
      subplot.zoomDomainAxes(factor,info,source,useAnchor);
    }
 else {
      Iterator iterator=getSubplots().iterator();
      while (iterator.hasNext()) {
        subplot=(XYPlot)iterator.next();
        subplot.zoomDomainAxes(factor,info,source,useAnchor);
      }
    }
  }
  /** 
 * Zooms in on the domain axes.
 * @param lowerPercent  the lower bound.
 * @param upperPercent  the upper bound.
 * @param info  the plot rendering info (<code>null</code> not permitted).
 * @param source  the source point (<code>null</code> not permitted).
 */
  public void zoomDomainAxes(  double lowerPercent,  double upperPercent,  PlotRenderingInfo info,  Point2D source){
    XYPlot subplot=findSubplot(info,source);
    if (subplot != null) {
      subplot.zoomDomainAxes(lowerPercent,upperPercent,info,source);
    }
 else {
      Iterator iterator=getSubplots().iterator();
      while (iterator.hasNext()) {
        subplot=(XYPlot)iterator.next();
        subplot.zoomDomainAxes(lowerPercent,upperPercent,info,source);
      }
    }
  }
  /** 
 * Returns the subplot (if any) that contains the (x, y) point (specified in Java2D space).
 * @param info  the chart rendering info (<code>null</code> not permitted).
 * @param source  the source point (<code>null</code> not permitted).
 * @return A subplot (possibly <code>null</code>).
 */
  public XYPlot findSubplot(  PlotRenderingInfo info,  Point2D source){
    if (info == null) {
      throw new IllegalArgumentException("Null 'info' argument.");
    }
    if (source == null) {
      throw new IllegalArgumentException("Null 'source' argument.");
    }
    XYPlot result=null;
    int subplotIndex=info.getSubplotIndex(source);
    if (subplotIndex >= 0) {
      result=(XYPlot)this.subplots.get(subplotIndex);
    }
    return result;
  }
  /** 
 * Sets the item renderer FOR ALL SUBPLOTS.  Registered listeners are notified that the plot has been modified. <P> Note: usually you will want to set the renderer independently for each subplot, which is NOT what this method does.
 * @param renderer the new renderer.
 */
  public void setRenderer(  XYItemRenderer renderer){
    super.setRenderer(renderer);
    Iterator iterator=this.subplots.iterator();
    while (iterator.hasNext()) {
      XYPlot plot=(XYPlot)iterator.next();
      plot.setRenderer(renderer);
    }
  }
  /** 
 * Sets the orientation for the plot (and all its subplots).
 * @param orientation  the orientation.
 */
  public void setOrientation(  PlotOrientation orientation){
    super.setOrientation(orientation);
    Iterator iterator=this.subplots.iterator();
    while (iterator.hasNext()) {
      XYPlot plot=(XYPlot)iterator.next();
      plot.setOrientation(orientation);
    }
  }
  /** 
 * Returns a range representing the extent of the data values in this plot (obtained from the subplots) that will be rendered against the specified axis.  NOTE: This method is intended for internal JFreeChart use, and is public only so that code in the axis classes can call it.  Since only the range axis is shared between subplots, the JFreeChart code will only call this method for the range values (although this is not checked/enforced).
 * @param axis  the axis.
 * @return The range.
 */
  public Range getDataRange(  ValueAxis axis){
    Range result=null;
    if (this.subplots != null) {
      Iterator iterator=this.subplots.iterator();
      while (iterator.hasNext()) {
        XYPlot subplot=(XYPlot)iterator.next();
        result=Range.combine(result,subplot.getDataRange(axis));
      }
    }
    return result;
  }
  /** 
 * Sets the space (width or height, depending on the orientation of the plot) for the domain axis of each subplot.
 * @param space  the space.
 */
  protected void setFixedDomainAxisSpaceForSubplots(  AxisSpace space){
    Iterator iterator=this.subplots.iterator();
    while (iterator.hasNext()) {
      XYPlot plot=(XYPlot)iterator.next();
      plot.setFixedDomainAxisSpace(space,false);
    }
  }
  /** 
 * Handles a 'click' on the plot by updating the anchor values...
 * @param x  x-coordinate, where the click occured.
 * @param y  y-coordinate, where the click occured.
 * @param info  object containing information about the plot dimensions.
 */
  public void handleClick(  int x,  int y,  PlotRenderingInfo info){
    Rectangle2D dataArea=info.getDataArea();
    if (dataArea.contains(x,y)) {
      for (int i=0; i < this.subplots.size(); i++) {
        XYPlot subplot=(XYPlot)this.subplots.get(i);
        PlotRenderingInfo subplotInfo=info.getSubplotInfo(i);
        subplot.handleClick(x,y,subplotInfo);
      }
    }
  }
  /** 
 * Receives a                               {@link PlotChangeEvent} and responds by notifying alllisteners.
 * @param event  the event.
 */
  public void plotChanged(  PlotChangeEvent event){
    notifyListeners(event);
  }
  /** 
 * Tests this plot for equality with another object.
 * @param obj  the other object.
 * @return <code>true</code> or <code>false</code>.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CombinedRangeXYPlot)) {
      return false;
    }
    CombinedRangeXYPlot that=(CombinedRangeXYPlot)obj;
    if (this.gap != that.gap) {
      return false;
    }
    if (!ObjectUtilities.equal(this.subplots,that.subplots)) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns a clone of the plot.
 * @return A clone.
 * @throws CloneNotSupportedException  this class will not throw thisexception, but subclasses (if any) might.
 */
  public Object clone() throws CloneNotSupportedException {
    CombinedRangeXYPlot result=(CombinedRangeXYPlot)super.clone();
    result.subplots=(List)ObjectUtilities.deepClone(this.subplots);
    for (Iterator it=result.subplots.iterator(); it.hasNext(); ) {
      Plot child=(Plot)it.next();
      child.setParent(result);
    }
    ValueAxis rangeAxis=result.getRangeAxis();
    if (rangeAxis != null) {
      rangeAxis.configure();
    }
    return result;
  }
}
