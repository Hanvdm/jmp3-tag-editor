package com.mscg.util.transformation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.mscg.i18n.Messages;
import com.mscg.jmp3.ui.listener.filetotag.AddTransformationListener;
import com.mscg.jmp3.ui.listener.filetotag.EditTransformationListener;
import com.mscg.jmp3.ui.listener.filetotag.RemoveTransformationsListener;
import com.mscg.jmp3.ui.listener.filetotag.TransformationsListChangeListener;
import com.mscg.theme.ThemeManager;
import com.mscg.theme.ThemeManager.IconType;
import com.mscg.util.contextmenu.JPopupMenuFactory;
import com.mscg.util.contextmenu.StringTransformatorsListHandler;

public class FilenameTransformationsPanel extends JPanel {

    private static final long serialVersionUID = 6653470987076682436L;

    //private static final Logger LOG = LoggerFactory.getLogger(FilenameTransformationsPanel.class);

    private JList transformationList;
    private DefaultListModel transformationListModel;
    private JButton addButton;
    private JButton removeButton;
    private ImageIcon addButtonIcon;
    private ImageIcon removeButtonIcon;
    private ImageIcon editButtonIcon;

    public FilenameTransformationsPanel() throws FileNotFoundException {
        super();
        initComponents();
    }

    public FilenameTransformationsPanel(boolean isDoubleBuffered) throws FileNotFoundException {
        super(isDoubleBuffered);
        initComponents();
    }

    protected void initComponents() throws FileNotFoundException {
        setLayout(new BorderLayout());

        addButtonIcon = new ImageIcon(ThemeManager.getIcon(IconType.ADD_SMALL));
        removeButtonIcon = new ImageIcon(ThemeManager.getIcon(IconType.REMOVE_SMALL));
        editButtonIcon = new ImageIcon(ThemeManager.getIcon(IconType.EDIT_SMALL));

        transformationListModel = new DefaultListModel();
        transformationList = new JList(transformationListModel);

        JScrollPane transformationScroll = new JScrollPane();
        transformationScroll.setViewportView(transformationList);
        add(transformationScroll, BorderLayout.CENTER);

        transformationScroll.setMinimumSize(new Dimension(100, 80));
        transformationScroll.setPreferredSize(new Dimension(100, 80));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        add(buttonsPanel, BorderLayout.LINE_END);

        addButton = new JButton(null, addButtonIcon);
        addButton.setToolTipText(Messages.getString("operations.file.maintransform.add.tooltip"));
        addButton.addActionListener(new AddTransformationListener(transformationList));
        removeButton = new JButton(null, removeButtonIcon);
        removeButton.setToolTipText(Messages.getString("operations.file.maintransform.remove.tooltip"));
        removeButton.addActionListener(new RemoveTransformationsListener(transformationList));
        removeButton.setEnabled(false);

        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(addButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonsPanel.add(removeButton);
        buttonsPanel.add(Box.createVerticalGlue());

        TransformationsListChangeListener listener = new TransformationsListChangeListener(transformationList, removeButton);
        transformationList.addListSelectionListener(listener);
        transformationList.addMouseListener(new StringTransformatorsListHandler(transformationList,
                                                                                new RemoveTransformationsPopupFactory()));
        transformationListModel.addListDataListener(listener);
    }

    private class RemoveTransformationsPopupFactory implements JPopupMenuFactory {

        private JPopupMenu contextMenu;
        private RemoveTransformationsListener removeTransformationListener;
        private EditTransformationListener editTransformationListener;
        private JMenuItem editSelectedMenuItem;
        private JSeparator separator;
        private JMenuItem removeMenuItem;
        private JMenuItem removeAllSelectedMenuItem;

        @Override
        public JPopupMenu getPopupMenu(JList list, int index) {
            if(contextMenu == null) {
                contextMenu = new JPopupMenu();
                removeMenuItem = new JMenuItem(Messages.getString("operations.file.maintransform.remove.context"),
                                               removeButtonIcon);
                removeTransformationListener = new RemoveTransformationsListener(list);
                removeMenuItem.addActionListener(removeTransformationListener);

                removeAllSelectedMenuItem = new JMenuItem(Messages.getString("operations.file.maintransform.remove.tooltip"),
                                                          removeButtonIcon);
                removeAllSelectedMenuItem .addActionListener(new RemoveTransformationsListener(list));

                editSelectedMenuItem = new JMenuItem(Messages.getString("operations.file.maintransform.edit.context"),
                                                     editButtonIcon);
                editTransformationListener = new EditTransformationListener(list);
                editSelectedMenuItem.addActionListener(editTransformationListener);


                contextMenu.add(editSelectedMenuItem);
                separator = new JSeparator(JSeparator.HORIZONTAL);
                contextMenu.add(separator);
                contextMenu.add(removeMenuItem);
                contextMenu.add(removeAllSelectedMenuItem);
            }
            removeTransformationListener.setListIndex(index);
            editTransformationListener.setListIndex(index);

            removeAllSelectedMenuItem.setVisible(list.getSelectedIndices().length > 1);

            editSelectedMenuItem.setVisible(!removeAllSelectedMenuItem.isVisible());
            separator.setVisible(editSelectedMenuItem.isVisible());
            removeMenuItem.setVisible(editSelectedMenuItem.isVisible());

            return contextMenu;
        }

    }

}
