package org.jfree.chart.ui;
import java.io.File;
import javax.swing.filechooser.FileFilter;
/** 
 * A filter for JFileChooser that filters files by extension.
 */
public class ExtensionFileFilter extends FileFilter {
  /** 
 * A description for the file type. 
 */
  private String description;
  /** 
 * The extension (for example, "png" for *.png files). 
 */
  private String extension;
  /** 
 * Standard constructor.
 * @param description  a description of the file type;
 * @param extension  the file extension;
 */
  public ExtensionFileFilter(  String description,  String extension){
    this.description=description;
    this.extension=extension;
  }
  /** 
 * Returns true if the file ends with the specified extension.
 * @param file  the file to test.
 * @return A boolean that indicates whether or not the file is accepted by the filter.
 */
  public boolean accept(  File file){
    if (file.isDirectory()) {
      return true;
    }
    String name=file.getName().toLowerCase();
    if (name.endsWith(this.extension)) {
      return true;
    }
 else {
      return false;
    }
  }
  /** 
 * Returns the description of the filter.
 * @return a description of the filter.
 */
  public String getDescription(){
    return this.description;
  }
}
