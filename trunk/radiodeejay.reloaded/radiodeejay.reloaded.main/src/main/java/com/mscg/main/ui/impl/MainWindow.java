package com.mscg.main.ui.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.mscg.i18n.LocalizationService;
import com.mscg.main.activator.RadioDeejayReloadedMainActivator;
import com.mscg.main.ui.MainWindowInterface;
import com.mscg.settings.SettingsService;

public class MainWindow extends MainWindowInterface implements ComponentListener, WindowStateListener {

    private static final long serialVersionUID = -7240979686578111055L;

    private boolean maximized;

    private JPanel mainPanel;

    public MainWindow() {
        SettingsService settings = RadioDeejayReloadedMainActivator.getInstance().getSettingsService();

        this.maximized = Boolean.parseBoolean(settings.getValue("window.maximixed"));

        //Use OS LookAndFeel
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            initComponents();

            SwingUtilities.updateComponentTreeUI(this);
            pack();

            // center on screen
            setLocationRelativeTo(null);

            if(maximized)
                setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
        } catch(Exception e){
            showError(e);
        }
    }

    private void initComponents() {
        LocalizationService localization = RadioDeejayReloadedMainActivator.getInstance().getLocalizationService();
        SettingsService settings = RadioDeejayReloadedMainActivator.getInstance().getSettingsService();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(localization.getString("application.title"));

        getContentPane().setLayout(new BorderLayout());

        setMinimumSize(new Dimension(420, 280));

        int width = 620;
        try {
            width = Integer.parseInt(settings.getValue("mainarea.width"));
        } catch(Exception e){}
        int height = 462;
        try {
            height = Integer.parseInt(settings.getValue("mainarea.height"));
        } catch(Exception e){}

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(width, height));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        addComponentListener(this);
        addWindowStateListener(this);
    }

    public void componentShown(ComponentEvent e) {

    }

    public void componentResized(ComponentEvent e) {
        SettingsService settings = RadioDeejayReloadedMainActivator.getInstance().getSettingsService();

        if(!maximized) {
            Rectangle size = mainPanel.getBounds();
            settings.setValue("mainarea.width", Integer.toString(size.width));
            settings.setValue("mainarea.height", Integer.toString(size.height));
        }
    }

    public void componentMoved(ComponentEvent e) {

    }

    public void componentHidden(ComponentEvent e) {

    }

    public void windowStateChanged(WindowEvent e) {
        SettingsService settings = RadioDeejayReloadedMainActivator.getInstance().getSettingsService();

        maximized = (MAXIMIZED_BOTH & e.getNewState()) != 0;
        settings.setValue("window.maximixed", Boolean.toString(maximized));
    }

}
