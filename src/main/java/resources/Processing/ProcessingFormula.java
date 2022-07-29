package resources.Processing;

import java.util.*;

public class ProcessingFormula extends Operation{
    private final String[] topRow;
    private boolean[][] results;
    private final int numVar;
    private int countToAdd = 0;

    public ProcessingFormula(String preEdit){
        String currPortion = cleanUpFormula(preEdit.toLowerCase());

        /**
         * The SetArraySizes class is utilized in the constructor
         * to set the size for both the top row saying what each column
         * refers to and 2D array for the results
         */
        SetArraySizes sizes = new SetArraySizes(currPortion);
        results = new boolean[sizes.getBoolColSize()][sizes.getBoolRowSize()];
        numVar = sizes.getNumVar();

        DefineArrays defineArrays = new DefineArrays(results, currPortion, sizes.getBoolColSize());
        topRow = defineArrays.getFormulaArray();
        results = defineArrays.getResults();
        processValueArray();
    }

    /**
     * This method cleans up the formula. It uses StringBuilder to remove all characters which
     * are not variables, parentheses, or an operator and changes the formula field
     * to the new clean string
     */
    private String cleanUpFormula(String preEdit){
        StringBuilder sb = new StringBuilder(preEdit);
        int charsDeleted = 0;
        for(int i = 0; i < preEdit.length(); i++){
            char c = preEdit.charAt(i);
            if(!((c >= 97 && c <= 122 ) || c == 40 || c == 41 ||
                    c == '\u22C0' || c == '\u22C1' || c == '\u00AC'
                    || c == '\u21D2' || c == '\u21D4' || c == '\u2295')){
                sb.deleteCharAt(i - charsDeleted);
                charsDeleted++;
            }
        }
        return sb.toString();
    }

    public String[] getTopRow() { return topRow; }
    public boolean[][] getResults() { return results; }

    /**
     * This method starts the process of finding the truth values for the tables
     * It initializes a hashmap which has keys of variables and values of booleans
     * Then it takes the current formula portion being processed and switches out
     * the variables for the boolean values
     */
    private void processValueArray(){
        HashMap<Character, Boolean> variables = new HashMap<>();
        int colCount = 0;

        for(int j = 0; j < results[0].length; j++){

            /**
             * This loop puts each variable's current value in a hashmap
             * with the key being the char and the value being the boolean
             */
            for(int i = 0; i < numVar; i++){
                if(!variables.containsKey(topRow[i].charAt(0))){
                    variables.put(topRow[i].charAt(0), results[i][colCount]);
                }
            }

            /**
             * This loop takes the formula of the current column,
             * then switches out the var for their boolean counterpart
             */
            for(int i = numVar; i < results.length; i++){
                String currFormula = topRow[i];
                String processedFormula = "";

                for(char c: currFormula.toCharArray()){
                    processedFormula = switch(c){
                        case '\u22C0', '\u22C1', '\u00AC', '\u21D2', '\u21D4', '\u2295' , ')', '(':
                            yield processedFormula + c;

                        default:
                            yield processedFormula + variables.get(c);
                    };
                }
                results[i][colCount] = processingOperations(processedFormula);

            }
            colCount++;
            variables.clear(); //Cleans out the hashmap
        }
    }

    /**
     * This method contains all the computations(with the exclusion of negation)
     *
     * There is 3 booleans, bool1, bool2, and firstPC (for first processed check)
     *
     * On the first call, bool1 can only run into 1 of 4 things assuming you have valid input
     * t,f,(, or negation, so those 4 cases are dealt with, the firstPC is made true
     *
     * Once firstPC is made true, the only thing that can be run into is ony of the operators
     * Now, an operator reacts differently based on the next char, which is processed
     *
     * If it is a (, paraChecker is called
     * If it is a negation, processNegation is called
     * If it a t or f, the boolean is processed (and stored in bool2)
     * and computed with the operand along with bool1
     *
     * That boolean is then stored in bool1, and the process repeats until the loop is complete.
     * Once completed, bool1 (AKA the fully processed bool is returned
     *
     * @param formula - the formula portion being processed
     * @return a processed boolean
     */
    private boolean processingOperations(String formula){
        boolean bool1 = false;
        boolean bool2;
        boolean firstPC = false;

        for(int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (!firstPC) {
                switch (c) {
                    case 't':
                        bool1 = true;
                        i += 3;
                        break;

                    case 'f':
                        i += 4;
                        break;

                    case '(':
                        bool1 = paraDetected(formula, i);
                        i += countToAdd;
                        break;

                    case '\u00AC':
                        bool1 = processNegation(formula, i);
                        i += countToAdd;
                        break;
                }
                firstPC = true;
            } else {
                switch (c) {
                    case '\u22C0':
                        if(formula.charAt(i+1) == '(') {
                            bool2 = paraDetected(formula, ++i);
                            i += countToAdd;
                        } else if (formula.charAt(i+1) == '\u00AC'){
                            bool2 = processNegation(formula, ++i);
                            i += countToAdd;
                        } else {
                            bool2 = formula.charAt(++i) == 't';
                            i = (bool2) ? i + 3 : i + 4;
                        }
                        bool1 = andOp(bool1, bool2);
                        break;

                    case '\u22C1':
                        if(formula.charAt(i+1) == '('){
                            bool2 = paraDetected(formula, ++i);
                            i += countToAdd;
                        } else if (formula.charAt(i+1) == '\u00AC') {
                            bool2 = processNegation(formula, ++i);
                            i += countToAdd;
                        } else {
                            bool2 = formula.charAt(++i) == 't';
                            i = (bool2) ? i + 3 : i + 4;
                        }
                        bool1 = orOp(bool1, bool2);
                        break;

                    case '\u21D2':
                        if(formula.charAt(i+1) == '('){
                            bool2 = paraDetected(formula, ++i);
                            i += countToAdd;
                        } else if (formula.charAt(i+1) == '\u00AC') {
                            bool2 = processNegation(formula, ++i);
                            i += countToAdd;
                        } else {
                            bool2 = formula.charAt(++i) == 't';
                            i = (bool2) ? i + 3 : i + 4;
                        }
                        bool1 = ifOp(bool1, bool2);
                        break;

                    case '\u21D4':
                        if(formula.charAt(i+1) == '('){
                            bool2 = paraDetected(formula, ++i);
                            i += countToAdd;
                        } else if (formula.charAt(i+1) == '\u00AC') {
                            bool2 = processNegation(formula, ++i);
                            i += countToAdd;
                        } else {
                            bool2 = formula.charAt(++i) == 't';
                            i = (bool2) ? i + 3 : i + 4;
                        }
                        bool1 = ifOIOp(bool1, bool2);
                        break;

                    case '\u2295':
                        if(formula.charAt(i+1) == '('){
                            bool2 = paraDetected(formula, ++i);
                            i += countToAdd;
                        } else if (formula.charAt(i+1) == '\u00AC') {
                            bool2 = processNegation(formula, ++i);
                            i += countToAdd;
                        } else {
                            bool2 = formula.charAt(++i) == 't';
                            i = (bool2) ? i + 3 : i + 4;
                        }
                        bool1 = orCOp(bool1, bool2);
                        break;
                }
            }
        }
        return bool1;
    }

