package com.mscg.jmp3.ui.util.input;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.util.Util;

public class FileSelectionInputPanel extends GenericJFileChooserSelectionInputPanel {

    private static final long serialVersionUID = 2069577375813574841L;

    private IconType iconType;
    private List<FileFilter> fileFilters;

    public FileSelectionInputPanel(String label, int topBorder, int bottomBorder,
                                   String fileSelectionTooltip) {
        this(label, topBorder, bottomBorder, fileSelectionTooltip, null, null);
    }

    public FileSelectionInputPanel(String label, String fileSelectionTooltip) {
        this(label, fileSelectionTooltip, null, null);
    }

    public FileSelectionInputPanel(String label, int topBorder, int bottomBorder,
                                   String fileSelectionTooltip, IconType iconType) {
        this(label, topBorder, bottomBorder, fileSelectionTooltip, iconType, null);
    }

    public FileSelectionInputPanel(String label, String fileSelectionTooltip, IconType iconType) {
        this(label, fileSelectionTooltip, iconType, null);
    }

    public FileSelectionInputPanel(String label, int topBorder, int bottomBorder,
                                   String fileSelectionTooltip, IconType iconType,
                                   List<FileFilter> fileFilters) {
        super(label, topBorder, bottomBorder, fileSelectionTooltip, false);
        this.iconType = iconType;
        if(fileFilters != null)
            this.fileFilters = new LinkedList<FileFilter>(fileFilters);
        initComponent(topBorder, bottomBorder);
    }

    public FileSelectionInputPanel(String label, String fileSelectionTooltip, IconType iconType,
                                   List<FileFilter> fileFilters) {
        this(label, defaultTopBorder, defaultBottomBorder, fileSelectionTooltip, iconType, fileFilters);
    }

    @Override
    protected ImageIcon getButtonIcon() throws FileNotFoundException {
        return new ImageIcon(ThemeManager.getIcon((iconType != null ? iconType : IconType.FILE_GENERIC_SMALL)));
    }

    @Override
    protected String getLastFolderSettingName() {
        return "files.last.image.dir";
    }

    @Override
    protected void initFileChooser(JFileChooser fileChooser) {
        fileChooser.setDialogTitle(tooltip);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(!Util.isEmpty(fileFilters)) {
            for(FileFilter filter : fileFilters)
                fileChooser.addChoosableFileFilter(filter);
        }
        fileChooser.setAcceptAllFileFilterUsed(Util.isEmpty(fileFilters));
        fileChooser.setMultiSelectionEnabled(false);
    }

}
