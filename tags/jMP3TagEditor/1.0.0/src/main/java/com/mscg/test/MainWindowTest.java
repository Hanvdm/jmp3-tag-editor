package com.mscg.test;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import com.mscg.jmp3.i18n.Messages;

/**
 *
 * @author Giuseppe Miscione
 */
public class MainWindowTest extends JFrame {

    private static final long serialVersionUID = -5201854720133047361L;

    private static Logger LOG = Logger.getLogger(MainWindowTest.class);

    private JTextArea actions;
    private JScrollPane jScrollPane1;
    private JButton startButton;

    /** Creates new form MainWindowTest */
    public MainWindowTest() {
        initComponents();
        //Usa il LookAndFeel del sistema operativo e centra la finestra nello schermo
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
            pack();
        }
        catch(Exception e){
            LOG.error("Cannot change Look-And-Feel of the application.", e); //$NON-NLS-1$
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        jScrollPane1 = new JScrollPane();
        actions = new JTextArea();
        startButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Messages.getString("MainWindowTest.title"));
        setBounds(new Rectangle(0, 0, 620, 462));
        getContentPane().setLayout(new GridBagLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle size = getBounds();
        setBounds((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2,
                  size.width, size.height);

        actions.setColumns(73);
        actions.setEditable(false);
        actions.setFont(new Font("Courier New", 0, 12));
        actions.setRows(20);
        actions.setMinimumSize(new Dimension(164, 94));
        jScrollPane1.setViewportView(actions);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        startButton.setText(Messages.getString("MainWindowTest.start"));
        startButton.addActionListener(new ButtonClickListener());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 4, 4, 4);
        getContentPane().add(startButton, gridBagConstraints);

    }

    private final class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {

        }
    }

}
