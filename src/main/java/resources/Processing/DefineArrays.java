package resources.Processing;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * This class contains methods to set the variable
 * values in the results array as well as define
 * the entire formula array
 */
public class DefineArrays {
    private final boolean[][] results;
    private int numVar;
    private final String formula;
    private final String[] formulaArray;
    private int currIndex;

     DefineArrays(boolean[][] results, String formula, int formulaArrSize){
        this.results = results;
        this.formula = formula;
        formulaArray = new String[formulaArrSize];
        setFormulaArray();
        defineResults();
    }

    /**
     * This method processes the variable columns.
     * So in equation x || y || z, there are three variables,
     * so those variables are set first
     *
     * The equation 2^(x - (i + 1)) shows what portions of the array must
     * be true, then false, the true and repeated. So if there is 3 variables
     * x flips every 4, y flips every 2, z flips every 1
     */
    private void defineResults(){
        for(int i = 0; i < numVar; i++){
            int truthSet = (int) Math.pow(2, numVar - (i + 1));
            int testAgainst = truthSet;
            boolean toTrue = true;

            /**
             * This loop places the correct truth value in the 2D
             * array. When a cycle is finished(4 -> 3 -> 2 -> 1 -> 0)
             * The truth value is swapped with its opposite, and the
             * variable used to check how many more times the current
             * truth variable is needed to reset.
             *
             * So for y where the value must be swapped every 2 steps it goes
             * Truth - 2 -> 1 -> 0(which is never processed but reset to 2)
             * False - 2 -> 1 -> 0
             * Truth - 2 -> 1 -> 0
             * False - 2 -> 1 -> 0(This is the last 0, so the for loop ends)
             */
            for(int j = 0; j < results[i].length; j++){
                if(testAgainst == 0){
                    testAgainst = truthSet;
                    toTrue = !toTrue;
                }

                results[i][j] = toTrue;
                testAgainst--;
            }
        }
    }

    /**
     * This method places the first x columns of the title array
     * with the correct variables. So if the equation is x || y && z
     * This method places the first three columns as x, then y, then z
     */
    private void setFormulaArray(){
        /**
         * And ArrayList was used over an array as the number variables can
         * be different every time the code is utilized, as well as the contains
         * method of the ArrayList to test if a char has been placed before
         */
        ArrayList<Character> allVars = new ArrayList<>();
        for(char c: formula.toCharArray()){
            if(c >= 97 && c <= 122){
                if(!allVars.contains(c)){
                    allVars.add(c);
                }
            }
        }
        numVar = allVars.size(); //This is just setting the numVar variable for use later
        for(int i = 0; i < numVar; i++){
            formulaArray[i] =  allVars.get(i).toString();
        }
        defineFormulaArray();
    }

    private void defineFormulaArray(){
        Deque<Character> stack = new ArrayDeque<>();
        stack.push('='); //This is an empty char that will never be reached. It's pushed
                            //to prevent null point exceptions
        for(int i = formula.length() - 1; i >= 0; i--){
            stack.push(formula.charAt(i));
        }
        currIndex = numVar;

        while(stack.size() > 1){
            char c = stack.pop();
            String portion = "";

            switch(c) {
                case '(':
                    paraDetected(stack);
                    break;

                case '\u00AC':
                    negationFound(stack);
                    break;

                case '\u22C0','\u22C1','\u21D2','\u21D4','\u2295' :
                    if(stack.peek() == '('){
                        String prevPortion = formulaArray[currIndex - 1];
                        stack.pop(); //removes the opening parenthesis
                        paraDetected(stack);
                        portion = "(" + prevPortion + ")" + c
                                + "(" + formulaArray[currIndex - 1] + ")";
                        formulaArray[currIndex] = portion;

                    } else if (stack.peek() == '\u00AC'){
                        stack.pop();
                        portion += c + negationFound(stack);
                    } else {
                        portion = formulaArray[currIndex - 1] + c + stack.pop();
                        formulaArray[currIndex++] = portion;
                    }
                    break;

                default:
                    portion = Character.toString(c) + stack.pop();

                    if(stack.peek() == '\u00AC'){
                        stack.pop();
                        negationFound(stack);
                    } else if (stack.peek() == '('){
                        stack.pop();
                        paraDetected(stack);
                        formulaArray[currIndex] = portion + "(" + formulaArray[currIndex - 1] + ")";
                    } else {
                        portion += stack.pop();
                        formulaArray[currIndex++] = portion;
                    }
            }
        }

        if(formulaArray[formulaArray.length-1] == null){
            formulaArray[formulaArray.length-1] = formula;
        }
    }

    /**
     * This method is for when negation if found. If the peeked element after the negation is a
     * parenthesis then paraChecker is called, and the returned string has a negation added to the
     * start before it's added to the formulaArray.
     *
     * If the peeked element is a var, a loop runs pulling every element forward, then right the new
     * negation + variable is placed right after the initial variable.
     * @param stack - Utilized deque/stack to be processed
     * @return portion
     */
    private String negationFound(Deque<Character> stack){
        String portion;

        if(stack.peek() == '('){
            stack.pop();
            portion = "\u00AC(" + paraDetected(stack) + ")";
            formulaArray[currIndex++] = portion;
        } else {
            portion = "\u00AC" + stack.pop();
            for(int i = formulaArray.length - 1; i > numVar; i--){
                formulaArray[i] = formulaArray[i-1];
            }
            formulaArray[numVar] = portion;
            currIndex++;
        }
        return portion;
    }

    /**
     * This method is for the case when a parenthesis is detected in the equation. If one is detected
     * the stack is passed to this method, and then it runs the calculations. If another start to a
     * parenthesis is detected, recursion starts by calling the same method again.
     *
     * The loop breaks when a parenthesis that matches the current call's is found.
     * ie : x && (y || z), the ( parenthesis is matched with the ) parenthesis.
     */
    private String paraDetected(Deque<Character> stack){
        int paraCount = 1;
        String portion = "";

        while(paraCount > 0){
            switch(stack.peek()){
                case '(':
                    stack.pop();
                    portion += "(" + paraDetected(stack) + ")";
                    break;

                case ')':
                    paraCount--;
                    stack.pop();
                    break;

                case '\u00AC':
                    stack.pop();
                    portion += negationFound(stack);
                    break;

                default:
                    portion += stack.pop();
            }
        }

        formulaArray[currIndex++] = portion;
        return portion;
    }

    public boolean[][] getResults() { return results; }

    public String[] getFormulaArray() { return formulaArray; }
}
