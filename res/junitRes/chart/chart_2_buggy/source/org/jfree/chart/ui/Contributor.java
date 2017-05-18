package org.jfree.chart.ui;
/** 
 * A simple class representing a contributor to a software project.
 */
public class Contributor {
  /** 
 * The name. 
 */
  private String name;
  /** 
 * The e-mail address. 
 */
  private String email;
  /** 
 * Creates a new contributor.
 * @param name  the name.
 * @param email  the e-mail address.
 */
  public Contributor(  String name,  String email){
    this.name=name;
    this.email=email;
  }
  /** 
 * Returns the contributor's name.
 * @return the contributor's name.
 */
  public String getName(){
    return this.name;
  }
  /** 
 * Returns the contributor's e-mail address.
 * @return the contributor's e-mail address.
 */
  public String getEmail(){
    return this.email;
  }
}
