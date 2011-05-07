package com.mscg.jmp3.ui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.frame.MainWindow;
import com.mscg.jmp3.ui.listener.filelist.AddFilesListener;
import com.mscg.jmp3.ui.listener.filelist.RemoveFilesListener;
import com.mscg.jmp3.ui.renderer.IconedListCellRenderer;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;
import com.mscg.jmp3.util.filefilter.MP3FileFilter;

public class FileChooseCard extends GenericCard {

    private static final long serialVersionUID = -241829622070661247L;

    private JList filesList;
    private ListModel listModel;

    private JButton addButton;
    private JButton removeButton;

    public FileChooseCard(MainWindow mainWindow) throws FileNotFoundException {
        super(mainWindow);
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        setLayout(new BorderLayout());

        // buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        buttonsPanel.add(Box.createVerticalGlue());

        // button to add files to list
        addButton = new JButton();
        addButton.setIcon(new ImageIcon(ThemeManager.getIcon(IconType.ADD)));
        addButton.setToolTipText(Messages.getString("files.add"));
        buttonsPanel.add(addButton);

        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // button to remove files from list
        removeButton = new JButton();
        removeButton.setIcon(new ImageIcon(ThemeManager.getIcon(IconType.REMOVE)));
        removeButton.setToolTipText(Messages.getString("files.remove"));
        buttonsPanel.add(removeButton);

        buttonsPanel.add(Box.createVerticalGlue());

        add(buttonsPanel, BorderLayout.LINE_END);

        // file list titled panel
        JPanel titledPanel = new JPanel(new BorderLayout());
        titledPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("files.list.title")));
        listModel = new DefaultListModel();
        ListFileSelectionListener selectionListener = new ListFileSelectionListener();
        listModel.addListDataListener(selectionListener);
        filesList = new JList(listModel);
        filesList.setCellRenderer(new IconedListCellRenderer());
        filesList.addListSelectionListener(selectionListener);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(filesList);
        titledPanel.add(scrollPane, BorderLayout.CENTER);

        add(titledPanel, BorderLayout.CENTER);

        selectionListener.checkEmptinessAndSelection();

        addButton.addActionListener(new AddFilesListener(filesList, mainWindow));
        removeButton.addActionListener(new RemoveFilesListener(filesList));

        parseParameters();
    }

    /**
     * @return the filesList
     */
    public JList getFilesList() {
        return filesList;
    }

    private void parseParameters() throws FileNotFoundException {
        FileFilter mp3Filter = new MP3FileFilter();
        for(String param : AppLaunch.applicationArguments) {
            File file = new File(param);
            if(file.exists() && file.isFile() && mp3Filter.accept(file))
                ((DefaultListModel)listModel).addElement(new IconAndFileListElement(ThemeManager.getMp3Icon(),
                                                                                    file));
        }
    }

    private class ListFileSelectionListener implements ListDataListener, ListSelectionListener {
        @Override
        public void intervalRemoved(ListDataEvent e) {
            checkEmptinessAndSelection();
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            checkEmptinessAndSelection();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            checkEmptinessAndSelection();
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            checkEmptinessAndSelection();
        }

        public void checkEmptinessAndSelection() {
            removeButton.setEnabled(listModel.getSize() != 0 &&
                                    filesList != null &&
                                    filesList.getSelectedIndices().length != 0);
            mainWindow.getNextButton().setEnabled(listModel.getSize() != 0);
        }

    }
}
