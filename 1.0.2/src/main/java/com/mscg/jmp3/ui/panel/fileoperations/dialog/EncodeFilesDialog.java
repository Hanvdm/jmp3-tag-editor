package com.mscg.jmp3.ui.panel.fileoperations.dialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.mp3.ui.MainWindowInterface;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.listener.StopJobListener;
import com.mscg.jmp3.ui.panel.EncodeFileCard;
import com.mscg.jmp3.ui.panel.EncodeFileCard.QualityElement;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.runnable.EncodeFilesRunnable;

public class EncodeFilesDialog extends GenericFilesOperationDialog {

    private static final long serialVersionUID = -7862490993740909177L;

    private EncodeFileCard encodeFileCard;

    private List<JLabel> actualFileNames;
    private List<JProgressBar> actualFileProgresses;

    private int parallelProcesses;

    public EncodeFilesDialog(EncodeFileCard encodeFileCard) throws FileNotFoundException {
        super();
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Dialog owner) throws FileNotFoundException {
        super(owner);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Frame owner) throws FileNotFoundException {
        super(owner);
        initComponents(encodeFileCard);
    }

    protected void initComponents(EncodeFileCard encodeFileCard) throws FileNotFoundException {
        this.encodeFileCard = encodeFileCard;
        initComponents();
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setTitle(getDialogTitle());
        setIconImage(Toolkit.getDefaultToolkit().getImage(ThemeManager.getIcon(IconType.RUN_SMALL)));

        filesOperationRunnable = getRunnable();

        QualityElement parallelProcEl = (QualityElement)((JComboBox)encodeFileCard.getParallelEncodings().getValueComponent()).getSelectedItem();
        parallelProcesses = parallelProcEl.getValue();
        if(parallelProcesses <= 0)
            parallelProcesses = Runtime.getRuntime().availableProcessors();
        DefaultListModel filesModel = (DefaultListModel)MainWindowInterface.getInstance().getFilesList();
        parallelProcesses = Math.min(filesModel.getSize(), parallelProcesses);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

        centerPanel.add(Box.createVerticalGlue());

        actualFileNames = new ArrayList<JLabel>(parallelProcesses);
        actualFileProgresses = new ArrayList<JProgressBar>(parallelProcesses);
        for(int i = 0; i < parallelProcesses; i++) {
            JPanel titledPanel = new JPanel();
            titledPanel.setLayout(new BoxLayout(titledPanel, BoxLayout.PAGE_AXIS));
            titledPanel.setBorder(BorderFactory.createTitledBorder(
                String.format(Messages.getString("operations.file.encode.execute.thread"), i + 1)));

            JPanel labelsPanel = new JPanel();
            labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.LINE_AXIS));
            labelsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            JLabel actualFileLabel = new JLabel(Messages.getString("operations.file.encode.execute.actualfile"));
            labelsPanel.add(actualFileLabel);
            labelsPanel.add(Box.createHorizontalStrut(10));
            JLabel label = new JLabel();
            actualFileNames.add(label);
            labelsPanel.add(label);
            labelsPanel.add(Box.createHorizontalGlue());
            titledPanel.add(labelsPanel);

            JPanel labelWrapper = new JPanel();
            labelWrapper.setLayout(new BoxLayout(labelWrapper, BoxLayout.LINE_AXIS));
            labelWrapper.add(new JLabel(Messages.getString("operations.file.encode.execute.progress.actual")));
            labelWrapper.add(Box.createHorizontalGlue());
            titledPanel.add(labelWrapper);

            JProgressBar actualFileProgress = new JProgressBar();
            actualFileProgresses.add(actualFileProgress);
            titledPanel.add(actualFileProgress);

            centerPanel.add(titledPanel);
            centerPanel.add(Box.createVerticalStrut(10));
        }

        JPanel totalProgressWrapper = new JPanel();
        totalProgressWrapper.setLayout(new BoxLayout(totalProgressWrapper, BoxLayout.PAGE_AXIS));
        totalProgressWrapper.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.encode.execute.progress.overall")));
        progressBar = new JProgressBar();
        totalProgressWrapper.add(progressBar);
        centerPanel.add(totalProgressWrapper);

        centerPanel.add(Box.createVerticalGlue());

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        JButton stopButton = new JButton(null, new ImageIcon(ThemeManager.getIcon(IconType.STOP)));
        stopButton.setToolTipText(Messages.getString("operations.stop"));
        stopButton.addActionListener(new StopJobListener(filesOperationRunnable, stopButton));
        buttonPanel.add(stopButton);
        buttonPanel.add(Box.createHorizontalGlue());
        contentPanel.add(buttonPanel, BorderLayout.PAGE_END);

        setContentPane(contentPanel);

        setMinimumSize(new Dimension(380, 250 + (parallelProcesses - 1) * 100));
        setPreferredSize(getMinimumSize());
        setResizable(false);

        setLocationRelativeTo(getOwner());
    }

    public int getParallelProcesses() {
        return parallelProcesses;
    }

    @Override
    public JLabel getActualFileName() {
        return getActualFileName(0);
    }

    public JLabel getActualFileName(int index) {
        return actualFileNames.get(index);
    }

    @Override
    protected String getDialogTitle() {
        return Messages.getString("operations.file.encode.dialog.title");
    }

    @Override
    protected InterruptibleRunnable getRunnable() {
        return new EncodeFilesRunnable(this, encodeFileCard);
    }

    public JProgressBar getActualFileProgress() {
        return getActualFileProgress(0);
    }

    public JProgressBar getActualFileProgress(int index) {
        return actualFileProgresses.get(index);
    }

    public void initActualFileProgresses() {
        for(JProgressBar actualProgress : actualFileProgresses) {
            actualProgress.setMinimum(0);
        }
    }

}
