package org.jfree.chart.util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.Point;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.reflect.Method;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
/** 
 * A collection of utility methods relating to user interfaces.
 */
public class RefineryUtilities {
  private RefineryUtilities(){
  }
  /** 
 * Computes the center point of the current screen device. If this method is called on JDK 1.4, Xinerama-aware results are returned. (See Sun-Bug-ID 4463949 for details).
 * @return the center point of the current screen.
 */
  public static Point getCenterPoint(){
    GraphicsEnvironment localGraphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
    try {
      Method method=GraphicsEnvironment.class.getMethod("getCenterPoint",(Class[])null);
      return (Point)method.invoke(localGraphicsEnvironment,(Object[])null);
    }
 catch (    Exception e) {
    }
    Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
    return new Point(s.width / 2,s.height / 2);
  }
  /** 
 * Computes the maximum bounds of the current screen device. If this method is called on JDK 1.4, Xinerama-aware results are returned. (See Sun-Bug-ID 4463949 for details).
 * @return the maximum bounds of the current screen.
 */
  public static Rectangle getMaximumWindowBounds(){
    GraphicsEnvironment localGraphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
    try {
      Method method=GraphicsEnvironment.class.getMethod("getMaximumWindowBounds",(Class[])null);
      return (Rectangle)method.invoke(localGraphicsEnvironment,(Object[])null);
    }
 catch (    Exception e) {
    }
    Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
    return new Rectangle(0,0,s.width,s.height);
  }
  /** 
 * Positions the specified frame in the middle of the screen.
 * @param frame  the frame to be centered on the screen.
 */
  public static void centerFrameOnScreen(  final Window frame){
    positionFrameOnScreen(frame,0.5,0.5);
  }
  /** 
 * Positions the specified frame at a relative position in the screen, where 50% is considered to be the center of the screen.
 * @param frame  the frame.
 * @param horizontalPercent  the relative horizontal position of the frame(0.0 to 1.0, where 0.5 is the center of the screen).
 * @param verticalPercent  the relative vertical position of the frame(0.0 to 1.0, where 0.5 is the center of the screen).
 */
  public static void positionFrameOnScreen(  Window frame,  double horizontalPercent,  double verticalPercent){
    Rectangle s=getMaximumWindowBounds();
    Dimension f=frame.getSize();
    int w=Math.max(s.width - f.width,0);
    int h=Math.max(s.height - f.height,0);
    int x=(int)(horizontalPercent * w) + s.x;
    int y=(int)(verticalPercent * h) + s.y;
    frame.setBounds(x,y,f.width,f.height);
  }
  /** 
 * Positions the specified frame at a random location on the screen while ensuring that the entire frame is visible (provided that the frame is smaller than the screen).
 * @param frame  the frame.
 */
  public static void positionFrameRandomly(  Window frame){
    positionFrameOnScreen(frame,Math.random(),Math.random());
  }
  /** 
 * Positions the specified dialog within its parent.
 * @param dialog  the dialog to be positioned on the screen.
 */
  public static void centerDialogInParent(  Dialog dialog){
    positionDialogRelativeToParent(dialog,0.5,0.5);
  }
  /** 
 * Positions the specified dialog at a position relative to its parent.
 * @param dialog  the dialog to be positioned.
 * @param horizontalPercent  the relative location.
 * @param verticalPercent  the relative location.
 */
  public static void positionDialogRelativeToParent(  Dialog dialog,  double horizontalPercent,  double verticalPercent){
    Dimension d=dialog.getSize();
    Container parent=dialog.getParent();
    Dimension p=parent.getSize();
    int baseX=parent.getX() - d.width;
    int baseY=parent.getY() - d.height;
    int w=d.width + p.width;
    int h=d.height + p.height;
    int x=baseX + (int)(horizontalPercent * w);
    int y=baseY + (int)(verticalPercent * h);
    Rectangle s=getMaximumWindowBounds();
    x=Math.min(x,(s.width - d.width));
    x=Math.max(x,0);
    y=Math.min(y,(s.height - d.height));
    y=Math.max(y,0);
    dialog.setBounds(x + s.x,y + s.y,d.width,d.height);
  }
  /** 
 * Creates a panel that contains a table based on the specified table model.
 * @param model  the table model to use when constructing the table.
 * @return The panel.
 */
  public static JPanel createTablePanel(  TableModel model){
    JPanel panel=new JPanel(new BorderLayout());
    JTable table=new JTable(model);
    for (int columnIndex=0; columnIndex < model.getColumnCount(); columnIndex++) {
      TableColumn column=table.getColumnModel().getColumn(columnIndex);
      Class c=model.getColumnClass(columnIndex);
      if (c.equals(Number.class)) {
        column.setCellRenderer(new NumberCellRenderer());
      }
    }
    panel.add(new JScrollPane(table));
    return panel;
  }
  /** 
 * Creates a label with a specific font.
 * @param text  the text for the label.
 * @param font  the font.
 * @return The label.
 */
  public static JLabel createJLabel(  String text,  Font font){
    JLabel result=new JLabel(text);
    result.setFont(font);
    return result;
  }
  /** 
 * Creates a label with a specific font and color.
 * @param text  the text for the label.
 * @param font  the font.
 * @param color  the color.
 * @return The label.
 */
  public static JLabel createJLabel(  String text,  Font font,  Color color){
    JLabel result=new JLabel(text);
    result.setFont(font);
    result.setForeground(color);
    return result;
  }
  /** 
 * Creates a      {@link JButton}.
 * @param label  the label.
 * @param font  the font.
 * @return The button.
 */
  public static JButton createJButton(  String label,  Font font){
    JButton result=new JButton(label);
    result.setFont(font);
    return result;
  }
}
