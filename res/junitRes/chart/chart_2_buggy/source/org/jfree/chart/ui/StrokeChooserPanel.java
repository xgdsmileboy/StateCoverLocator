package org.jfree.chart.ui;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
/** 
 * A component for choosing a stroke from a list of available strokes.  This class needs work.
 */
public class StrokeChooserPanel extends JPanel {
  /** 
 * A combo for selecting the stroke. 
 */
  private JComboBox selector;
  /** 
 * Creates a panel containing a combo-box that allows the user to select one stroke from a list of available strokes.
 * @param current  the current stroke sample.
 * @param available  an array of 'available' stroke samples.
 */
  public StrokeChooserPanel(  StrokeSample current,  StrokeSample[] available){
    setLayout(new BorderLayout());
    this.selector=new JComboBox(available);
    this.selector.setSelectedItem(current);
    this.selector.setRenderer(new StrokeSample(new BasicStroke(1)));
    add(this.selector);
    this.selector.addActionListener(new ActionListener(){
      public void actionPerformed(      final ActionEvent evt){
        getSelector().transferFocus();
      }
    }
);
  }
  /** 
 * Returns the selector component.
 * @return Returns the selector.
 */
  protected final JComboBox getSelector(){
    return this.selector;
  }
  /** 
 * Returns the selected stroke.
 * @return the selected stroke.
 */
  public Stroke getSelectedStroke(){
    StrokeSample sample=(StrokeSample)this.selector.getSelectedItem();
    return sample.getStroke();
  }
}
