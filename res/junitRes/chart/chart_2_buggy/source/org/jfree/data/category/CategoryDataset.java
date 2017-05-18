package org.jfree.data.category;
import org.jfree.data.KeyedValues2D;
import org.jfree.data.general.Dataset;
/** 
 * The interface for a dataset with one or more series, and values associated with categories. <P> The categories are represented by <code>Comparable</code> instance, with the category label being provided by the <code>toString</code> method.
 */
public interface CategoryDataset extends KeyedValues2D, Dataset {
}