    /**
     * This method processes any negations found inside the formula.
     * If the next char after the negation is a opening parenthesis, paraDetected is called
     *
     * If it's a char, the opposite bool value of the char (false for t, true for f) is
     * assigned and count to add is incremented the proper amount.
     * @param formula - the formula
     * @param index - where the negation char can be found
     * @return boolean - the boolean value opposite of what the inputted value was
     */
    private boolean processNegation(String formula, int index){
        boolean bool;

        if((formula.charAt(index+1) == '(')){
            bool = !paraDetected(formula, index);
        } else {
            bool = formula.charAt(index + 1) != 't';
            countToAdd = formula.charAt(index + 1) == 't' ? countToAdd + 4 : countToAdd + 5;
        }

        return bool;
    }

    /**
     * This method processes parenthesis when they are detected and returns the boolean
     * If another parenthesis is detected, it is processed and placed as one of the boolean
     * values of the parent call of the method.
     *
     * count is used as apposed to i, that way if recursion called, the count isn't restarted wherever
     * the previous call left off. It is changed to what the 2nd call processed so repetition is avoided
     *
     * @param formula - The given formula which will be processed
     * @param index - Where the parenthesis begin
     * @return boolean - The solution of the operations inside the parenthesis
     */
    private boolean paraDetected(String formula, int index){
        boolean bool1 = false;
        boolean bool2 = false;
        boolean done1 = false;
        char op = ' ';
        char c;
        countToAdd = 0;

        for(int count = index + 1; count < formula.length(); count++){
            if(countToAdd > count){
                count = countToAdd + 1;
            } else {
                countToAdd = count;
            }

            //This is here in case count has overflowed passed the array, in which case it is
            //lowered by 1
            try {
                c = formula.charAt(count);
            } catch (Exception e){
                c = formula.charAt(count - 1);
            }

            switch (c){
                case ')':
                    return processingOperations(bool1 + Character.toString(op) + bool2);

                /**
                 * If another parenthesis is detected, the paraDetected method is called
                 * again and recursion starts. It breaks when a closing parenthesis is
                 * detected, then you return to the previous method call
                 */
                case '(':
                    if(done1){
                        bool2 = paraDetected(formula, count);
                    } else {
                        bool1 = paraDetected(formula, count);
                        done1 = true;
                    }
                    break;

                /**
                 * If a negation is detected, the processNegation method is called
                 */
                case '\u00AC':
                    if(done1){
                        bool2 = processNegation(formula, count);
                    } else {
                        bool1 = processNegation(formula, count);
                        done1 = true;
                    }
                    break;

                case '\u22C1','\u21D2','\u22C0','\u21D4','\u2295':
                    op = c;
                    break;

                default:
                    if(done1){
                        bool2 = (formula.charAt(count) == 't');
                        count = (bool2) ? count + 3 : count + 4;
                    } else {
                        bool1 = (formula.charAt(count) == 't');
                        count = (bool1) ? count + 3 : count + 4;
                    }
                    done1 = true;
            }
        }
        return false;
    }

    @Override
    /**
     * This method is for testing purposes only
     */
    public String toString() {
        System.out.println("Title Row");
        System.out.println(Arrays.toString(topRow) + "\n");
        System.out.println("\nStart");
        for(int i = 0; i < results.length; i++){
            System.out.print("Column " + topRow[i] + ": | ");
            for(int j = 0; j < results[i].length; j++){
                System.out.print(" " + results[i][j] + " ");
            }
            System.out.println(" |");
        }
        return "End";
    }
}
