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
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.ThreadPoolManager;

public abstract class GenericFilesOperationDialog extends JDialog {

    private static final long serialVersionUID = -3655004463544639864L;

    protected InterruptibleRunnable filesOperationRunnable;

    protected JLabel actualFileName;
    protected JProgressBar progressBar;

    public GenericFilesOperationDialog() throws FileNotFoundException {
        super();
    }

    public GenericFilesOperationDialog( Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
    }

    public GenericFilesOperationDialog(Dialog owner) throws FileNotFoundException {
        super(owner);
    }

    public GenericFilesOperationDialog(Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
    }

    public GenericFilesOperationDialog(Frame owner) throws FileNotFoundException {
        super(owner);
    }

    public GenericFilesOperationDialog(Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
    }

    public GenericFilesOperationDialog(Window owner) throws FileNotFoundException {
        super(owner);
    }

    protected abstract String getDialogTitle();

    protected abstract InterruptibleRunnable getRunnable();

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

        setMinimumSize(new Dimension(380, 180));
        setPreferredSize(getMinimumSize());
        setResizable(false);

        setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible)
            ThreadPoolManager.getInstance().execute(filesOperationRunnable);
        super.setVisible(visible);
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
