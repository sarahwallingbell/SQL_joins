/*
 * Sarah Walling-Bell
 * CS 455: Databases
 * Hwk 6: Joins
 * November 18, 2019
 */

 /**
  * Attribtue represents a database attribtue: containing an attribute name.
  */
public class Attribute{
  String attrName;

  public Attribute(String name){
    attrName = name;
  }

  /**
   * Get attribute name
   * @return attribute name
   */
  public String getName(){
    return attrName;
  }

  /**
   * Check equality of this and another attribute
   * @param other another attribute
   * @return true if this and other attribute have the same name 
   */
  public Boolean equals(Attribute other){
    if (attrName.equals(other.getName())){
      return true;
    }
    return false;
  }
}
