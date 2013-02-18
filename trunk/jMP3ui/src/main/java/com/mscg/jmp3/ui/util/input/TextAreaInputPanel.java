package com.mscg.jmp3.ui.util.input;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TextAreaInputPanel extends InputPanel {

    private static final long serialVersionUID = -1931812689413356851L;

    protected JComponent component;
    protected JTextArea textArea;

    protected Integer panelHeight;
    protected String helpText;

    public TextAreaInputPanel(String label, int topBorder, int bottomBorder) {
        this(label, label, topBorder, bottomBorder);
    }

    public TextAreaInputPanel(String label) {
        this(label, label);
    }

    public TextAreaInputPanel(String helpText, String label, int topBorder, int bottomBorder) {
        super(label, topBorder, bottomBorder, false);
        this.helpText = helpText;
        initComponent(topBorder, bottomBorder);
    }

    public TextAreaInputPanel(String helpText, String label) {
        this(helpText, label, defaultTopBorder, defaultBottomBorder);
    }

    @Override
    protected Integer getPanelHeight(Component valueBox) {
        if(panelHeight == null)
            panelHeight = 5 * super.getPanelHeight(valueBox);
        return panelHeight;
    }

    @Override
    public Component getValueComponent() {
        if(component == null) {
            textArea = new JTextArea();
            textArea.setFont(new JTextField().getFont());
            getPanelHeight(textArea);
            textArea.setToolTipText(helpText);
            JScrollPane scroller = new JScrollPane(textArea);
            scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            setComponentDimensions(scroller, panelHeight, 0, 0);
            component = scroller;
        }
        return component;
    }

    @Override
    public String getValue() {
        return textArea.getText();
    }

    @Override
    public void setValue(String value) {
        textArea.setText(value);
    }

}
