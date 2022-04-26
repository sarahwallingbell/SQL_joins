/*
 * Sarah Walling-Bell
 * CS 455: Databases
 * Hwk 6: Joins
 * November 18, 2019
 */

import java.util.*;
/**
 * Relation represents a database relation: containing a list of attributes,
 * a list of tuples, and perhaps an attribute that the tuples are sorted on.
 */
public class Relation{

  private String name;
  private ArrayList<Attribute> attributes;
  private ArrayList<Tuple> tuples;
  private Attribute sortBy;
  private int sortByIdx;

  /**
   * Create an UNNAMED relation
   */
  public Relation(){
    attributes = new ArrayList<>();
    tuples = new ArrayList<>();
    sortBy = null;
    sortByIdx = -1;
    name = null;
  }

  /**
   * Create a NAMED relation
   */
  public Relation(String n){
    attributes = new ArrayList<>();
    tuples = new ArrayList<>();
    sortBy = null;
    sortByIdx = -1;
    name = n;
  }

  /**
   * Get attribute tuples are sorted on
   * @return sortBy attribute
   */
  public Attribute getSortBy(){
    return sortBy;
  }

  /**
   * Get relation name
   * @return relation name
   */
  public String getName(){
    return name;
  }

  /**
   * Adds a single attribute
   * @param a attribute to add
   */
  public void addAttribute(Attribute a){
    attributes.add(a);
  }

  /**
   * Add multiple attributes
   * @param attrs ArrayList of attribtues to add
   */
  public void addAttributes(ArrayList<Attribute> attrs){
    attributes = attrs;
  }

  /**
   * Get attributes
   * @return attributes
   */
  public ArrayList<Attribute> getAttributes(){
    return attributes;
  }

  /**
   * Get index of attribute
   * @param attrName name of attribute
   * @return index of attribute
   */
  public int getAttributeIndex(String attrName){
    for (int i = 0; i < attributes.size(); i++){
      if (attributes.get(i).getName().equals(attrName)){
        return i;
      }
    }
    return -1;
  }

  /**
   * Add sort attribtue
   * @param sort Attribute
   */
  public void addSortByAttribute(Attribute sort){
    sortBy = sort;
    sortByIdx = getAttributeIndex(sortBy.getName());
  }

  /**
   * Get sortByIdx
   * @return sortByIdx
   */
  public int getSortByIdx(){
    return sortByIdx;
  }
  /**
   * Get tuples
   * @return ArrayList of all tuples
   */
  public ArrayList<Tuple> getTuples(){
    return tuples;
  }

  /**
   * Get number of tuples in attribute
   * @return number of tuples
   */
  public int numTuples(){
    return tuples.size();
  }

  /**
   * Add a tuple
   * @param t tuple to add
   */
  public void addTuple(Tuple t){
      tuples.add(t);
  }

  /**
   * Check if common attribute between this and another relation exists
   * @param other another relation
   * @return true if common attribute between this and other relation exists
   */
  public Boolean commonAttribute(Relation other){
    for (int i = 0; i < attributes.size(); i++){
      for (int j = 0; j < other.getAttributes().size(); j++){
        if (attributes.get(i).equals(other.getAttributes().get(j))){
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get common attribute between this and another relation
   * @param other another relation
   * @return common attribute between this and other relation
   */
  public Attribute getCommonAttribute(Relation other){
    for (int i = 0; i < attributes.size(); i++){
      for (int j = 0; j < other.getAttributes().size(); j++){
        if (attributes.get(i).equals(other.getAttributes().get(j))){
          return attributes.get(i);
        }
      }
    }
    return null;
  }

  /**
   * Check if relation contains an attribute
   * @param a attribute to check if exists in this relation
   * @return true if relation contains attribute a
   */
  private Boolean containsAttribute (Attribute a){
    for (int i = 0; i < attributes.size(); i++){
      if (attributes.get(i).getName().equals(a.getName())){
        return true;
      }
    }
    return false;
  }


  /**
   * Get String representation of relation
   * @return string representation of relation
   */
  public String toString(){
    String ret = "";

    for (int i = 0; i < attributes.size(); i++){
      ret += attributes.get(i).getName() + "| ";
    }
    for (int i = 0; i < tuples.size(); i++){
      ret += "\n" + tuples.get(i).toString();
    }
    return ret;
  }

}
