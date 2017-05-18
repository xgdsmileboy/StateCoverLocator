package org.jfree.chart.servlet;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/** 
 * Servlet used for streaming charts to the client browser from the temporary directory.  You need to add this servlet and mapping to your deployment descriptor (web.xml) in order to get it to work.  The syntax is as follows: <xmp> <servlet> <servlet-name>DisplayChart</servlet-name> <servlet-class>org.jfree.chart.servlet.DisplayChart</servlet-class> </servlet> <servlet-mapping> <servlet-name>DisplayChart</servlet-name> <url-pattern>/servlet/DisplayChart</url-pattern> </servlet-mapping> </xmp>
 */
public class DisplayChart extends HttpServlet {
  /** 
 * Default constructor.
 */
  public DisplayChart(){
    super();
  }
  /** 
 * Init method.
 * @throws ServletException never.
 */
  public void init() throws ServletException {
    return;
  }
  /** 
 * Service method.
 * @param request  the request.
 * @param response  the response.
 * @throws ServletException ??.
 * @throws IOException ??.
 */
  public void service(  HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException {
    HttpSession session=request.getSession();
    String filename=request.getParameter("filename");
    if (filename == null) {
      throw new ServletException("Parameter 'filename' must be supplied");
    }
    filename=ServletUtilities.searchReplace(filename,"..","");
    File file=new File(System.getProperty("java.io.tmpdir"),filename);
    if (!file.exists()) {
      throw new ServletException("File '" + file.getAbsolutePath() + "' does not exist");
    }
    boolean isChartInUserList=false;
    ChartDeleter chartDeleter=(ChartDeleter)session.getAttribute("JFreeChart_Deleter");
    if (chartDeleter != null) {
      isChartInUserList=chartDeleter.isChartAvailable(filename);
    }
    boolean isChartPublic=false;
    if (filename.length() >= 6) {
      if (filename.substring(0,6).equals("public")) {
        isChartPublic=true;
      }
    }
    boolean isOneTimeChart=false;
    if (filename.startsWith(ServletUtilities.getTempOneTimeFilePrefix())) {
      isOneTimeChart=true;
    }
    if (isChartInUserList || isChartPublic || isOneTimeChart) {
      ServletUtilities.sendTempFile(file,response);
      if (isOneTimeChart) {
        file.delete();
      }
    }
 else {
      throw new ServletException("Chart image not found");
    }
    return;
  }
}
