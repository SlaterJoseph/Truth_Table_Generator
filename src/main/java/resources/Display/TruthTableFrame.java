package resources.Display;

import javax.swing.*;
import java.awt.*;

public class TruthTableFrame{
    private final boolean[][] results;
    String[] formulaRow;
    JPanel truthPanel = new JPanel();
    GridLayout gridLayout;

    TruthTableFrame(boolean[][] results, String[] formulaRow){
        this.results = results;
        this.formulaRow = formulaRow;
        truthPanel.setLayout(new BorderLayout());
        truthPanel.setSize(500, 500);
        setFormulaPortion();
        setResultsPortion();

    }

    public JPanel getTruthPanel() {
        return truthPanel;
    }
    
    private void setFormulaPortion(){
        JPanel formulaPanel = new JPanel(new GridLayout(formulaRow.length, 1));
        formulaPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        for(String formulaPart: formulaRow){
            TextField currPortion = new TextField(formulaPart);
            currPortion.setEditable(false);
            formulaPanel.add(currPortion);
        }
        truthPanel.add(formulaPanel, BorderLayout.LINE_START);
    }
    
    private void setResultsPortion(){
        gridLayout = new GridLayout(results.length, results[0].length);
        JPanel resultsPanel = new JPanel(gridLayout);
        resultsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        for(int i = 0; i < results.length; i++){
            for(int j = 0; j < results[i].length; j++){
                boolean curr = results[i][j];
                TextField currPortion = new TextField(Boolean.toString(curr).toUpperCase());
                currPortion.setEditable(false);

                Color boxColor = curr ? Color.GREEN : Color.RED;
                currPortion.setBackground(boxColor);
                resultsPanel.add(currPortion);
            }
        }
        truthPanel.add(resultsPanel);
    }
}
