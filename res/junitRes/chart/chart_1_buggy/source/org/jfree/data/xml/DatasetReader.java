package org.jfree.data.xml;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.pie.PieDataset;
import org.xml.sax.SAXException;
/** 
 * A utility class for reading datasets from XML.
 */
public class DatasetReader {
  /** 
 * Reads a                               {@link PieDataset} from an XML file.
 * @param file  the file.
 * @return A dataset.
 * @throws IOException if there is a problem reading the file.
 */
  public static PieDataset readPieDatasetFromXML(  File file) throws IOException {
    InputStream in=new FileInputStream(file);
    return readPieDatasetFromXML(in);
  }
  /** 
 * Reads a                               {@link PieDataset} from a stream.
 * @param in  the input stream.
 * @return A dataset.
 * @throws IOException if there is an I/O error.
 */
  public static PieDataset readPieDatasetFromXML(  InputStream in) throws IOException {
    PieDataset result=null;
    SAXParserFactory factory=SAXParserFactory.newInstance();
    try {
      SAXParser parser=factory.newSAXParser();
      PieDatasetHandler handler=new PieDatasetHandler();
      parser.parse(in,handler);
      result=handler.getDataset();
    }
 catch (    SAXException e) {
      System.out.println(e.getMessage());
    }
catch (    ParserConfigurationException e2) {
      System.out.println(e2.getMessage());
    }
    return result;
  }
  /** 
 * Reads a                               {@link CategoryDataset} from a file.
 * @param file  the file.
 * @return A dataset.
 * @throws IOException if there is a problem reading the file.
 */
  public static CategoryDataset readCategoryDatasetFromXML(  File file) throws IOException {
    InputStream in=new FileInputStream(file);
    return readCategoryDatasetFromXML(in);
  }
  /** 
 * Reads a                               {@link CategoryDataset} from a stream.
 * @param in  the stream.
 * @return A dataset.
 * @throws IOException if there is a problem reading the file.
 */
  public static CategoryDataset readCategoryDatasetFromXML(  InputStream in) throws IOException {
    CategoryDataset result=null;
    SAXParserFactory factory=SAXParserFactory.newInstance();
    try {
      SAXParser parser=factory.newSAXParser();
      CategoryDatasetHandler handler=new CategoryDatasetHandler();
      parser.parse(in,handler);
      result=handler.getDataset();
    }
 catch (    SAXException e) {
      System.out.println(e.getMessage());
    }
catch (    ParserConfigurationException e2) {
      System.out.println(e2.getMessage());
    }
    return result;
  }
}
