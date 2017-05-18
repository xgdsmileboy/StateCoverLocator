package org.jfree.chart.ui;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.util.ObjectUtilities;
/** 
 * Basic project info.
 */
public class BasicProjectInfo extends Library {
  /** 
 * A helper class, which simplifies the loading of optional library implementations.
 */
private static class OptionalLibraryHolder {
    private String libraryClass;
    private transient Library library;
    /** 
 * Creates a new instance.
 * @param libraryClass  the library class.
 */
    public OptionalLibraryHolder(    String libraryClass){
      if (libraryClass == null) {
        throw new NullPointerException("LibraryClass must not be null.");
      }
      this.libraryClass=libraryClass;
    }
    /** 
 * Creates a new instance.
 * @param library  the library (<code>null</code> not permitted).
 */
    public OptionalLibraryHolder(    Library library){
      if (library == null) {
        throw new NullPointerException("Library must not be null.");
      }
      this.library=library;
      this.libraryClass=library.getClass().getName();
    }
    /** 
 * Returns the library class.
 * @return The library class.
 */
    public String getLibraryClass(){
      return this.libraryClass;
    }
    /** 
 * Returns the library.
 * @return The library.
 */
    public Library getLibrary(){
      if (this.library == null) {
        this.library=loadLibrary(this.libraryClass);
      }
      return this.library;
    }
    protected Library loadLibrary(    String classname){
      if (classname == null) {
        return null;
      }
      try {
        Class c=ObjectUtilities.getClassLoader(getClass()).loadClass(classname);
        try {
          Method m=c.getMethod("getInstance",(Class[])null);
          return (Library)m.invoke(null,(Object[])null);
        }
 catch (        Exception e) {
        }
        return (Library)c.newInstance();
      }
 catch (      Exception e) {
        return null;
      }
    }
  }
  /** 
 * The project copyright statement. 
 */
  private String copyright;
  /** 
 * A list of libraries used by the project. 
 */
  private List libraries;
  private List optionalLibraries;
  /** 
 * Default constructor.
 */
  public BasicProjectInfo(){
    this.libraries=new ArrayList();
    this.optionalLibraries=new ArrayList();
  }
  /** 
 * Creates a new library reference.
 * @param name    the name.
 * @param version the version.
 * @param licence the licence.
 * @param info    the web address or other info.
 */
  public BasicProjectInfo(  String name,  String version,  String licence,  String info){
    this();
    setName(name);
    setVersion(version);
    setLicenceName(licence);
    setInfo(info);
  }
  /** 
 * Creates a new project info instance.
 * @param name  the project name.
 * @param version  the project version.
 * @param info  the project info (web site for example).
 * @param copyright  the copyright statement.
 * @param licenceName  the license name.
 */
  public BasicProjectInfo(  String name,  String version,  String info,  String copyright,  String licenceName){
    this(name,version,licenceName,info);
    setCopyright(copyright);
  }
  /** 
 * Returns the copyright statement.
 * @return The copyright statement.
 */
  public String getCopyright(){
    return this.copyright;
  }
  /** 
 * Sets the project copyright statement.
 * @param copyright  the project copyright statement.
 */
  public void setCopyright(  String copyright){
    this.copyright=copyright;
  }
  /** 
 * Sets the project info string (for example, this could be the project URL).
 * @param info  the info string.
 */
  public void setInfo(  String info){
    super.setInfo(info);
  }
  /** 
 * Sets the license name.
 * @param licence  the license name.
 */
  public void setLicenceName(  String licence){
    super.setLicenceName(licence);
  }
  /** 
 * Sets the project name.
 * @param name  the project name.
 */
  public void setName(  String name){
    super.setName(name);
  }
  /** 
 * Sets the project version number.
 * @param version  the version number.
 */
  public void setVersion(  String version){
    super.setVersion(version);
  }
  /** 
 * Returns a list of libraries used by the project.
 * @return the list of libraries.
 */
  public Library[] getLibraries(){
    return (Library[])this.libraries.toArray(new Library[this.libraries.size()]);
  }
  /** 
 * Adds a library.
 * @param library  the library.
 */
  public void addLibrary(  Library library){
    if (library == null) {
      throw new NullPointerException();
    }
    this.libraries.add(library);
  }
  /** 
 * Returns a list of optional libraries used by the project.
 * @return the list of libraries.
 */
  public Library[] getOptionalLibraries(){
    ArrayList libraries=new ArrayList();
    for (int i=0; i < this.optionalLibraries.size(); i++) {
      OptionalLibraryHolder holder=(OptionalLibraryHolder)this.optionalLibraries.get(i);
      Library l=holder.getLibrary();
      if (l != null) {
        libraries.add(l);
      }
    }
    return (Library[])libraries.toArray(new Library[libraries.size()]);
  }
  /** 
 * Adds an optional library.
 * @param libraryClass  the library.
 */
  public void addOptionalLibrary(  String libraryClass){
    if (libraryClass == null) {
      throw new NullPointerException("Library classname must be given.");
    }
    this.optionalLibraries.add(new OptionalLibraryHolder(libraryClass));
  }
  /** 
 * Adds an optional library. These libraries will be booted, if they define a boot class. A missing class is considered non-fatal and it is assumed that the programm knows how to handle that.
 * @param library  the library.
 */
  public void addOptionalLibrary(  Library library){
    if (library == null) {
      throw new NullPointerException("Library must be given.");
    }
    this.optionalLibraries.add(new OptionalLibraryHolder(library));
  }
}
