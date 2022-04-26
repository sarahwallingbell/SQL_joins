/**
 * Sarah Walling-Bell
 * CS 455: Databases
 * Hwk 6: Joins
 * November 18, 2019
 */

import java.util.*;

/**
 * Performs a natural join between two relations if they share a common attribute.
 * If they do not, an Exception is thrown. The natural join is preformed through
 * a nested loop join, hash join, or sort-merge join.
 */
public class Joins{

  /**
   * Performs a nested loop join if the relations share a common attribute.
   * @param R the left relation in the natural join
   * @param S the right relation in the natural join
   * @return Natural join of R and S
   */
  public Relation nestedLoopJoin(Relation R, Relation S) throws Exception {

    // Find a common attribute (if one exists)
    if (!R.commonAttribute(S)){
      throw new Exception("No common attribute.");
    }
    else{
    // Relations have a common attribute, compute the join
    Relation T = new Relation();

    //Find common attribute
    Attribute common = R.getCommonAttribute(S);
    int commonAttrIdxR = R.getAttributeIndex(common.getName());
    int commonAttrIdxS = S.getAttributeIndex(common.getName());

    //Add attributes
    ArrayList<Attribute> AttrsR = R.getAttributes();
    ArrayList<Attribute> newAttrsS = S.getAttributes();
    newAttrsS.remove(commonAttrIdxS);
    ArrayList<Attribute> newAttrs = AttrsR;
    newAttrs.addAll(newAttrsS);
    T.addAttributes(newAttrs);

    //Get all tuples
    ArrayList<Tuple> rTuples = R.getTuples();
    ArrayList<Tuple> sTuples = S.getTuples();

    for (int tupR = 0; tupR < rTuples.size(); tupR++){
      Tuple r = rTuples.get(tupR);
      for (int tupS = 0; tupS < sTuples.size(); tupS++){
        Tuple s = sTuples.get(tupS);

        //If the common attributes for tuples r and s are the same, add to Relation T
        if (r.getData().get(commonAttrIdxR).equals(s.getData().get(commonAttrIdxS))){
          ArrayList<String> dataR = r.getData();
          ArrayList<String> newDataS = s.getData();
          ArrayList<String> newData = new ArrayList<>();

          for (int i = 0; i < dataR.size(); i++){
            newData.add(dataR.get(i));
          }
          for (int i = 0; i < newDataS.size(); i++){
            if (i != commonAttrIdxS){
              newData.add(newDataS.get(i));
            }
          }

          Tuple tuple = new Tuple(newData);
          T.addTuple(tuple);
        }
      }
    }
    //Return Relation T with the join of R and S
    return T;
    }
  }

  /**
   * Performs a hash join if the relations share a common attribute, and the
   * common attribute of relation R is unique.
   * @param R the left relation in the natural join
   * @param S the right relation in the natural join
   * @return Natural join of R and S
   */
  public Relation hashJoin(Relation R, Relation S) throws Exception {

    if (!R.commonAttribute(S)){
      throw new Exception("No common attribute.");
    }
    else{
      Relation T = new Relation();

      Attribute common = R.getCommonAttribute(S);
      int commonAttrIdxR = R.getAttributeIndex(common.getName());
      int commonAttrIdxS = S.getAttributeIndex(common.getName());

      // Phase I: Hash every tuple of R by the value of the common attribute
      HashMap<String, Tuple> map = new HashMap<>();

      ArrayList<Tuple> rTuples = R.getTuples();
      for (int tupR = 0; tupR < rTuples.size(); tupR++){
        Tuple r = rTuples.get(tupR);
        if (!map.containsKey(r.getData().get(commonAttrIdxR))){
          map.put(r.getData().get(commonAttrIdxR), r);
        }
        else{
          // common attribute is not R's primary key, so hash join cannot be performed
          throw new Exception("Hash-join cannot be performed because common attribute in " + R.getName() + " is not unique.");
        }
      }

      // Phase II: Join up with S
      ArrayList<Tuple> sTuples = S.getTuples();
      for (int tupS = 0; tupS < sTuples.size(); tupS++){
        Tuple s = sTuples.get(tupS);
        //search for tuple's common attribute value in hashmap
        if (map.containsKey(s.getData().get(commonAttrIdxS))){
          //add tuple to return relaton T
          Tuple t = map.get(s.getData().get(commonAttrIdxS));
          ArrayList<String> dataR = t.getData();
          ArrayList<String> newDataS = s.getData();
          ArrayList<String> newData = new ArrayList<>();

          for (int i = 0; i < dataR.size(); i++){
            newData.add(dataR.get(i));
          }
          for (int i = 0; i < newDataS.size(); i++){
            if (i != commonAttrIdxS){
              newData.add(newDataS.get(i));
            }
          }

          Tuple newTuple = new Tuple(newData);
          T.addTuple(newTuple);
        }
      }
      return T;
    }
  }

  /**
   * Performs a sort-merge join if the relations share a common attribute. If
   * either relation is not sorted on the common attribute, it first sorts them so.
   * @param R the left relation in the natural join
   * @param S the right relation in the natural join
   * @return Natural join of R and S
   */
  public Relation sortMergeJoin(Relation R, Relation S) throws Exception {

    if (!R.commonAttribute(S)){
      throw new Exception("No common attribute.");
    }
    else{
      //find common attribute
      Attribute common = R.getCommonAttribute(S);
      int commonAttrIdxR = R.getAttributeIndex(common.getName());
      int commonAttrIdxS = S.getAttributeIndex(common.getName());

      //make sure R and S are both sorted on their common attribute
      if (!R.getSortBy().getName().equals(common.getName())){
        Compare compR = new Compare(commonAttrIdxR);
        Collections.sort(R.getTuples(), compR);
      }
      if (!S.getSortBy().getName().equals(common.getName())){
        //S.resort(common);
        Compare compS = new Compare(commonAttrIdxS);
        Collections.sort(S.getTuples(), compS);
      }

      Relation T = new Relation();

      int i = 0;
      int j = 0;
      while (i < R.numTuples() && j < S.numTuples()){
        int compare = R.getTuples().get(i).getData().get(commonAttrIdxR).compareTo(S.getTuples().get(j).getData().get(commonAttrIdxS));
        if (compare == 0){
          // Match found, enter merge phase
          while ((i < R.numTuples()) && (R.getTuples().get(i).getData().get(commonAttrIdxR).equals(S.getTuples().get(j).getData().get(commonAttrIdxS)))){
            int k = j;
            //stay in merge phase while common attribute values match
            while ((k < S.numTuples()) && (R.getTuples().get(i).getData().get(commonAttrIdxR).equals(S.getTuples().get(k).getData().get(commonAttrIdxS)))){
              //create and add new tuple to return relation T
              ArrayList<String> dataR = R.getTuples().get(i).getData();
              ArrayList<String> newDataS = S.getTuples().get(k).getData();
              ArrayList<String> newData = new ArrayList<>();

              for (int x = 0; x < dataR.size(); x++){
                newData.add(dataR.get(x));
              }
              for (int x = 0; x < newDataS.size(); x++){
                if (x != commonAttrIdxS){
                  newData.add(newDataS.get(x));
                }
              }

              Tuple newTuple = new Tuple(newData);
              T.addTuple(newTuple);

              k++;
            }
            i++;
          }
        }
        //no match found, increment a pointer
        else if (compare < 0){
          i++;
        }
        else{
          j++;
        }
      }
      return T;
    }
  }
}
