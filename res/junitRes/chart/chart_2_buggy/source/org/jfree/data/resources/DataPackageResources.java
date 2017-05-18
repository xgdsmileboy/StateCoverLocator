package org.jfree.data.resources;
import java.util.ListResourceBundle;
/** 
 * A resource bundle that stores all the items that might need localisation.
 */
public class DataPackageResources extends ListResourceBundle {
  /** 
 * Returns the array of strings in the resource bundle.
 * @return The localised resources.
 */
  public Object[][] getContents(){
    return CONTENTS;
  }
  /** 
 * The resources to be localised. 
 */
  private static final Object[][] CONTENTS={{"series.default-prefix","Series"},{"categories.default-prefix","Category"}};
}
