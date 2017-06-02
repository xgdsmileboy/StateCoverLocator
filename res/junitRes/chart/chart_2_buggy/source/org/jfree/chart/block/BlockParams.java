package org.jfree.chart.block;
/** 
 * A standard parameter object that can be passed to the draw() method defined by the                                                                                                                                                                {@link Block} class.
 */
public class BlockParams implements EntityBlockParams {
  /** 
 * A flag that controls whether or not the block should generate and return chart entities for the items it draws.
 */
  private boolean generateEntities;
  /** 
 * The x-translation (used to enable chart entities to use global coordinates rather than coordinates that are local to the container they are within).
 */
  private double translateX;
  /** 
 * The y-translation (used to enable chart entities to use global coordinates rather than coordinates that are local to the container they are within).
 */
  private double translateY;
  /** 
 * Creates a new instance.
 */
  public BlockParams(){
    this.translateX=0.0;
    this.translateY=0.0;
    this.generateEntities=false;
  }
  /** 
 * Returns the flag that controls whether or not chart entities are generated.
 * @return A boolean.
 */
  public boolean getGenerateEntities(){
    return this.generateEntities;
  }
  /** 
 * Sets the flag that controls whether or not chart entities are generated.
 * @param generate  the flag.
 */
  public void setGenerateEntities(  boolean generate){
    this.generateEntities=generate;
  }
  /** 
 * Returns the translation required to convert local x-coordinates back to the coordinate space of the container.
 * @return The x-translation amount.
 */
  public double getTranslateX(){
    return this.translateX;
  }
  /** 
 * Sets the translation required to convert local x-coordinates into the coordinate space of the container.
 * @param x  the x-translation amount.
 */
  public void setTranslateX(  double x){
    this.translateX=x;
  }
  /** 
 * Returns the translation required to convert local y-coordinates back to the coordinate space of the container.
 * @return The y-translation amount.
 */
  public double getTranslateY(){
    return this.translateY;
  }
  /** 
 * Sets the translation required to convert local y-coordinates into the coordinate space of the container.
 * @param y  the y-translation amount.
 */
  public void setTranslateY(  double y){
    this.translateY=y;
  }
}
