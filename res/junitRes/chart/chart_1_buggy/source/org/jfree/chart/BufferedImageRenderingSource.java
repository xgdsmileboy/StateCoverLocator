package org.jfree.chart;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetAndSelection;
import org.jfree.data.general.DatasetSelectionState;
/** 
 * The rendering source for drawing to a buffered image.
 * @since 1.2.0
 */
public class BufferedImageRenderingSource implements RenderingSource {
  /** 
 * The buffered image. 
 */
  private BufferedImage image;
  /** 
 * A list of                               {@link DatasetAndSelection} objects.
 */
  private List selectionStates=new java.util.ArrayList();
  /** 
 * Creates a new rendering source.
 * @param image  the buffered image (<code>null</code> not permitted).
 */
  public BufferedImageRenderingSource(  BufferedImage image){
    if (image == null) {
      throw new IllegalArgumentException("Null 'image' argument.");
    }
    this.image=image;
  }
  /** 
 * Returns a graphics context that a renderer can use to calculate selection bounds.
 * @return A graphics context.
 */
  public Graphics2D createGraphics2D(){
    return this.image.createGraphics();
  }
  /** 
 * Returns the selection state, if any, that this source is maintaining for the specified dataset.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @return The selection state (possibly <code>null</code>).
 */
  public DatasetSelectionState getSelectionState(  Dataset dataset){
    Iterator iterator=this.selectionStates.iterator();
    while (iterator.hasNext()) {
      DatasetAndSelection das=(DatasetAndSelection)iterator.next();
      if (das.getDataset() == dataset) {
        return das.getSelection();
      }
    }
    return null;
  }
  /** 
 * Stores the selection state that is associated with the specified dataset for this rendering source.  If two rendering sources are displaying the same dataset, ideally they should have separate selection states.
 * @param dataset  the dataset (<code>null</code> not permitted).
 * @param state  the state (<code>null</code> permitted).
 */
  public void putSelectionState(  Dataset dataset,  DatasetSelectionState state){
    this.selectionStates.add(new DatasetAndSelection(dataset,state));
  }
}
