package com.mscg.jmp3.ui.util.input;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mp3.ui.MainWindowInterface;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.util.Util;

public abstract class GenericJFileChooserSelectionInputPanel extends InputPanel implements ActionListener {

    private static final long serialVersionUID = -4966883722357387269L;

    protected JPanel wrapperPanel;
    protected JTextField selectedFileField;
    protected JButton selectButton;
    protected String tooltip;

    protected GenericJFileChooserSelectionInputPanel(String label, int topBorder, int bottomBorder,
                                                     String tooltip, boolean init) {
        super(label, topBorder, bottomBorder, init);
        this.tooltip = tooltip;
    }

    protected GenericJFileChooserSelectionInputPanel(String label, String tooltip,
                                                     boolean init) {
        super(label, init);
        this.tooltip = tooltip;
    }

    @Override
    protected void setDimensions(int topBorder, int bottomBorder) {
        super.setDimensions(topBorder, bottomBorder);
        Dimension maxDim = getMaximumSize();
        if(maxDim.height < Util.maxSmallIconButtonSize.height + topBorder + bottomBorder) {
            maxDim = new Dimension(maxDim.width, Util.maxSmallIconButtonSize.height + topBorder + bottomBorder);
            setMaximumSize(maxDim);
            setMinimumSize(new Dimension(getMinimumSize().width, maxDim.height));
        }
    }

    @Override
    public Component getValueComponent() {
        if(wrapperPanel == null) {
            wrapperPanel = new JPanel(new BorderLayout());
            JPanel textFieldWrapper = new JPanel();
            textFieldWrapper.setLayout(new BoxLayout(textFieldWrapper, BoxLayout.PAGE_AXIS));
            selectedFileField = new JTextField();
            selectedFileField.setMaximumSize(new Dimension(Short.MAX_VALUE, panelSize));
            selectedFileField.setMinimumSize(new Dimension(10, panelSize));
            //selectedFileField.setPreferredSize(selectedFileField.getMaximumSize());
            selectedFileField.setToolTipText(label);
            selectedFileField.setEnabled(false);
            textFieldWrapper.add(Box.createVerticalGlue());
            textFieldWrapper.add(selectedFileField);
            textFieldWrapper.add(Box.createVerticalGlue());
            wrapperPanel.add(textFieldWrapper, BorderLayout.CENTER);

            JPanel buttonWrapper = new JPanel();
            buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.LINE_AXIS));
            try {
                selectButton = new JButton(getButtonIcon());
            } catch (FileNotFoundException e) {
                selectButton = new JButton("...");
            }
            selectButton.setToolTipText(tooltip);
            selectButton.setMaximumSize(Util.maxSmallIconButtonSize);
            selectButton.setPreferredSize(Util.maxSmallIconButtonSize);
            selectButton.setMinimumSize(Util.maxSmallIconButtonSize);
            selectButton.addActionListener(this);
            buttonWrapper.add(Box.createHorizontalGlue());
            buttonWrapper.add(selectButton);
            buttonWrapper.add(Box.createHorizontalGlue());
            wrapperPanel.add(buttonWrapper, BorderLayout.LINE_END);
        }
        return wrapperPanel;
    }

    @Override
    public String getValue() {
        return selectedFileField.getText();
    }

    @Override
    public void setValue(String value) {
        selectedFileField.setText(value);
    }

    protected abstract ImageIcon getButtonIcon() throws FileNotFoundException;

    protected abstract String getLastFolderSettingName();

    protected abstract void initFileChooser(JFileChooser fileChooser);

    public void actionPerformed(ActionEvent e) {
        String startDirectoryName = Settings.getSetting(getLastFolderSettingName()).
            replace("${user.home}", System.getProperty("user.home"));

        File startDirectory = new File(startDirectoryName);
        if(!startDirectory.exists() || !startDirectory.isDirectory()) {
            startDirectory = new File(System.getProperty("user.home"));
        }

        JFileChooser fileChooser = new JFileChooser(startDirectory);
        initFileChooser(fileChooser);
        int returnVal = fileChooser.showOpenDialog(MainWindowInterface.getInstance());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(file != null) {
                String absolutePath = file.isDirectory() ? file.getAbsolutePath() : file.getParentFile().getAbsolutePath();
                Settings.setSetting(getLastFolderSettingName(), absolutePath);
                selectedFileField.setText(file.getAbsolutePath());
                selectedFileField.setToolTipText(label + ": " + file.getAbsolutePath());
            }
        }
    }

}
