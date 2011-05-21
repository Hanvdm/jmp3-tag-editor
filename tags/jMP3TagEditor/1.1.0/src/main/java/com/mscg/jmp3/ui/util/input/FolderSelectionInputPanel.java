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

import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.util.Util;
import com.mscg.jmp3.util.filefilter.FolderFileFilter;

public class FolderSelectionInputPanel extends InputPanel implements ActionListener {

    private static final long serialVersionUID = -1292006349586275810L;

    private JPanel wrapperPanel;
    private JTextField folderField;
    private JButton selectFolderButton;
    private String folderSelectionTooltip;

    public FolderSelectionInputPanel(String label, int topBorder, int bottomBorder, String folderSelectionTooltip) {
        super(label, topBorder, bottomBorder);
        selectFolderButton.setToolTipText(folderSelectionTooltip);
        this.folderSelectionTooltip = folderSelectionTooltip;
    }

    public FolderSelectionInputPanel(String label, String folderSelectionTooltip) {
        super(label);
        selectFolderButton.setToolTipText(folderSelectionTooltip);
        this.folderSelectionTooltip = folderSelectionTooltip;
    }

    @Override
    public Component getValueComponent() {
        if(wrapperPanel == null) {
            wrapperPanel = new JPanel(new BorderLayout());
            folderField = new JTextField();
            folderField.setMaximumSize(new Dimension(Short.MAX_VALUE,
                                                     Util.getPanelHeightForFont(folderField.getFont())));
            folderField.setPreferredSize(folderField.getMaximumSize());
            folderField.setToolTipText(label);
            folderField.setEnabled(false);
            wrapperPanel.add(folderField, BorderLayout.CENTER);

            JPanel buttonWrapper = new JPanel();
            buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.LINE_AXIS));
            try {
                selectFolderButton = new JButton(new ImageIcon(ThemeManager.getIcon(IconType.FOLDER)));
            } catch (FileNotFoundException e) {
                selectFolderButton = new JButton("...");
            }
            selectFolderButton.setMaximumSize(Util.maxSmallIconButtonSize);
            selectFolderButton.setPreferredSize(Util.maxSmallIconButtonSize);
            selectFolderButton.addActionListener(this);
            buttonWrapper.add(Box.createHorizontalGlue());
            buttonWrapper.add(selectFolderButton);
            buttonWrapper.add(Box.createHorizontalGlue());
            wrapperPanel.add(buttonWrapper, BorderLayout.LINE_END);
        }
        return wrapperPanel;
    }

    @Override
    public String getValue() {
        return folderField.getText();
    }

    @Override
    public void setValue(String value) {
        folderField.setText(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String startDirectoryName = Settings.getSetting("files.encoded.last.dir").
            replace("${user.home}", System.getProperty("user.home"));

        File startDirectory = new File(startDirectoryName);
        if(!startDirectory.exists() || !startDirectory.isDirectory()) {
            startDirectory = new File(System.getProperty("user.home"));
        }

        JFileChooser fileChooser = new JFileChooser(startDirectory);
        fileChooser.setDialogTitle(folderSelectionTooltip);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.addChoosableFileFilter(new FolderFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showOpenDialog(AppLaunch.mainWindow);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(file != null) {
                String absolutePath = file.getAbsolutePath();
                Settings.setSetting("files.encoded.last.dir", absolutePath);
                folderField.setText(absolutePath);
                folderField.setToolTipText(label + ": " + absolutePath);
            }
        }
    }

}
