package com.mscg.jmp3.ui.util.contextmenu;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;

public class FilesListHandler extends ContextMenuListHandler {

    public FilesListHandler(JList list, JPopupMenuFactory popupMenuFactory) {
        super(list, popupMenuFactory);
    }

    @Override
    public void mouseClicked(MouseEvent ev) {
        if(ev.getClickCount() == 2 && list.getSelectedIndices().length == 1) {
            int idx = list.getSelectedIndex();
            if(idx < 0)
                return;
            IconAndFileListElement fileElem = (IconAndFileListElement)list.getSelectedValue();
            File file = fileElem.getFile();
            try {
                Desktop.getDesktop().open(fileElem.getFile().getCanonicalFile());
            } catch (Exception e) {
                LOG.warn("Cannot play file \"" + file.getAbsolutePath() + "\"", e);
                AppLaunch.showError(e);
            }
        }
    }

}
