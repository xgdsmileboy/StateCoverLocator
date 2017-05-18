package org.jfree.data.xml;
import java.util.Stack;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/** 
 * A SAX handler that delegates work to sub-handlers.
 */
public class RootHandler extends DefaultHandler implements DatasetTags {
  /** 
 * The sub-handlers. 
 */
  private Stack subHandlers;
  /** 
 * Creates a new handler.
 */
  public RootHandler(){
    this.subHandlers=new Stack();
  }
  /** 
 * Returns the stack of sub handlers.
 * @return The sub-handler stack.
 */
  public Stack getSubHandlers(){
    return this.subHandlers;
  }
  /** 
 * Receives some (or all) of the text in the current element.
 * @param ch  character buffer.
 * @param start  the start index.
 * @param length  the length of the valid character data.
 * @throws SAXException for errors.
 */
  public void characters(  char[] ch,  int start,  int length) throws SAXException {
    DefaultHandler handler=getCurrentHandler();
    if (handler != this) {
      handler.characters(ch,start,length);
    }
  }
  /** 
 * Returns the handler at the top of the stack.
 * @return The handler.
 */
  public DefaultHandler getCurrentHandler(){
    DefaultHandler result=this;
    if (this.subHandlers != null) {
      if (this.subHandlers.size() > 0) {
        Object top=this.subHandlers.peek();
        if (top != null) {
          result=(DefaultHandler)top;
        }
      }
    }
    return result;
  }
  /** 
 * Pushes a sub-handler onto the stack.
 * @param subhandler  the sub-handler.
 */
  public void pushSubHandler(  DefaultHandler subhandler){
    this.subHandlers.push(subhandler);
  }
  /** 
 * Pops a sub-handler from the stack.
 * @return The sub-handler.
 */
  public DefaultHandler popSubHandler(){
    return (DefaultHandler)this.subHandlers.pop();
  }
}
