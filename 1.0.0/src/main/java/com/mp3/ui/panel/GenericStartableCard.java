package com.mp3.ui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.mp3.ui.MainWindowInterface;

public abstract class GenericStartableCard extends GenericCard {

    private static final long serialVersionUID = 1474280779220101373L;

    protected JButton startButton;

    public GenericStartableCard(MainWindowInterface mainWindow) throws FileNotFoundException {
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

        startButton = new JButton();
        startButton.setIcon(getStartButtonIcon());
        startButton.setToolTipText(getStartButtonText());
        startButton.addActionListener(getStartButtonListener());
        buttonsPanel.add(startButton);

        buttonsPanel.add(Box.createVerticalGlue());

        add(buttonsPanel, BorderLayout.LINE_END);

        add(getCenterComponent(), BorderLayout.CENTER);
    }

    protected abstract Icon getStartButtonIcon() throws FileNotFoundException ;

    protected abstract String getStartButtonText();

    protected abstract ActionListener getStartButtonListener();

    protected abstract Component getCenterComponent() throws FileNotFoundException;
}
