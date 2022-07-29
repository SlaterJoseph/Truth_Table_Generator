package resources.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InsertFormulaFrame implements ActionListener{

    private final JPanel formulaPanel;
    private JPanel buttonPanel;
    private JTextField inputTextField;
    String formulaString;
    boolean startClicked = false;


    public JPanel getFormulaPanel(){ return formulaPanel; }

    public boolean isStartClicked() {
        return startClicked;
    }

    public String getFormulaString() {
        return formulaString;
    }

    InsertFormulaFrame(){
        formulaPanel = new JPanel();
        formulaPanel.setLayout(new BorderLayout());
        formulaPanel.setSize(500, 500);
        setTextFields();
        createButtons();
        setFormulaPanel();
    }

    /**
     * andButton is for the AND operation
     * orButton is for the OR operation
     * negButton is for the NEGATION operation
     * ifButton is for the IF operation
     * ifOIButton is for the IF AND ONLY IF operation
     * orCButton is for the CONDITIONAL OR operation
     */

    private void setFormulaPanel(){
        formulaPanel.add(buttonPanel, BorderLayout.PAGE_END);
    }

    private void setTextFields(){
        inputTextField = new JTextField("Please put your truth equation here");
        inputTextField.setSize(100,100);
        formulaPanel.add(inputTextField, BorderLayout.CENTER);

        /**
         * This code deletes the text prompting the user to input their
         * formula ni this field
         */
        inputTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });
    }

    private void createButtons(){

        /**
         * This code initializes the buttonPanel, sizes it
         * and sets the layout to be a GroupLayout while
         * setting up the GroupLayout
         */
        buttonPanel = new JPanel();
        buttonPanel.setSize(300, 200);
        buttonPanel.setPreferredSize(new Dimension(300, 200));
        GroupLayout layout = new GroupLayout(buttonPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        buttonPanel.setLayout(layout);

        /**
         * This code creates all the needed buttons
         */

        JButton andButton = new JButton("\u22C0");
        JButton orButton = new JButton("\u22C1");
        JButton negButton = new JButton("\u00AC");
        JButton ifButton = new JButton("\u21D2");
        JButton ifOIButton = new JButton("\u21D4");
        JButton orCButton = new JButton("\u2295");
        JButton selectButton = new JButton("Start");

        andButton.addActionListener(this);
        orButton.addActionListener(this);
        negButton.addActionListener(this);
        ifButton.addActionListener(this);
        ifOIButton.addActionListener(this);
        orCButton.addActionListener(this);
        selectButton.addActionListener(this);

        formulaPanel.add(selectButton, BorderLayout.LINE_END);

        /**
         * This bunch of code sets up the alignment of the 6
         * operation buttons which print the symbols
         *
         */
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(andButton)
                                .addComponent(ifButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(orButton)
                                .addComponent(ifOIButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(negButton)
                                .addComponent(orCButton))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(andButton)
                                .addComponent(orButton)
                                .addComponent(negButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(ifButton)
                                .addComponent(ifOIButton)
                                .addComponent(orCButton))
                        );

    }

    /**\
     * This code deals with the button clicks. If any button other than start
     * is clicked, the symbol is added. If the start button is clicked the formula
     * is processed and then the next JPanel is displayed.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String currText = inputTextField.getText();
        JButton clickButton = (JButton) e.getSource();
        switch (clickButton.getText().charAt(0)){
                case '\u22C0':
                    inputTextField.setText(currText + '\u22C0');
                    break;
                case '\u22C1':
                    inputTextField.setText(currText + '\u22C1');
                    break;
                case '\u00AC':
                    inputTextField.setText(currText + '\u00AC');
                    break;
                case '\u21D2':
                    inputTextField.setText(currText + '\u21D2');
                    break;
                case '\u21D4':
                    inputTextField.setText(currText + '\u21D4');
                    break;
                case '\u2295':
                    inputTextField.setText(currText + '\u2295');
                    break;
                case 'S':
                    formulaString = inputTextField.getText();
                    startClicked = true;
                    formulaPanel.setVisible(false);
        }


    }
}
