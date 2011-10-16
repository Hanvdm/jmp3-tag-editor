package com.mscg.jmp3.ui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.frame.MainWindow;

public abstract class GenericStartableCard extends GenericCard {

    private static final long serialVersionUID = 1474280779220101373L;

    protected JButton startButton;

    public GenericStartableCard(MainWindow mainWindow) throws FileNotFoundException {
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
        startButton.setIcon(new ImageIcon(ThemeManager.getIcon(IconType.START)));
        startButton.setToolTipText(Messages.getString("operations.start"));
        startButton.addActionListener(getStartButtonListener());
        buttonsPanel.add(startButton);

        buttonsPanel.add(Box.createVerticalGlue());

        add(buttonsPanel, BorderLayout.LINE_END);

        add(getCenterComponent(), BorderLayout.CENTER);
    }

    protected abstract ActionListener getStartButtonListener();

    protected abstract Component getCenterComponent() throws FileNotFoundException;
}
