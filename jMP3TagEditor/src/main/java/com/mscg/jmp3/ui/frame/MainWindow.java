package com.mscg.jmp3.ui.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import com.mp3.ui.MainWindowInterface;
import com.mp3.ui.cards.UICardInfo;
import com.mp3.ui.cards.UICardProvider;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.listener.CloseWindowClickListener;
import com.mscg.jmp3.ui.panel.EmbeddedCardProvider;
import com.mscg.jmp3.ui.panel.FileChooseCard;
import com.mscg.jmp3.ui.panel.FilenameOperationsCard;
import com.mscg.jmp3.util.service.ServiceLoader;

public class MainWindow extends MainWindowInterface implements ActionListener, ComponentListener, WindowStateListener {

    private static final long serialVersionUID = -2803783276903439771L;

    //private static Logger LOG = Logger.getLogger(MainWindow.class);

    private JButton nextButton;
    private JButton prevButton;
    private JPanel mainPanel;

    int cardIndex;
    int cardsCount;
    private boolean maximized;

    private FileChooseCard fileChooseCard;
    private FilenameOperationsCard filenameOperationsCard;

    public MainWindow() throws FileNotFoundException {
        this.maximized = Boolean.parseBoolean(Settings.getSetting("window.maximixed"));

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
            AppLaunch.manageError(e);
        }
    }

    private void initComponents() throws FileNotFoundException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Messages.getString("application.title"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(ThemeManager.getMainIcon(true)));

        getContentPane().setLayout(new BorderLayout());

        setMinimumSize(new Dimension(420, 280));

        // panel for next/prev buttons
        JPanel buttonBorderPanel = new JPanel(new BorderLayout());
        buttonBorderPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        buttonBorderPanel.add(buttonPanel);
        nextButton = new JButton(Messages.getString("application.next"),
                                 new ImageIcon(ThemeManager.getIcon(IconType.NEXT)));
        nextButton.setPreferredSize(new Dimension(130, 36));
        nextButton.setHorizontalTextPosition(SwingConstants.LEFT);
        nextButton.addActionListener(this);
        prevButton = new JButton(Messages.getString("application.prev"),
                                 new ImageIcon(ThemeManager.getIcon(IconType.PREV)));
        prevButton.addActionListener(this);
        prevButton.setPreferredSize(new Dimension(130, 36));
        prevButton.setVisible(false);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        getContentPane().add(buttonBorderPanel, BorderLayout.PAGE_END);

        // menubar
        JMenuBar menubar = new JMenuBar();
        menubar.setPreferredSize(new Dimension(200, 20));
        JMenu fileMenu = (JMenu)createMenu(Messages.getString("menu.file"), true);
        JMenuItem exitMenu = createMenu(Messages.getString("menu.file.exit"), false);
        exitMenu.setIcon(new ImageIcon(ThemeManager.getIcon(IconType.EXIT)));
        exitMenu.addActionListener(new CloseWindowClickListener(MainWindow.this));
        fileMenu.add(exitMenu);
        menubar.add(fileMenu);
        setJMenuBar(menubar);

        int width = 620;
        try {
            width = Integer.parseInt(Settings.getSetting("mainarea.width"));
        } catch(Exception e){}
        int height = 462;
        try {
            height = Integer.parseInt(Settings.getSetting("mainarea.height"));
        } catch(Exception e){}

        mainPanel = new JPanel(new CardLayout());
        mainPanel.setPreferredSize(new Dimension(width, height));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // add the cards to the main panel
        cardIndex = 0;

        ServiceLoader<UICardProvider> serviceLoader = ServiceLoader.load(UICardProvider.class);
        Set<UICardInfo> cardInfos = new TreeSet<UICardInfo>();
        for(UICardProvider provider : serviceLoader) {
            cardInfos.addAll(provider.getCardInfos(this));
        }
        int sequenceIndex = 0;
        for(UICardInfo cardInfo : cardInfos) {
            mainPanel.add(cardInfo.getCard(), Integer.toString(sequenceIndex++));
            if(EmbeddedCardProvider.FILE_CHOOSE_CARD_NAME.equals(cardInfo.getName()))
                fileChooseCard = (FileChooseCard)cardInfo.getCard();
            else if(EmbeddedCardProvider.FILENAME_OPERATIONS_CARD_NAME.equals(cardInfo.getName()))
                filenameOperationsCard = (FilenameOperationsCard)cardInfo.getCard();
        }

        cardsCount = mainPanel.getComponentCount();

        addComponentListener(this);
        addWindowStateListener(this);
    }

    private JMenuItem createMenu(String title, boolean isMenu) {
        String realTitle = title;
        Character mnemonic = null;
        int index = title.indexOf('&');
        if(index >= 0) {
            mnemonic = title.charAt(index + 1);
            realTitle = title.substring(0, index) + title.substring(index + 1);
        }
        JMenuItem menu = null;
        if(isMenu)
            menu = new JMenu(realTitle);
        else
            menu = new JMenuItem(realTitle);
        if(mnemonic != null)
            menu.setMnemonic(mnemonic);
        return menu;
    }

    @Override
    public JButton getNextButton() {
        return nextButton;
    }

    @Override
    public JButton getPrevButton() {
        return prevButton;
    }

    @Override
    public ListModel getFilesList() {
        return fileChooseCard.getFilesList().getModel();
    }

    public FileChooseCard getFileChooseCard() {
        return fileChooseCard;
    }

    public FilenameOperationsCard getFilenameOperationsCard() {
        return filenameOperationsCard;
    }

    //------------------------------------------------------------
    // EVENTS
    //------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout)mainPanel.getLayout();
        if(nextButton == e.getSource()) {
            cardIndex = (cardIndex + 1) % cardsCount;
        } else {
            cardIndex = (cardIndex + cardsCount - 1) % cardsCount;
        }
        cardLayout.show(mainPanel, Integer.toString(cardIndex));
        prevButton.setVisible(cardIndex != 0);
        nextButton.setVisible(cardIndex != (cardsCount - 1));
    }

    public void componentShown(ComponentEvent e) {

    }

    public void componentResized(ComponentEvent e) {
        if(!maximized) {
            Rectangle size = mainPanel.getBounds();
            Settings.setSetting("mainarea.width", Integer.toString(size.width));
            Settings.setSetting("mainarea.height", Integer.toString(size.height));
        }
    }

    public void componentMoved(ComponentEvent e) {

    }

    public void componentHidden(ComponentEvent e) {

    }

    public void windowStateChanged(WindowEvent e) {
        maximized = (MAXIMIZED_BOTH & e.getNewState()) != 0;
        Settings.setSetting("window.maximixed", Boolean.toString(maximized));
    }

}
