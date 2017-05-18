package org.jfree.chart.ui;
/** 
 * A simple class representing a library in a software project.
 */
public class Library {
  /** 
 * The name. 
 */
  private String name;
  /** 
 * The version. 
 */
  private String version;
  /** 
 * The licenceName. 
 */
  private String licenceName;
  /** 
 * The version. 
 */
  private String info;
  /** 
 * Creates a new library reference.
 * @param name  the name.
 * @param version  the version.
 * @param licence  the licenceName.
 * @param info  the web address or other info.
 */
  public Library(  String name,  String version,  String licence,  String info){
    this.name=name;
    this.version=version;
    this.licenceName=licence;
    this.info=info;
  }
  /** 
 * Creates a new library reference.
 */
  protected Library(){
  }
  /** 
 * Returns the library name.
 * @return the library name.
 */
  public String getName(){
    return this.name;
  }
  /** 
 * Returns the library version.
 * @return the library version.
 */
  public String getVersion(){
    return this.version;
  }
  /** 
 * Returns the licenceName text.
 * @return the licenceName text.
 */
  public String getLicenceName(){
    return this.licenceName;
  }
  /** 
 * Returns the project info for the library.
 * @return the project info.
 */
  public String getInfo(){
    return this.info;
  }
  /** 
 * Sets the project info.
 * @param info  the project info.
 */
  protected void setInfo(  String info){
    this.info=info;
  }
  /** 
 * Sets the licence name.
 * @param licenceName  the licence name.
 */
  protected void setLicenceName(  String licenceName){
    this.licenceName=licenceName;
  }
  /** 
 * Sets the project name.
 * @param name  the project name.
 */
  protected void setName(  String name){
    this.name=name;
  }
  /** 
 * Sets the version identifier.
 * @param version  the version identifier.
 */
  protected void setVersion(  String version){
    this.version=version;
  }
  /** 
 * Tests this instance for equality with an arbitrary object.
 * @param obj  the object (<code>null</code> permitted).
 * @return A boolean.
 */
  public boolean equals(  Object obj){
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Library library=(Library)obj;
    if (this.name != null ? !this.name.equals(library.name) : library.name != null) {
      return false;
    }
    return true;
  }
  /** 
 * Returns a hash code for this instance.
 * @return A hash code.
 */
  public int hashCode(){
    return (this.name != null ? this.name.hashCode() : 0);
  }
}
