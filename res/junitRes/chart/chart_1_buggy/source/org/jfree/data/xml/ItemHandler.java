package org.jfree.data.xml;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/** 
 * A handler for reading key-value items.
 */
public class ItemHandler extends DefaultHandler implements DatasetTags {
  /** 
 * The root handler. 
 */
  private RootHandler root;
  /** 
 * The parent handler (can be the same as root, but not always). 
 */
  private DefaultHandler parent;
  /** 
 * The key. 
 */
  private Comparable key;
  /** 
 * The value. 
 */
  private Number value;
  /** 
 * Creates a new item handler.
 * @param root  the root handler.
 * @param parent  the parent handler.
 */
  public ItemHandler(  RootHandler root,  DefaultHandler parent){
    this.root=root;
    this.parent=parent;
    this.key=null;
    this.value=null;
  }
  /** 
 * Returns the key that has been read by the handler, or <code>null</code>.
 * @return The key.
 */
  public Comparable getKey(){
    return this.key;
  }
  /** 
 * Sets the key.
 * @param key  the key.
 */
  public void setKey(  Comparable key){
    this.key=key;
  }
  /** 
 * Returns the key that has been read by the handler, or <code>null</code>.
 * @return The value.
 */
  public Number getValue(){
    return this.value;
  }
  /** 
 * Sets the value.
 * @param value  the value.
 */
  public void setValue(  Number value){
    this.value=value;
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
    if (qName.equals(ITEM_TAG)) {
      KeyHandler subhandler=new KeyHandler(this.root,this);
      this.root.pushSubHandler(subhandler);
    }
 else {
      if (qName.equals(VALUE_TAG)) {
        ValueHandler subhandler=new ValueHandler(this.root,this);
        this.root.pushSubHandler(subhandler);
      }
 else {
        throw new SAXException("Expected <Item> or <Value>...found " + qName);
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
    if (this.parent instanceof PieDatasetHandler) {
      PieDatasetHandler handler=(PieDatasetHandler)this.parent;
      handler.addItem(this.key,this.value);
      this.root.popSubHandler();
    }
 else {
      if (this.parent instanceof CategorySeriesHandler) {
        CategorySeriesHandler handler=(CategorySeriesHandler)this.parent;
        handler.addItem(this.key,this.value);
        this.root.popSubHandler();
      }
    }
  }
}
