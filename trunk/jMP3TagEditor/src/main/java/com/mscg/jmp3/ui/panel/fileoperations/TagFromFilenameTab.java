package com.mscg.jmp3.ui.panel.fileoperations;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.util.transformation.FilenameTransformationsPanel;


public class TagFromFilenameTab extends GenericFileoperationTab {

    private static final long serialVersionUID = 1071623235908117442L;
    private InputPanel regExpPanel;
    private InputPanel authorPanel;
    private InputPanel albumPanel;
    private InputPanel titlePanel;
    private InputPanel numberPanel;
    private InputPanel panel;
    private InputPanel yearPanel;

    public TagFromFilenameTab() throws FileNotFoundException {
        super();
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        setLayout(new BorderLayout());

        JPanel transformationsPanel = new FilenameTransformationsPanel();
        transformationsPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.maintransform.title")));
        add(transformationsPanel, BorderLayout.PAGE_START);

        JScrollPane infoScroller = new JScrollPane();
        infoScroller.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.taginfo.title")));
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoScroller.setViewportView(infoPanel);
        add(infoScroller, BorderLayout.CENTER);

        regExpPanel = new InputPanel(Messages.getString("operations.file.taginfo.info.regex"), 0, 10);
        infoPanel.add(regExpPanel);
        authorPanel = new InputPanel(Messages.getString("operations.file.taginfo.info.author"));
        infoPanel.add(authorPanel);
        albumPanel = new InputPanel(Messages.getString("operations.file.taginfo.info.album"));
        infoPanel.add(albumPanel);
        titlePanel = new InputPanel(Messages.getString("operations.file.taginfo.info.title"));
        infoPanel.add(titlePanel);
        numberPanel = new InputPanel(Messages.getString("operations.file.taginfo.info.number"));
        infoPanel.add(numberPanel);
        panel = new InputPanel(Messages.getString("operations.file.taginfo.info.genre"));
        infoPanel.add(panel);
        yearPanel = new InputPanel(Messages.getString("operations.file.taginfo.info.year"));
        infoPanel.add(yearPanel);
    }

}
