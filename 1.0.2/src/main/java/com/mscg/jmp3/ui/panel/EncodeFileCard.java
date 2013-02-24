package com.mscg.jmp3.ui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mp3.ui.MainWindowInterface;
import com.mp3.ui.panel.GenericStartableCard;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.listener.encode.StartEncodingListener;
import com.mscg.jmp3.ui.util.input.CheckboxInputPanel;
import com.mscg.jmp3.ui.util.input.ComboboxInputPanel;
import com.mscg.jmp3.ui.util.input.FolderSelectionInputPanel;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.util.Util;

public class EncodeFileCard extends GenericStartableCard {

    private static final long serialVersionUID = 1615134393808569470L;

    private InputPanel destinationFolder;
    private InputPanel bitrate;
    private InputPanel sampleFrequency;
    private InputPanel quality;
    private InputPanel parallelEncodings;
    private InputPanel copyTag;


    public EncodeFileCard(MainWindowInterface mainWindow) throws FileNotFoundException {
        super(mainWindow);
    }

    @Override
    protected ActionListener getStartButtonListener() {
        return new StartEncodingListener(this);
    }

    @Override
    protected Icon getStartButtonIcon() throws FileNotFoundException {
        return new ImageIcon(ThemeManager.getIcon(IconType.START));
    }

    @Override
    protected String getStartButtonText() {
        return Messages.getString("operations.start");
    }

    @Override
    protected Component getCenterComponent() throws FileNotFoundException {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.encode.title")));

        JScrollPane infoScroller = new JScrollPane();
        infoScroller.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JPanel border = new JPanel();
        border.setLayout(new BoxLayout(border, BoxLayout.PAGE_AXIS));

        border.add(Box.createVerticalGlue());

        destinationFolder = new FolderSelectionInputPanel(Messages.getString("operations.file.encode.destination"),
                                                          0, 10,
                                                          Messages.getString("operations.file.encode.destination.choose"));
        border.add(destinationFolder);

        DefaultComboBoxModel sampleFreqModel = new DefaultComboBoxModel(
            new String[]{Messages.getString("operations.file.encode.samplefreq.orig"),
                         "44.1 kHz", "48 kHz", "32 kHz"});
        String selectedFreq = Settings.getSetting("encode.sample.freq");
        sampleFrequency = new ComboboxInputPanel(Messages.getString("operations.file.encode.samplefreq"),
                                                 0, 10,
                                                 sampleFreqModel,
                                                 (Util.isEmptyOrWhiteSpaceOnly(selectedFreq) ? null : selectedFreq));
        ((JComboBox)sampleFrequency.getValueComponent()).addActionListener(
            new SaveComboboxValueListener((JComboBox)sampleFrequency.getValueComponent(),
                                          "encode.sample.freq", 0));
        border.add(sampleFrequency);

        DefaultComboBoxModel bitrateModel = new DefaultComboBoxModel(
            new String[]{"320 kb/s", "256 kb/s", "224 kb/s", "192 kb/s", "160 kb/s", "128 kb/s", "112 kb/s",
                         "96 kb/s", "80 kb/s", "64 kb/s", "56 kb/s", "48 kb/s", "40 kb/s", "32 kb/s"});
        bitrate = new ComboboxInputPanel(Messages.getString("operations.file.encode.bitrate"),
                                         0, 10,
                                         bitrateModel,
                                         Settings.getSetting("encode.bitrate"));
        ((JComboBox)bitrate.getValueComponent()).addActionListener(
            new SaveComboboxValueListener((JComboBox)bitrate.getValueComponent(),
                                          "encode.bitrate", null));
        border.add(bitrate);

        QualityElement defaultQuality = new QualityElement("5 - " + Messages.getString("operations.file.encode.quality.default"), 5);
        DefaultComboBoxModel qualityModel = new DefaultComboBoxModel(
            new QualityElement[]{new QualityElement("0 - " + Messages.getString("operations.file.encode.quality.highest"), 0),
                                 new QualityElement("1", 1),
                                 new QualityElement("2", 2),
                                 new QualityElement("3", 3),
                                 new QualityElement("4", 4),
                                 defaultQuality,
                                 new QualityElement("6", 6),
                                 new QualityElement("7", 7),
                                 new QualityElement("8", 8),
                                 new QualityElement("9 - " + Messages.getString("operations.file.encode.quality.lowest"), 9),
                                 });
        quality = new ComboboxInputPanel(Messages.getString("operations.file.encode.quality"),
                                         0, 10,
                                         qualityModel,
                                         defaultQuality);
        border.add(quality);

        int maxParallelEncodings = 2 * Runtime.getRuntime().availableProcessors();
        int parallelEncodingsValue = 0;
        try {
            parallelEncodingsValue = Integer.parseInt(Settings.getSetting("encode.parallel.processes"));
        } catch(Exception e){}
        Vector<QualityElement> parallelEncodingsModelElems = new Vector<EncodeFileCard.QualityElement>(maxParallelEncodings + 1);
        parallelEncodingsModelElems.add(new QualityElement(Messages.getString("operations.file.encode.parallel.automatic"), 0));
        for(int i = 1; i <= maxParallelEncodings; i++) {
            parallelEncodingsModelElems.add(new QualityElement(Integer.toString(i), i));
        }
        DefaultComboBoxModel parallelEncodingsModel = new DefaultComboBoxModel(parallelEncodingsModelElems);
        QualityElement selectedValue;
        try {
            selectedValue = parallelEncodingsModelElems.get(parallelEncodingsValue);
        } catch(Exception e){
            selectedValue = parallelEncodingsModelElems.get(0);
        }
        parallelEncodings = new ComboboxInputPanel(Messages.getString("operations.file.encode.parallel"),
                                                   0, 10,
                                                   parallelEncodingsModel,
                                                   selectedValue);
        ((JComboBox)parallelEncodings.getValueComponent()).addActionListener(
            new SaveComboboxValueListener((JComboBox)parallelEncodings.getValueComponent(),
                                          "encode.parallel.processes", null));
        border.add(parallelEncodings);

        copyTag = new CheckboxInputPanel(Messages.getString("operations.file.encode.copytag"),
                                         0, 10,
                                         Boolean.parseBoolean(Settings.getSetting("encode.copytag")));
        ((CheckboxInputPanel)copyTag).getCheckbox().addChangeListener(
            new CheckboxSelectionListener(((CheckboxInputPanel)copyTag).getCheckbox(),
                                          "encode.copytag"));
        border.add(copyTag);

        border.add(Box.createVerticalGlue());

        infoScroller.setViewportView(border);
        wrapper.add(border, BorderLayout.CENTER);

        URL lameLogo = Thread.currentThread().getContextClassLoader().getResource("lame/lame-logo.png");
        JLabel poweredBy = new JLabel(Messages.getString("operations.file.encode.powered-by-lame"),
                                      new ImageIcon(lameLogo),
                                      JLabel.CENTER);
        poweredBy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        poweredBy.addMouseListener(new LabelLinkListener(poweredBy));
        poweredBy.setVerticalTextPosition(JLabel.BOTTOM);
        poweredBy.setHorizontalTextPosition(JLabel.CENTER);

        JPanel poweredByWrapper = new JPanel(new BorderLayout());
        poweredByWrapper.add(poweredBy, BorderLayout.LINE_END);

        wrapper.add(poweredByWrapper, BorderLayout.PAGE_END);

        return wrapper;
    }

