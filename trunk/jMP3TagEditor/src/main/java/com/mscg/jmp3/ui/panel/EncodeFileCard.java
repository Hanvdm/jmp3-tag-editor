package com.mscg.jmp3.ui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.ui.frame.MainWindow;
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
    private InputPanel copyTag;

    public EncodeFileCard(MainWindow mainWindow) throws FileNotFoundException {
        super(mainWindow);
    }

    @Override
    protected ActionListener getStartButtonListener() {
        return new StartEncodingListener(this);
    }

    @Override
    protected Component getCenterComponent() throws FileNotFoundException {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.encode.title")));

        JPanel border = new JPanel();
        border.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
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

        copyTag = new CheckboxInputPanel(Messages.getString("operations.file.encode.copytag"),
                                         0, 10,
                                         Boolean.parseBoolean(Settings.getSetting("encode.copytag")));
        ((CheckboxInputPanel)copyTag).getCheckbox().addChangeListener(
            new CheckboxSelectionListener(((CheckboxInputPanel)copyTag).getCheckbox(),
                                          "encode.copytag"));
        border.add(copyTag);

        border.add(Box.createVerticalGlue());

        wrapper.add(border, BorderLayout.CENTER);

        return wrapper;
    }

    public InputPanel getDestinationFolder() {
        return destinationFolder;
    }

    public InputPanel getBitrate() {
        return bitrate;
    }

    public InputPanel getSampleFrequency() {
        return sampleFrequency;
    }

    public InputPanel getCopyTag() {
        return copyTag;
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
            else
                Settings.setSetting(settingsKey,
                                    (String)comboBox.getSelectedItem());
        }
    }

}
