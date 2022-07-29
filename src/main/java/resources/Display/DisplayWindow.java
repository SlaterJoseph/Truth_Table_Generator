package resources.Display;

import javax.swing.*;
import java.awt.*;
import resources.Processing.*;

public class DisplayWindow {
    JFrame jframe = new JFrame("Truth Table Generator");
    InsertFormulaFrame formulaFrame = new InsertFormulaFrame();
    TruthTableFrame truthTableFrame;
    JPanel jPanel1 = formulaFrame.getFormulaPanel();
    JPanel jPanel2;
    private String formula;
    private boolean isSelected;

    public String getFormula() {
        return formula;
    }

    public DisplayWindow(){
        jframe.setSize(500, 500);
        jframe.setPreferredSize(new Dimension(500, 500));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.add(jPanel1);
        jframe.pack();

        while(!isSelected){
            formula = formulaFrame.getFormulaString();
            isSelected = formulaFrame.isStartClicked();
        }
        ProcessingFormula processingFormula = new ProcessingFormula(formula);
        truthTableFrame = new TruthTableFrame(processingFormula.getResults(), processingFormula.getTopRow());
        jPanel2 = truthTableFrame.getTruthPanel();
        jframe.add(jPanel2);
        jframe.pack();

    }
}