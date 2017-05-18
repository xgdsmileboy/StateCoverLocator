package org.jfree.chart.title;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.event.EventListenerList;
import org.jfree.chart.block.AbstractBlock;
import org.jfree.chart.block.Block;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.chart.event.TitleChangeListener;
import org.jfree.chart.util.HorizontalAlignment;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.chart.util.VerticalAlignment;
/** 
 * The base class for all chart titles.  A chart can have multiple titles, appearing at the top, bottom, left or right of the chart. <P> Concrete implementations of this class will render text and images, and hence do the actual work of drawing titles.
 */
public abstract class Title extends AbstractBlock implements Block, Cloneable, Serializable {
  /** 
 * For serialization. 
 */
  private static final long serialVersionUID=-6675162505277817221L;
  /** 
 * The default title position. 
 */
  public static final RectangleEdge DEFAULT_POSITION=RectangleEdge.TOP;
  /** 
 * The default horizontal alignment. 
 */
  public static final HorizontalAlignment DEFAULT_HORIZONTAL_ALIGNMENT=HorizontalAlignment.CENTER;
  /** 
 * The default vertical alignment. 
 */
  public static final VerticalAlignment DEFAULT_VERTICAL_ALIGNMENT=VerticalAlignment.CENTER;
  /** 
 * Default title padding. 
 */
  public static final RectangleInsets DEFAULT_PADDING=new RectangleInsets(1,1,1,1);
  /** 
 * A flag that controls whether or not the title is visible.
 * @since 1.0.11
 */
  public boolean visible;
  /** 
 * The title position. 
 */
  private RectangleEdge position;
  /** 
 * The horizontal alignment of the title content. 
 */
  private HorizontalAlignment horizontalAlignment;
  /** 
 * The vertical alignment of the title content. 
 */
  private VerticalAlignment verticalAlignment;
  /** 
 * Storage for registered change listeners. 
 */
  private transient EventListenerList listenerList;
  /** 
 * A flag that can be used to temporarily disable the listener mechanism.
 */
  private boolean notify;
  /** 
 * Creates a new title, using default attributes where necessary.
 */
  protected Title(){
    this(Title.DEFAULT_POSITION,Title.DEFAULT_HORIZONTAL_ALIGNMENT,Title.DEFAULT_VERTICAL_ALIGNMENT,Title.DEFAULT_PADDING);
  }
  /** 
 * Creates a new title, using default attributes where necessary.
 * @param position  the position of the title (<code>null</code> notpermitted).
 * @param horizontalAlignment  the horizontal alignment of the title(<code>null</code> not permitted).
 * @param verticalAlignment  the vertical alignment of the title(<code>null</code> not permitted).
 */
  protected Title(  RectangleEdge position,  HorizontalAlignment horizontalAlignment,  VerticalAlignment verticalAlignment){
    this(position,horizontalAlignment,verticalAlignment,Title.DEFAULT_PADDING);
  }
  /** 
 * Creates a new title.
 * @param position  the position of the title (<code>null</code> notpermitted).
 * @param horizontalAlignment  the horizontal alignment of the title (LEFT,CENTER or RIGHT, <code>null</code> not permitted).
 * @param verticalAlignment  the vertical alignment of the title (TOP,MIDDLE or BOTTOM, <code>null</code> not permitted).
 * @param padding  the amount of space to leave around the outside of thetitle (<code>null</code> not permitted).
 */
  protected Title(  RectangleEdge position,  HorizontalAlignment horizontalAlignment,  VerticalAlignment verticalAlignment,  RectangleInsets padding){
    if (position == null) {
      throw new IllegalArgumentException("Null 'position' argument.");
    }
    if (horizontalAlignment == null) {
      throw new IllegalArgumentException("Null 'horizontalAlignment' argument.");
    }
    if (verticalAlignment == null) {
      throw new IllegalArgumentException("Null 'verticalAlignment' argument.");
    }
    if (padding == null) {
      throw new IllegalArgumentException("Null 'spacer' argument.");
    }
    this.visible=true;
    this.position=position;
    this.horizontalAlignment=horizontalAlignment;
    this.verticalAlignment=verticalAlignment;
    setPadding(padding);
    this.listenerList=new EventListenerList();
    this.notify=true;
  }
  /** 
 * Returns a flag that controls whether or not the title should be drawn.  The default value is <code>true</code>.
 * @return A boolean.
 * @see #setVisible(boolean)
 * @since 1.0.11
 */
  public boolean isVisible(){
    return this.visible;
  }
  /** 
 * Sets a flag that controls whether or not the title should be drawn, and sends a                                                                                               {@link TitleChangeEvent} to all registered listeners.
 * @param visible  the new flag value.
 * @see #isVisible()
 * @since 1.0.11
 */
  public void setVisible(  boolean visible){
    this.visible=visible;
    notifyListeners(new TitleChangeEvent(this));
  }
  /** 
 * Returns the position of the title.
 * @return The title position (never <code>null</code>).
 */
  public RectangleEdge getPosition(){
    return this.position;
  }
  /** 
 * Sets the position for the title and sends a                                                                                               {@link TitleChangeEvent} toall registered listeners.
 * @param position  the position (<code>null</code> not permitted).
 */
  public void setPosition(  RectangleEdge position){
    if (position == null) {
      throw new IllegalArgumentException("Null 'position' argument.");
    }
    if (this.position != position) {
      this.position=position;
      notifyListeners(new TitleChangeEvent(this));
    }
  }
  /** 
 * Returns the horizontal alignment of the title.
 * @return The horizontal alignment (never <code>null</code>).
 */
  public HorizontalAlignment getHorizontalAlignment(){
    return this.horizontalAlignment;
  }
  /** 
 * Sets the horizontal alignment for the title and sends a                                                                                              {@link TitleChangeEvent} to all registered listeners.
 * @param alignment  the horizontal alignment (<code>null</code> notpermitted).
 */
  public void setHorizontalAlignment(  HorizontalAlignment alignment){
    if (alignment == null) {
      throw new IllegalArgumentException("Null 'alignment' argument.");
    }
    if (this.horizontalAlignment != alignment) {
      this.horizontalAlignment=alignment;
      notifyListeners(new TitleChangeEvent(this));
    }
  }
  /** 
 * Returns the vertical alignment of the title.
 * @return The vertical alignment (never <code>null</code>).
 */
  public VerticalAlignment getVerticalAlignment(){
    return this.verticalAlignment;
  }
  /** 
 * Sets the vertical alignment for the title, and notifies any registered listeners of the change.
 * @param alignment  the new vertical alignment (TOP, MIDDLE or BOTTOM,<code>null</code> not permitted).
 */
  public void setVerticalAlignment(  VerticalAlignment alignment){
    if (alignment == null) {
      throw new IllegalArgumentException("Null 'alignment' argument.");
    }
    if (this.verticalAlignment != alignment) {
      this.verticalAlignment=alignment;
      notifyListeners(new TitleChangeEvent(this));
    }
  }
  /** 
 * Returns the flag that indicates whether or not the notification mechanism is enabled.
 * @return The flag.
 */
  public boolean getNotify(){
    return this.notify;
  }
  /** 
 * Sets the flag that indicates whether or not the notification mechanism is enabled.  There are certain situations (such as cloning) where you want to turn notification off temporarily.
 * @param flag  the new value of the flag.
 */
  public void setNotify(  boolean flag){
    this.notify=flag;
    if (flag) {
      notifyListeners(new TitleChangeEvent(this));
    }
  }
  /** 
 * Draws the title on a Java 2D graphics device (such as the screen or a printer).
 * @param g2  the graphics device.
 * @param area  the area allocated for the title (subclasses should notdraw outside this area).
 */
  public abstract void draw(  Graphics2D g2,  Rectangle2D area);
  /** 
 * Returns a clone of the title. <P> One situation when this is useful is when editing the title properties - you can edit a clone, and then it is easier to cancel the changes if necessary.
 * @return A clone of the title.
 * @throws CloneNotSupportedException not thrown by this class, but it maybe thrown by subclasses.
 */
  public Object clone() throws CloneNotSupportedException {
    Title duplicate=(Title)super.clone();
    duplicate.listenerList=new EventListenerList();
    return duplicate;
  }
  /** 
 * Registers an object for notification of changes to the title.
 * @param listener  the object that is being registered.
 */
  public void addChangeListener(  TitleChangeListener listener){
    this.listenerList.add(TitleChangeListener.class,listener);
  }
  /** 
 * Unregisters an object for notification of changes to the chart title.
 * @param listener  the object that is being unregistered.
 */
  public void removeChangeListener(  TitleChangeListener listener){
    this.listenerList.remove(TitleChangeListener.class,listener);
  }
  /** 
 * Notifies all registered listeners that the chart title has changed in some way.
 * @param event  an object that contains information about the change tothe title.
 */
  protected void notifyListeners(  TitleChangeEvent event){
    if (this.notify) {
      Object[] listeners=this.listenerList.getListenerList();
      for (int i=listeners.length - 2; i >= 0; i-=2) {
        if (listeners[i] == TitleChangeListener.class) {
          ((TitleChangeListener)listeners[i + 1]).titleChanged(event);
        }
      }
    }
  }
  /** 
 * Tests an object for equality with this title.
 * @param obj  the object (<code>null</code> not permitted).
 * @return <code>true</code> or <code>false</code>.
 */
  public boolean equals(  Object obj){
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Title)) {
      return false;
    }
    Title that=(Title)obj;
    if (this.visible != that.visible) {
      return false;
    }
    if (this.position != that.position) {
      return false;
    }
    if (this.horizontalAlignment != that.horizontalAlignment) {
      return false;
    }
    if (this.verticalAlignment != that.verticalAlignment) {
      return false;
    }
    if (this.notify != that.notify) {
      return false;
    }
    return super.equals(obj);
  }
  /** 
 * Returns a hashcode for the title.
 * @return The hashcode.
 */
  public int hashCode(){
    int result=193;
    result=37 * result + ObjectUtilities.hashCode(this.position);
    result=37 * result + ObjectUtilities.hashCode(this.horizontalAlignment);
    result=37 * result + ObjectUtilities.hashCode(this.verticalAlignment);
    return result;
  }
  /** 
 * Provides serialization support.
 * @param stream  the output stream.
 * @throws IOException  if there is an I/O error.
 */
  private void writeObject(  ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
  }
  /** 
 * Provides serialization support.
 * @param stream  the input stream.
 * @throws IOException  if there is an I/O error.
 * @throws ClassNotFoundException  if there is a classpath problem.
 */
  private void readObject(  ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.listenerList=new EventListenerList();
  }
}
