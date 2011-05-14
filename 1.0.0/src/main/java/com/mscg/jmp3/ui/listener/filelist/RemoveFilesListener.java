package com.mscg.jmp3.ui.listener.filelist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class RemoveFilesListener extends GenericFileListListener implements ActionListener {

    public RemoveFilesListener(JList filesList) {
        super(filesList);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int indexes[] = filesList.getSelectedIndices();
        DefaultListModel listModel = (DefaultListModel) filesList.getModel();
        for(int i = indexes.length - 1; i >= 0; i--)
            listModel.remove(indexes[i]);
    }

}
