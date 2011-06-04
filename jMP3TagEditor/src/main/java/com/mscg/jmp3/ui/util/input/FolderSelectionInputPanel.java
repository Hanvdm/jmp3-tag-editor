package com.mscg.jmp3.ui.util.input;

import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.util.filefilter.FolderFileFilter;

public class FolderSelectionInputPanel extends GenericJFileChooserSelectionInputPanel {

    private static final long serialVersionUID = 2564523302868220708L;

    public FolderSelectionInputPanel(String label, int topBorder, int bottomBorder, String folderSelectionTooltip) {
        super(label, topBorder, bottomBorder, folderSelectionTooltip, true);
    }

    public FolderSelectionInputPanel(String label, String folderSelectionTooltip) {
        super(label, folderSelectionTooltip, true);
    }

    @Override
    protected ImageIcon getButtonIcon() throws FileNotFoundException {
        return new ImageIcon(ThemeManager.getIcon(IconType.FOLDER));
    }

    @Override
    protected String getLastFolderSettingName() {
        return "files.encoded.last.dir";
    }

    @Override
    protected void initFileChooser(JFileChooser fileChooser) {
        fileChooser.setDialogTitle(tooltip);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.addChoosableFileFilter(new FolderFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
    }

}
