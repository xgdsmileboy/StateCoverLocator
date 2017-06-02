package org.jfree.chart.plot;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.data.Range;
/** 
 * A combined category plot where the range axis is shared.
 */
public class CombinedRangeCategoryPlot extends CategoryPlot implements PlotChangeListener {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=7260210007554504515L;
  /** 
 * Storage for the subplot references. 
 */
  private List subplots;
  /** 
 * The gap between subplots. 
 */
  private double gap;
  /** 
 * Temporary storage for the subplot areas. 
 */
  private transient Rectangle2D[] subplotArea;
  /** 
 * Default constructor.
 */
  public CombinedRangeCategoryPlot(){
    this(new NumberAxis());
  }
  /** 
 * Creates a new plot.
 * @param rangeAxis  the shared range axis.
 */
  public CombinedRangeCategoryPlot(  ValueAxis rangeAxis){
    super(null,null,rangeAxis,null);
    this.subplots=new java.util.ArrayList();
    this.gap=5.0;
  }
  /** 
 * Returns the space between subplots.
 * @return The gap (in Java2D units).
 */
  public double getGap(){
    return this.gap;
  }
  /** 
 * Sets the amount of space between subplots and sends a                                                                                                                                                               {@link PlotChangeEvent} to all registered listeners.
 * @param gap  the gap between subplots (in Java2D units).
 */
  public void setGap(  double gap){
    this.gap=gap;
    fireChangeEvent();
  }
  /** 
 * Adds a subplot (with a default 'weight' of 1) and sends a                                                                                                                                                               {@link PlotChangeEvent} to all registered listeners.<br><br> You must ensure that the subplot has a non-null domain axis.  The range axis for the subplot will be set to <code>null</code>.
 * @param subplot  the subplot (<code>null</code> not permitted).
 */
  public void add(  CategoryPlot subplot){
    add(subplot,1);
  }
  /** 
 * Adds a subplot and sends a                                                                                                                                                                {@link PlotChangeEvent} to all registeredlisteners. <br><br> You must ensure that the subplot has a non-null domain axis.  The range axis for the subplot will be set to <code>null</code>.
 * @param subplot  the subplot (<code>null</code> not permitted).
 * @param weight  the weight (must be >= 1).
 */
  public void add(  CategoryPlot subplot,  int weight){
    if (subplot == null) {
      throw new IllegalArgumentException("Null 'subplot' argument.");
    }
    if (weight <= 0) {
      throw new IllegalArgumentException("Require weight >= 1.");
    }
    subplot.setParent(this);
    subplot.setWeight(weight);
    subplot.setInsets(new RectangleInsets(0.0,0.0,0.0,0.0));
    subplot.setRangeAxis(null);
    subplot.setOrientation(getOrientation());
    subplot.addChangeListener(this);
    this.subplots.add(subplot);
    ValueAxis axis=getRangeAxis();
    if (axis != null) {
      axis.configure();
    }
    fireChangeEvent();
  }
  /** 
 * Removes a subplot from the combined chart.
 * @param subplot  the subplot (<code>null</code> not permitted).
 */
  public void remove(  CategoryPlot subplot){
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
      ValueAxis range=getRangeAxis();
      if (range != null) {
        range.configure();
      }
      ValueAxis range2=getRangeAxis(1);
      if (range2 != null) {
        range2.configure();
      }
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
      CategoryPlot sub=(CategoryPlot)this.subplots.get(i);
      totalWeight+=sub.getWeight();
    }
    this.subplotArea=new Rectangle2D[n];
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
      CategoryPlot plot=(CategoryPlot)this.subplots.get(i);
      if (orientation == PlotOrientation.VERTICAL) {
        double w=usableSize * plot.getWeight() / totalWeight;
        this.subplotArea[i]=new Rectangle2D.Double(x,y,w,adjustedPlotArea.getHeight());
        x=x + w + this.gap;
      }
 else {
        if (orientation == PlotOrientation.HORIZONTAL) {
          double h=usableSize * plot.getWeight() / totalWeight;
          this.subplotArea[i]=new Rectangle2D.Double(x,y,adjustedPlotArea.getWidth(),h);
          y=y + h + this.gap;
        }
      }
      AxisSpace subSpace=plot.calculateDomainAxisSpace(g2,this.subplotArea[i],null);
      space.ensureAtLeast(subSpace);
    }
    return space;
  }
  /** 
 * Draws the plot on a Java 2D graphics device (such as the screen or a printer).  Will perform all the placement calculations for each sub-plots and then tell these to draw themselves.
 * @param g2  the graphics device.
 * @param area  the area within which the plot (including axis labels)should be drawn.
 * @param anchor  the anchor point (<code>null</code> permitted).
 * @param parentState  the parent state.
 * @param info  collects information about the drawing (<code>null</code>permitted).
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
    RectangleEdge rangeEdge=getRangeAxisEdge();
    double cursor=RectangleEdge.coordinate(dataArea,rangeEdge);
    AxisState state=axis.draw(g2,cursor,area,dataArea,rangeEdge,info);
    if (parentState == null) {
      parentState=new PlotState();
    }
    parentState.getSharedAxisStates().put(axis,state);
    for (int i=0; i < this.subplots.size(); i++) {
      CategoryPlot plot=(CategoryPlot)this.subplots.get(i);
      PlotRenderingInfo subplotInfo=null;
      if (info != null) {
        subplotInfo=new PlotRenderingInfo(info.getOwner());
        info.addSubplotInfo(subplotInfo);
      }
      Point2D subAnchor=null;
      if (anchor != null && this.subplotArea[i].contains(anchor)) {
        subAnchor=anchor;
      }
      plot.draw(g2,this.subplotArea[i],subAnchor,parentState,subplotInfo);
    }
    if (info != null) {
      info.setDataArea(dataArea);
    }
  }
  /** 
 * Sets the orientation for the plot (and all the subplots).
 * @param orientation  the orientation.
 */
  public void setOrientation(  PlotOrientation orientation){
    super.setOrientation(orientation);
    Iterator iterator=this.subplots.iterator();
    while (iterator.hasNext()) {
      CategoryPlot plot=(CategoryPlot)iterator.next();
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
        CategoryPlot subplot=(CategoryPlot)iterator.next();
        result=Range.combine(result,subplot.getDataRange(axis));
      }
    }
    return result;
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
          CategoryPlot plot=(CategoryPlot)iterator.next();
          LegendItemCollection more=plot.getLegendItems();
          result.addAll(more);
        }
      }
    }
    return result;
  }
  /** 
 * Sets the size (width or height, depending on the orientation of the plot) for the domain axis of each subplot.
 * @param space  the space.
 */
  protected void setFixedDomainAxisSpaceForSubplots(  AxisSpace space){
    Iterator iterator=this.subplots.iterator();
    while (iterator.hasNext()) {
      CategoryPlot plot=(CategoryPlot)iterator.next();
      plot.setFixedDomainAxisSpace(space,false);
    }
  }
  /** 
 * Handles a 'click' on the plot by updating the anchor value.
 * @param x  x-coordinate of the click.
 * @param y  y-coordinate of the click.
 * @param info  information about the plot's dimensions.
 */
  public void handleClick(  int x,  int y,  PlotRenderingInfo info){
    Rectangle2D dataArea=info.getDataArea();
    if (dataArea.contains(x,y)) {
      for (int i=0; i < this.subplots.size(); i++) {
        CategoryPlot subplot=(CategoryPlot)this.subplots.get(i);
        PlotRenderingInfo subplotInfo=info.getSubplotInfo(i);
        subplot.handleClick(x,y,subplotInfo);
      }
    }
  }
  /** 
 * Receives a                                                                                                                                                                {@link PlotChangeEvent} and responds by notifying alllisteners.
 * @param event  the event.
 */
  public void plotChanged(  PlotChangeEvent event){
    notifyListeners(event);
  }
  /** 
 * Tests the plot for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return <code>true</code> or <code>false</code>.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CombinedRangeCategoryPlot)) {
      return false;
    }
    CombinedRangeCategoryPlot that=(CombinedRangeCategoryPlot)obj;
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
    CombinedRangeCategoryPlot result=(CombinedRangeCategoryPlot)super.clone();
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
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    ValueAxis rangeAxis=getRangeAxis();
    if (rangeAxis != null) {
      rangeAxis.configure();
    }
  }
}
