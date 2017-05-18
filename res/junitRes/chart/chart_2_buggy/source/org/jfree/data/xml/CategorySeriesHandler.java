package org.jfree.data.xml;
import java.util.Iterator;
import org.jfree.data.DefaultKeyedValues;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/** 
 * A handler for reading a series for a category dataset.
 */
public class CategorySeriesHandler extends DefaultHandler implements DatasetTags {
  /** 
 * The root handler. 
 */
  private RootHandler root;
  /** 
 * The series key. 
 */
  private Comparable seriesKey;
  /** 
 * The values. 
 */
  private DefaultKeyedValues values;
  /** 
 * Creates a new item handler.
 * @param root  the root handler.
 */
  public CategorySeriesHandler(  RootHandler root){
    this.root=root;
    this.values=new DefaultKeyedValues();
  }
  /** 
 * Sets the series key.
 * @param key  the key.
 */
  public void setSeriesKey(  Comparable key){
    this.seriesKey=key;
  }
  /** 
 * Adds an item to the temporary storage for the series.
 * @param key  the key.
 * @param value  the value.
 */
  public void addItem(  Comparable key,  final Number value){
    this.values.addValue(key,value);
  }
  /** 
 * The start of an element.
 * @param namespaceURI  the namespace.
 * @param localName  the element name.
 * @param qName  the element name.
 * @param atts  the attributes.
 * @throws SAXException for errors.
 */
  public void startElement(  String namespaceURI,  String localName,  String qName,  Attributes atts) throws SAXException {
    if (qName.equals(SERIES_TAG)) {
      setSeriesKey(atts.getValue("name"));
      ItemHandler subhandler=new ItemHandler(this.root,this);
      this.root.pushSubHandler(subhandler);
    }
 else {
      if (qName.equals(ITEM_TAG)) {
        ItemHandler subhandler=new ItemHandler(this.root,this);
        this.root.pushSubHandler(subhandler);
        subhandler.startElement(namespaceURI,localName,qName,atts);
      }
 else {
        throw new SAXException("Expecting <Series> or <Item> tag...found " + qName);
      }
    }
  }
  /** 
 * The end of an element.
 * @param namespaceURI  the namespace.
 * @param localName  the element name.
 * @param qName  the element name.
 */
  public void endElement(  String namespaceURI,  String localName,  String qName){
    if (this.root instanceof CategoryDatasetHandler) {
      CategoryDatasetHandler handler=(CategoryDatasetHandler)this.root;
      Iterator iterator=this.values.getKeys().iterator();
      while (iterator.hasNext()) {
        Comparable key=(Comparable)iterator.next();
        Number value=this.values.getValue(key);
        handler.addItem(this.seriesKey,key,value);
      }
      this.root.popSubHandler();
    }
  }
}
