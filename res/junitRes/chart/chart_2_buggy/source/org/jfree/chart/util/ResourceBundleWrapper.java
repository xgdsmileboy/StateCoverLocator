package org.jfree.chart.util;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
/** 
 * Wrapper of ResourceBundle.getBundle() methods. This wrapper is introduced to avoid a dramatic performance penalty by superfluous resource (and classes loaded by Class.forName) lookups on web server in applets. <pre> public class AppletC extends javax.swing.JApplet { public void init() { ResourceBundleWrapper.removeCodeBase(getCodeBase(), (URLClassLoader) getClass().getClassLoader()); ... </pre>
 * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4243379">
 *               Bug ID: 4243379</a>
 * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4668479">
 *               Bug ID: 4668479</a>
 * @since 1.2.0
 */
public class ResourceBundleWrapper {
  /** 
 * A special class loader with no code base lookup.  This field may be <code>null</code> (the field is only initialised if removeCodeBase() is called from an applet.
 */
  private static URLClassLoader noCodeBaseClassLoader;
  /** 
 * Private constructor.
 */
  private ResourceBundleWrapper(){
  }
  /** 
 * Instantiate a                                                                                                                                                                {@link URLClassLoader} for resource lookups where thecodeBase URL is removed.  This method is typically called from an applet's init() method.  If this method is never called, the <code>getBundle()</code> methods map to the standard {@link ResourceBundle} lookup methods.
 * @param codeBase  the codeBase URL.
 * @param urlClassLoader  the class loader.
 */
  public static void removeCodeBase(  URL codeBase,  URLClassLoader urlClassLoader){
    List urlsNoBase=new ArrayList();
    URL[] urls=urlClassLoader.getURLs();
    for (int i=0; i < urls.length; i++) {
      if (!urls[i].sameFile(codeBase)) {
        urlsNoBase.add(urls[i]);
      }
    }
    URL[] urlsNoBaseArray=(URL[])urlsNoBase.toArray(new URL[0]);
    noCodeBaseClassLoader=URLClassLoader.newInstance(urlsNoBaseArray);
  }
  /** 
 * Finds and returns the specified resource bundle.
 * @param baseName  the base name.
 * @return The resource bundle.
 */
  public static final ResourceBundle getBundle(  String baseName){
    if (noCodeBaseClassLoader != null) {
      return ResourceBundle.getBundle(baseName,Locale.getDefault(),noCodeBaseClassLoader);
    }
 else {
      return ResourceBundle.getBundle(baseName);
    }
  }
  /** 
 * Finds and returns the specified resource bundle.
 * @param baseName  the base name.
 * @param locale  the locale.
 * @return The resource bundle.
 */
  public static final ResourceBundle getBundle(  String baseName,  Locale locale){
    if (noCodeBaseClassLoader != null) {
      return ResourceBundle.getBundle(baseName,locale,noCodeBaseClassLoader);
    }
 else {
      return ResourceBundle.getBundle(baseName,locale);
    }
  }
  /** 
 * Maps directly to <code>ResourceBundle.getBundle(baseName, locale, loader)</code>.
 * @param baseName  the base name.
 * @param locale  the locale.
 * @param loader  the class loader.
 * @return The resource bundle.
 */
  public static ResourceBundle getBundle(  String baseName,  Locale locale,  ClassLoader loader){
    return ResourceBundle.getBundle(baseName,locale,loader);
  }
}
