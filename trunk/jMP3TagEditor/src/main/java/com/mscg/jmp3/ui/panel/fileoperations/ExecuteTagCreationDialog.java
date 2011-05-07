package com.mscg.jmp3.ui.panel.fileoperations;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.listener.StopJobListener;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.ThreadPoolManager;
import com.mscg.jmp3.util.pool.runnable.CreateTagsRunnable;

public class ExecuteTagCreationDialog extends JDialog {

    private static final long serialVersionUID = 8774582234015557326L;

    private TagFromFilenameTab tab;
    private InterruptibleRunnable createTagRunnable;
    private List<File> files;

    private JLabel actualFileName;
    private JProgressBar progressBar;

    public ExecuteTagCreationDialog(TagFromFilenameTab tab) throws FileNotFoundException {
        super();
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Dialog owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Frame owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Window owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    protected void initComponents(TagFromFilenameTab tab) throws FileNotFoundException {
        this.tab = tab;
        initComponents();
    }

    protected void initComponents() throws FileNotFoundException {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setTitle(Messages.getString("operations.file.taginfo.execute.title"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(ThemeManager.getIcon(IconType.RUN_SMALL)));

        files = new LinkedList<File>();
        DefaultListModel listModel = (DefaultListModel)AppLaunch.mainWindow.getFileChooseCard().getFilesList().getModel();
        for(Object listElement : listModel.toArray()) {
            IconAndFileListElement fileListEl = (IconAndFileListElement) listElement;
            files.add(fileListEl.getFile());
        }

        createTagRunnable = new CreateTagsRunnable(files, this);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

        centerPanel.add(Box.createVerticalGlue());

        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.LINE_AXIS));
        labelsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        JLabel actualFileLabel = new JLabel(Messages.getString("operations.file.taginfo.execute.actualfile"));
        labelsPanel.add(actualFileLabel);
        labelsPanel.add(Box.createHorizontalStrut(10));
        actualFileName = new JLabel();
        labelsPanel.add(actualFileName);
        labelsPanel.add(Box.createHorizontalGlue());
        centerPanel.add(labelsPanel);

        progressBar = new JProgressBar();
        centerPanel.add(progressBar);

        centerPanel.add(Box.createVerticalGlue());

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        JButton stopButton = new JButton(null, new ImageIcon(ThemeManager.getIcon(IconType.STOP)));
        stopButton.setToolTipText(Messages.getString("operations.stop"));
        stopButton.addActionListener(new StopJobListener(createTagRunnable, this, stopButton));
        buttonPanel.add(stopButton);
        buttonPanel.add(Box.createHorizontalGlue());
        contentPanel.add(buttonPanel, BorderLayout.PAGE_END);

        setContentPane(contentPanel);

        setMinimumSize(new Dimension(380, 180));
        setPreferredSize(getMinimumSize());
        setResizable(false);

        setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible)
            ThreadPoolManager.getInstance().execute(createTagRunnable);
        super.setVisible(visible);
    }

    /**
     * @return the tab
     */
    public TagFromFilenameTab getTab() {
        return tab;
    }

    /**
     * @param tab the tab to set
     */
    public void setTab(TagFromFilenameTab tab) {
        this.tab = tab;
    }

    /**
     * @return the actualFileName
     */
    public JLabel getActualFileName() {
        return actualFileName;
    }

    /**
     * @return the progressBar
     */
    public JProgressBar getProgressBar() {
        return progressBar;
    }

}
