package com.mscg.jmp3.ui.util.transformation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

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

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.ui.listener.filetotag.AddTransformationListener;
import com.mscg.jmp3.ui.listener.filetotag.EditTransformationListener;
import com.mscg.jmp3.ui.listener.filetotag.MoveTransformationListener;
import com.mscg.jmp3.ui.listener.filetotag.RemoveTransformationsListener;
import com.mscg.jmp3.ui.listener.filetotag.TransformationsListChangeListener;
import com.mscg.jmp3.ui.renderer.elements.StringTransformatorElement;
import com.mscg.jmp3.ui.util.contextmenu.JPopupMenuFactory;
import com.mscg.jmp3.ui.util.contextmenu.StringTransformatorsListHandler;

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
    private ImageIcon moveUpIcon;
    private ImageIcon moveDownIcon;

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
        moveUpIcon = new ImageIcon(ThemeManager.getIcon(IconType.ARROW_UP));
        moveDownIcon = new ImageIcon(ThemeManager.getIcon(IconType.ARROW_DOWN));

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
                                                                                new ManageTransformationsPopupFactory()));
        transformationListModel.addListDataListener(listener);
    }

    public List<StringTransformator> getTransformators() {
        List<StringTransformator> trasformators = new LinkedList<StringTransformator>();

        for(Object element : transformationListModel.toArray()) {
            StringTransformatorElement trElem = (StringTransformatorElement)element;
            trasformators.add(trElem.getTransformator());
        }

        return trasformators;
    }

    private class ManageTransformationsPopupFactory implements JPopupMenuFactory {

        private JPopupMenu contextMenu;
        private RemoveTransformationsListener removeTransformationListener;
        private EditTransformationListener editTransformationListener;
        private MoveTransformationListener moveUpTransformationListener;
        private MoveTransformationListener moveDownTransformationListener;
        private JMenuItem editSelectedMenuItem;
        private JSeparator separator1;
        private JMenuItem moveUpMenuItem;
        private JMenuItem moveDownMenuItem;
        private JSeparator separator2;
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

                moveUpMenuItem = new JMenuItem(Messages.getString("operations.file.maintransform.moveup.context"),
                                               moveUpIcon);
                moveUpTransformationListener = new MoveTransformationListener(list, true);
                moveUpMenuItem.addActionListener(moveUpTransformationListener);
                moveDownMenuItem = new JMenuItem(Messages.getString("operations.file.maintransform.movedown.context"),
                                               moveDownIcon);
                moveDownTransformationListener = new MoveTransformationListener(list, false);
                moveDownMenuItem.addActionListener(moveDownTransformationListener);

                contextMenu.add(editSelectedMenuItem);
                separator1 = new JSeparator(JSeparator.HORIZONTAL);
                separator2 = new JSeparator(JSeparator.HORIZONTAL);
                contextMenu.add(separator1);
                contextMenu.add(moveUpMenuItem);
                contextMenu.add(moveDownMenuItem);
                contextMenu.add(separator2);
                contextMenu.add(removeMenuItem);
                contextMenu.add(removeAllSelectedMenuItem);
            }
            removeTransformationListener.setListIndex(index);
            editTransformationListener.setListIndex(index);
            moveUpTransformationListener.setListIndex(index);
            moveDownTransformationListener.setListIndex(index);

            removeAllSelectedMenuItem.setVisible(list.getSelectedIndices().length > 1);

            moveUpMenuItem.setEnabled(index != 0);
            moveDownMenuItem.setEnabled(index != list.getModel().getSize() - 1);

            editSelectedMenuItem.setVisible(!removeAllSelectedMenuItem.isVisible());
            separator1.setVisible(editSelectedMenuItem.isVisible());
            moveUpMenuItem.setVisible(editSelectedMenuItem.isVisible());
            moveDownMenuItem.setVisible(editSelectedMenuItem.isVisible());
            separator2.setVisible(editSelectedMenuItem.isVisible());
            removeMenuItem.setVisible(editSelectedMenuItem.isVisible());

            return contextMenu;
        }

    }

}
