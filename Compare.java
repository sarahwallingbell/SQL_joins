/*
 * Sarah Walling-Bell
 * CS 455: Databases
 * Hwk 6: Joins
 * November 18, 2019
 */

import java.util.Comparator;
/**
 * Compares two tuples on a chosen attribute.
 */
public class Compare implements Comparator<Tuple>{
  private int idx;

  public Compare(int index){
    idx = index;
  }
  /**
   * Compare two tuples on attribute idx. 
   * @return int from compareTo on tuple strings
   */
  public int compare(Tuple a, Tuple b){
    String aCommon = a.getData().get(idx);
    String bCommon = b.getData().get(idx);

    return aCommon.compareTo(bCommon);
  }

}
