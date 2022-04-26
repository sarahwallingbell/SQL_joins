/*
 * Sarah Walling-Bell
 * CS 455: Databases
 * Hwk 6: Joins
 * November 18, 2019
 */

 /**
  * Tuple represents a database tuple: containing a list of data.
  */
import java.util.ArrayList;

public class Tuple{
  private ArrayList<String> data;

  public Tuple (ArrayList<String> d){
    data = d;
  }

  /**
   * Get tuple data
   * @return tuple data
   */
  public ArrayList<String> getData(){
    return data;
  }

  /**
   * Get specific data value
   * @param i index of data value
   * @return data at index i
   */
  public String getValue(int i){
    return data.get(i);
  }

  /**
   * Add single data value
   * @param d data value to add
   */
  public void addData(String d){
    data.add(d);
  }

  /**
   * Get String representation of tuple
   * @return string representation of tuple
   */
  public String toString(){
    String ret = "";
    for (int i = 0; i < data.size(); i++){
      ret += data.get(i);
      ret += "| ";
    }
    return ret;
  }

}
