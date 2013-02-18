package com.mscg.jmp3.ui.util.transformation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp3.ui.MainWindowInterface;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.ui.frame.MainWindow;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;
import com.mscg.jmp3.ui.renderer.elements.StringTransformatorElement;
import com.mscg.jmp3.ui.util.input.ConstantInputPanel;
import com.mscg.jmp3.util.Util;

public class TransformationsPreviewPanel extends JPanel implements ComponentListener, ActionListener {

    private static final long serialVersionUID = -2744638718803965735L;

    protected Logger LOG;

    private ConstantInputPanel originalName;
    private ConstantInputPanel transformedName;

    private int shownFileIndex;

    private ListModel filesListModel;
    private ListModel transformationListModel;

    private JButton previousButton;
    private JButton nextButton;

    public TransformationsPreviewPanel(ListModel transformationListModel) throws FileNotFoundException {
        super();

        LOG = LoggerFactory.getLogger(this.getClass());

        shownFileIndex = 0;
        this.transformationListModel = transformationListModel;

        initComponents();
    }

    protected void initComponents() throws FileNotFoundException {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.maintransform.preview.title")));
        JPanel namesWrapper = new JPanel();
        namesWrapper.setLayout(new BoxLayout(namesWrapper, BoxLayout.PAGE_AXIS));

        originalName = new ConstantInputPanel(Messages.getString("operations.file.maintransform.preview.original"));
        transformedName = new ConstantInputPanel(Messages.getString("operations.file.maintransform.preview.transformed"));
        namesWrapper.add(Box.createVerticalGlue());
        namesWrapper.add(originalName);
        namesWrapper.add(transformedName);
        namesWrapper.add(Box.createVerticalGlue());
        add(namesWrapper, BorderLayout.CENTER);

        JPanel buttonsWrapper = new JPanel();
        buttonsWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        buttonsWrapper.setLayout(new BoxLayout(buttonsWrapper, BoxLayout.PAGE_AXIS));

        buttonsWrapper.add(Box.createVerticalGlue());

        previousButton = new JButton(new ImageIcon(ThemeManager.getIcon(IconType.ARROW_UP)));
        previousButton.setMaximumSize(Util.maxSmallIconButtonSize);
        previousButton.setPreferredSize(Util.maxSmallIconButtonSize);
        previousButton.setToolTipText(Messages.getString("operations.file.maintransform.preview.previous"));
        previousButton.addActionListener(this);
        buttonsWrapper.add(previousButton);

        buttonsWrapper.add(Box.createRigidArea(new Dimension(0, 5)));

        nextButton = new JButton(new ImageIcon(ThemeManager.getIcon(IconType.ARROW_DOWN)));
        nextButton.setMaximumSize(Util.maxSmallIconButtonSize);
        nextButton.setPreferredSize(Util.maxSmallIconButtonSize);
        nextButton.setToolTipText(Messages.getString("operations.file.maintransform.preview.next"));
        nextButton.addActionListener(this);
        buttonsWrapper.add(nextButton);

        buttonsWrapper.add(Box.createVerticalGlue());

        add(buttonsWrapper, BorderLayout.LINE_END);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ((MainWindow)MainWindowInterface.getInstance()).getFilenameOperationsCard().addComponentListener(TransformationsPreviewPanel.this);
                } catch(NullPointerException e){
                    LOG.warn("Cannot add component listener to filename operation card", e);
                }
             }
         });

    }

    public void updatePreview() {
        if(LOG.isDebugEnabled())
            LOG.debug("Updating transformed name preview");

        try {
            if(filesListModel == null)
                filesListModel = MainWindowInterface.getInstance().getFilesList();
        } catch(NullPointerException e) {
            return;
        }

        IconAndFileListElement element = (IconAndFileListElement) filesListModel.getElementAt(shownFileIndex);
        String shownFileName = element.getFile().getName();
        int index = shownFileName.lastIndexOf(".");
        if(index >= 0)
            shownFileName = shownFileName.substring(0, index);

        originalName.setValue(shownFileName);

        for(int i = 0, l = transformationListModel.getSize(); i < l; i++) {
            Object transformatorElement = transformationListModel.getElementAt(i);
            if(transformatorElement instanceof StringTransformatorElement) {
                StringTransformator transformator = ((StringTransformatorElement)transformatorElement).getTransformator();
                shownFileName = transformator.transformString(shownFileName, (i + 1));
            }
        }

        transformedName.setValue(shownFileName);

        checkButtonsEnabled();
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
        updatePreview();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == previousButton)
            shownFileIndex = Math.max(0, shownFileIndex - 1);
        else if(e.getSource() == nextButton)
            shownFileIndex = Math.min(filesListModel.getSize() - 1, shownFileIndex + 1);

        updatePreview();
    }

    private void checkButtonsEnabled() {
        previousButton.setEnabled(shownFileIndex != 0);
        nextButton.setEnabled(shownFileIndex != (filesListModel.getSize() - 1));
    }

}