    public InputPanel getDestinationFolder() {
        return destinationFolder;
    }

    public InputPanel getBitrate() {
        return bitrate;
    }

    public InputPanel getQuality() {
        return quality;
    }

    public InputPanel getParallelEncodings() {
        return parallelEncodings;
    }

    public InputPanel getSampleFrequency() {
        return sampleFrequency;
    }

    public InputPanel getCopyTag() {
        return copyTag;
    }

    private class LabelLinkListener implements MouseListener {

        private JLabel label;
        private String text;

        public LabelLinkListener(JLabel label) {
            this.label = label;
            text = label.getText();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setText(text);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            label.setText("<html><a href=\"\">" + text + "</a></html>");
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                Desktop.getDesktop().browse(new URI("http://lame.sourceforge.net/"));
            } catch (Exception e1) {
            }
        }
    }

    private static class CheckboxSelectionListener implements ChangeListener {

        private JCheckBox checkbox;
        private String settingsKey;

        public CheckboxSelectionListener(JCheckBox checkbox, String settingsKey) {
            this.checkbox = checkbox;
            this.settingsKey = settingsKey;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Settings.setSetting(settingsKey, Boolean.toString(checkbox.isSelected()));
        }
    }

    private static class SaveComboboxValueListener implements ActionListener {

        private JComboBox comboBox;
        private String settingsKey;
        private Integer defaultIndex;

        public SaveComboboxValueListener(JComboBox sampleFrequency, String settingsKey, Integer defaultIndex) {
            this.comboBox = sampleFrequency;
            this.settingsKey = settingsKey;
            this.defaultIndex = defaultIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(defaultIndex != null && comboBox.getSelectedIndex() == defaultIndex)
                Settings.setSetting(settingsKey, "");
            else {
                Object selected = comboBox.getSelectedItem();
                String value;
                if(selected instanceof String)
                    value = (String)selected;
                else if(selected instanceof QualityElement)
                    value = Integer.toString(((QualityElement)selected).getValue());
                else
                    value = selected.toString();
                Settings.setSetting(settingsKey, value);
            }
        }
    }

    public static class QualityElement {
        private String label;
        private int value;

        public QualityElement(String label, int value) {
            this.label = label;
            this.value = value;
        }

        @Override
        public String toString() {
            return label;
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

    }

}
