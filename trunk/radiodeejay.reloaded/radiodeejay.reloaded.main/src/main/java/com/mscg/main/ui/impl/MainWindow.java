package com.mscg.main.ui.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.mscg.i18n.LocalizationService;
import com.mscg.main.activator.RadioDeejayReloadedMainActivator;
import com.mscg.main.ui.MainUIManager;
import com.mscg.main.ui.listener.CloseWindowClickListener;
import com.mscg.settings.SettingsService;
import com.mscg.theme.IconType;
import com.mscg.theme.ThemeManager;

public class MainWindow extends JFrame implements ComponentListener, WindowStateListener {

    private static final long serialVersionUID = -7240979686578111055L;

    private static MainWindow instance;

    /**
     * @return the instance
     */
    public synchronized static MainWindow getInstance() {
        return instance;
    }

    /**
     * @param instance the instance to set
     */
    public synchronized static void setInstance(MainWindow instance) {
        MainWindow.instance = instance;
    }

    private boolean maximized;
    private MainUIManager uiManager;
    private JPanel mainPanel;

    public MainWindow(MainUIManager uiManager) {
        this.uiManager = uiManager;

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
            this.uiManager.showError(e, true);
        }
    }

    private void initComponents() {
        LocalizationService localization = RadioDeejayReloadedMainActivator.getInstance().getLocalizationService();
        SettingsService settings = RadioDeejayReloadedMainActivator.getInstance().getSettingsService();
        ThemeManager themeManager = RadioDeejayReloadedMainActivator.getInstance().getThemeManager();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(localization.getString("application.title"));

        getContentPane().setLayout(new BorderLayout());

        // menubar
        JMenuBar menubar = new JMenuBar();
        menubar.setPreferredSize(new Dimension(200, 20));
        JMenu fileMenu = (JMenu)uiManager.createMenu(localization.getString("menu.file"), true);
        JMenuItem exitMenu = uiManager.createMenu(localization.getString("menu.file.exit"), false);
        exitMenu.setIcon(new ImageIcon(themeManager.getIcon(IconType.EXIT)));
        exitMenu.addActionListener(new CloseWindowClickListener(MainWindow.this));
        fileMenu.add(exitMenu);
        menubar.add(fileMenu);
        setJMenuBar(menubar);

        setMinimumSize(new Dimension(200, 100));

        int width = 550;
        try {
            width = Integer.parseInt(settings.getValue("mainarea.width"));
        } catch(Exception e){}
        int height = 260;
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
