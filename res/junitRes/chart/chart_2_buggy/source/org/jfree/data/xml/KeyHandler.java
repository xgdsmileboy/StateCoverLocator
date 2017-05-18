package org.jfree.data.xml;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/** 
 * A SAX handler for reading a key.
 */
public class KeyHandler extends DefaultHandler implements DatasetTags {
  /** 
 * The root handler. 
 */
  private RootHandler rootHandler;
  /** 
 * The item handler. 
 */
  private ItemHandler itemHandler;
  /** 
 * Storage for the current CDATA 
 */
  private StringBuffer currentText;
  /** 
 * Creates a new handler.
 * @param rootHandler  the root handler.
 * @param itemHandler  the item handler.
 */
  public KeyHandler(  RootHandler rootHandler,  ItemHandler itemHandler){
    this.rootHandler=rootHandler;
    this.itemHandler=itemHandler;
    this.currentText=new StringBuffer();
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
    if (qName.equals(KEY_TAG)) {
      clearCurrentText();
    }
 else {
      throw new SAXException("Expecting <Key> but found " + qName);
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
    if (qName.equals(KEY_TAG)) {
      this.itemHandler.setKey(getCurrentText());
      this.rootHandler.popSubHandler();
      this.rootHandler.pushSubHandler(new ValueHandler(this.rootHandler,this.itemHandler));
    }
 else {
      throw new SAXException("Expecting </Key> but found " + qName);
    }
  }
  /** 
 * Receives some (or all) of the text in the current element.
 * @param ch  character buffer.
 * @param start  the start index.
 * @param length  the length of the valid character data.
 */
  public void characters(  char[] ch,  int start,  int length){
    if (this.currentText != null) {
      this.currentText.append(String.copyValueOf(ch,start,length));
    }
  }
  /** 
 * Returns the current text of the textbuffer.
 * @return The current text.
 */
  protected String getCurrentText(){
    return this.currentText.toString();
  }
  /** 
 * Removes all text from the textbuffer at the end of a CDATA section.
 */
  protected void clearCurrentText(){
    this.currentText.delete(0,this.currentText.length());
  }
}
