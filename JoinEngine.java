/**
 * Sarah Walling-Bell
 * CS 455: Databases
 * Hwk 6: Joins
 * November 18, 2019
 */

import java.io.*;
import java.util.*;

/**
 * Loads .txt files from data folder into database. Presents user with relations
 * on which to perfom joins. Performs Nested, Hash, or Sort-Merge natural join
 * between two relations. If no common attribute exists between the relations,
 * the user is notified.
 */
public class JoinEngine {

    public static void main(String[] args) {

        try {
            File f = new File("data"); //folder to search
            // Create a FileFilter
            FileFilter filter = new FileFilter() {
                public boolean accept(File f){
                    // Keep files that don't start with . and end with txt
                    if (f.getName().endsWith("txt") && !(f.getName().startsWith("."))){
                      return true;
                    }
                    return false;
                }
            };
            // Get names of files in data directory that weren't filtered out
            File[] files = f.listFiles(filter);

            //create a map to store relations (file name -> relation)
            Map<String,Relation> relations = new HashMap<String,Relation>();

            // Read in and create Relation object for each .txt file
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                String relationName = fileName.replace(".txt", "");

                Relation relation = new Relation(relationName);

                //Read file, parse and add attributes and tuples
                File file = new File("data/" + fileName);
                Scanner sc = new Scanner(file);

                int lineCounter = 0;
                while (sc.hasNextLine()){
                  String line = sc.nextLine();
                  //ADD ATTRIBUTES
                  if ((lineCounter == 0) && (line.charAt(0) == '#')){
                    //Parse and add attributes to relation
                    StringTokenizer st = new StringTokenizer(line,"[#|]+");
                    while (st.hasMoreTokens()) {
                      String attr = st.nextToken();
                      //create and add attribute
                      Attribute a = new Attribute(attr);
                      relation.addAttribute(a);
                    }
                  }
                  //ADD SORT BY ATTRIBUTE
                  else if ((lineCounter == 1) && (line.charAt(0) == '#')){
                    //Parse and add sortBy attribute to relation
                    String sortBy = line.substring(1);
                    Attribute s = new Attribute(sortBy);
                    relation.addSortByAttribute(s);
                  }
                  //ADD TUPLES
                  else{
                    //create and add tuple to relation
                    ArrayList<String> data = new ArrayList<String>();
                    StringTokenizer st = new StringTokenizer(line,"|");
                    while (st.hasMoreTokens()) {
                      String d = st.nextToken();
                      data.add(d);
                    }
                    Tuple tuple = new Tuple(data);
                    relation.addTuple(tuple);
                  }

                  lineCounter++;
                }
                //add relation to relations map
                relations.put(relationName, relation);
            }

            //PROMPT USER TO PERFORM JOINS
            System.out.print("Available Relations: \n\n\t");
            Set<String> availableRels = relations.keySet();
            for (String relName: availableRels){
              System.out.print(relName + " ");
            }
            System.out.print("\n\nYour selection (separated by space): ");
            Scanner in = new Scanner(System.in);
            String rRelName = null;
            String sRelName = null;

            Boolean valid = false;
            while (!valid){
              String userInput = in.nextLine();
              StringTokenizer stk = new StringTokenizer(userInput, " ");
              if (stk.hasMoreTokens()) {
                rRelName = stk.nextToken();
                if (stk.hasMoreTokens()) {
                  sRelName = stk.nextToken();
                  valid = true;
                }
              }
              //If the user didn't enter two strings separated by a space, re-prompt them.
              if (!valid) System.out.print("Invalid entry, try again: ");
            }
            //If the user entered invalid relations, throw IllegalArgumentException
            if (!availableRels.contains(rRelName) || !availableRels.contains(sRelName)){
              throw new IllegalArgumentException("Invalid relations.");
            }
            else{
              //Prompt user for join algorithm
              System.out.println("Choose a join algorithm:");
              System.out.println("1. Nested loop join");
              System.out.println("2. Hash Join");
              System.out.println("3. Sort-Merge Join");
              System.out.print("Your selection: ");
              int userJoinChoice = -1;
              valid = false;
              while (!valid){
                userJoinChoice = in.nextInt();
                if (userJoinChoice > 0 && userJoinChoice < 4){
                  valid = true;
                }
                else{
                  //reprompt user for valid input
                  System.out.print("Invalid number, try again: ");
                }
              }

              Relation R = relations.get(rRelName);
              Relation S = relations.get(sRelName);
              double time = -1;
              Joins j = new Joins();
              Relation ret = null;

              //perform and time joins
              if (userJoinChoice == 1){
                double start = System.nanoTime();
                ret = j.nestedLoopJoin(R, S);
                double finish = System.nanoTime();
                time = (finish - start) / 1000000.0;
              }
              if (userJoinChoice == 2){
                double start = System.nanoTime();
                ret = j.hashJoin(R, S);
                double finish = System.nanoTime();
                time = (finish - start) / 1000000.0;
              }
              if (userJoinChoice == 3){
                double start = System.nanoTime();
                ret = j.sortMergeJoin(R, S);
                double finish = System.nanoTime();
                time = (finish - start) / 1000000.0;
              }

              //print results
              System.out.println(ret.toString());
              System.out.println("\nTime = "+ time +" ms");
              System.out.println("Number of rows = "+ ret.numTuples());

            }

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
