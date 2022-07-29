package resources.Processing;

import java.util.HashMap;
import java.util.Map;

public class SetArraySizes {
    private final int boolRowSize;
    private final int boolColSize;
    private String formula;
    private int numVar;

    public SetArraySizes(String formula){
        this.formula = formula.toLowerCase();
        boolRowSize = setBoolRowSize();
        boolColSize = setBoolColSize();
    }

    /**
     * The number of rows in a truth table cna be found by 2^(Number of variables)
     * So if the equation is x || y, the formula is 2^2, so it is 4.
     * If its x || y && z then it's 2^3, which is 8
     * @return int
     */
    private int setBoolRowSize(){
        HashMap<Character, Integer> numRows = new HashMap<>();

        for(char c: formula.toCharArray()){
            /**
             * This if statement checks that the char (c) is a lowercase letter
             * (AKA a variable) and if it is one and has not been detected before
             * its added
             */
            if(c >= 97 && c <= 122 && !numRows.containsKey(c)){
                numRows.put(c, 1);
            }
        }
        numVar = numRows.size();
        return (int) Math.pow(2, numRows.size());
    }

    /**
     * The number of columns in the truth table is equal to each individual var
     * plus every instance of an operation. So x || y would have 3 columns, x, y,
     * and x || y
     * @return int
     */
    private int setBoolColSize(){
        HashMap<Character, Integer> numColumn = new HashMap<>();
        /**
         * I put all the possible operators in the HashMap from the start so
         * the value can be incremented when found
         */
        numColumn.put('\u22C0', 0);
        numColumn.put('\u22C1', 0);
        numColumn.put('\u00AC', 0);
        numColumn.put('\u21D2', 0);
        numColumn.put('\u21D4', 0);
        numColumn.put('\u2295', 0);

        for(char c: formula.toCharArray()){
            /**
             * This if statement checks that the char (c) is a lowercase letter
             * (AKA a variable) and if it is one and has not been detected before
             * it's added
             */
            if(c >= 97 && c <= 122 && !numColumn.containsKey(c)){
                numColumn.put(c, 1);
            }
            /**
             * This next portion of the else if tests that the char (c) is an operator
             * and if it is it increments the value associated with the key
             */
            else if (c == '\u22C0' || c == '\u22C1' || c == '\u00AC' || c == '\u21D2'
                    || c == '\u21D4' || c == '\u2295'){
                numColumn.put(c, numColumn.get(c) + 1);
            }
        }

        int returnValue = 0;
        /**
         * This for loop processes the formula in order to figure out the number to
         * return. If a var is the key detected, it increments the return value by one,
         * and if an operator is the next key detected it increments the return value
         * by the value associated with the key
         */
        for(Map.Entry element: numColumn.entrySet()){
            char c = (char) element.getKey();
            if(c >= 97 && c <= 122){
                returnValue++;
            } else {
                returnValue += (int) element.getValue();
            }
        }
        return returnValue;
    }

    public int getBoolRowSize() { return boolRowSize; }
    public int getBoolColSize() { return boolColSize; }
    public int getNumVar() { return numVar; }
}
