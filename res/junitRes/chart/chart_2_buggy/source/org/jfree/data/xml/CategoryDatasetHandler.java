package org.jfree.data.xml;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/** 
 * A SAX handler for reading a                                                                                                                                                               {@link CategoryDataset} from an XML file.
 */
public class CategoryDatasetHandler extends RootHandler implements DatasetTags {
  /** 
 * The dataset under construction. 
 */
  private DefaultCategoryDataset dataset;
  /** 
 * Creates a new handler.
 */
  public CategoryDatasetHandler(){
    this.dataset=null;
  }
  /** 
 * Returns the dataset.
 * @return The dataset.
 */
  public CategoryDataset getDataset(){
    return this.dataset;
  }
  /** 
 * Adds an item to the dataset.
 * @param rowKey  the row key.
 * @param columnKey  the column key.
 * @param value  the value.
 */
  public void addItem(  Comparable rowKey,  Comparable columnKey,  Number value){
    this.dataset.addValue(value,rowKey,columnKey);
  }
  /** 
 * The start of an element.
 * @param namespaceURI  the namespace.
 * @param localName  the element name.
 * @param qName  the element name.
 * @param atts  the element attributes.
 * @throws SAXException for errors.
 */
  public void startElement(  String namespaceURI,  String localName,  String qName,  Attributes atts) throws SAXException {
    DefaultHandler current=getCurrentHandler();
    if (current != this) {
      current.startElement(namespaceURI,localName,qName,atts);
    }
 else {
      if (qName.equals(CATEGORYDATASET_TAG)) {
        this.dataset=new DefaultCategoryDataset();
      }
 else {
        if (qName.equals(SERIES_TAG)) {
          CategorySeriesHandler subhandler=new CategorySeriesHandler(this);
          getSubHandlers().push(subhandler);
          subhandler.startElement(namespaceURI,localName,qName,atts);
        }
 else {
          throw new SAXException("Element not recognised: " + qName);
        }
      }
    }
  }
  /** 
 * The end of an element.
 * @param namespaceURI  the namespace.
 * @param localName  the element name.
 * @param qName  the element name.
 * @throws SAXException for errors.
 */
  public void endElement(  String namespaceURI,  String localName,  String qName) throws SAXException {
    DefaultHandler current=getCurrentHandler();
    if (current != this) {
      current.endElement(namespaceURI,localName,qName);
    }
  }
}
