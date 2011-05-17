package com.mscg.jmp3.ui.panel.fileoperations.dialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.listener.StopJobListener;
import com.mscg.jmp3.ui.panel.EncodeFileCard;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.runnable.EncodeFilesRunnable;

public class EncodeFilesDialog extends GenericFilesOperationDialog {

    private static final long serialVersionUID = -7862490993740909177L;

    private EncodeFileCard encodeFileCard;

    private JProgressBar actualFileProgress;

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

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Window owner) throws FileNotFoundException {
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

        JPanel labelWrapper = new JPanel();
        labelWrapper.setLayout(new BoxLayout(labelWrapper, BoxLayout.LINE_AXIS));
        labelWrapper.add(new JLabel(Messages.getString("operations.file.encode.execute.progress.actual")));
        labelWrapper.add(Box.createHorizontalGlue());
        centerPanel.add(labelWrapper);
        actualFileProgress = new JProgressBar();
        centerPanel.add(actualFileProgress);
        centerPanel.add(Box.createVerticalStrut(20));

        labelWrapper = new JPanel();
        labelWrapper.setLayout(new BoxLayout(labelWrapper, BoxLayout.LINE_AXIS));
        labelWrapper.add(new JLabel(Messages.getString("operations.file.encode.execute.progress.overall")));
        labelWrapper.add(Box.createHorizontalGlue());
        centerPanel.add(labelWrapper);
        progressBar = new JProgressBar();
        centerPanel.add(progressBar);

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

        setMinimumSize(new Dimension(380, 250));
        setPreferredSize(getMinimumSize());
        setResizable(false);

        setLocationRelativeTo(getOwner());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.getString("operations.file.encode.dialog.title");
    }

    @Override
    protected InterruptibleRunnable getRunnable() {
        return new EncodeFilesRunnable(this, encodeFileCard);
    }

    /**
     * @return the actualFileProgress
     */
    public JProgressBar getActualFileProgress() {
        return actualFileProgress;
    }

}
