package com.mscg.jmp3.ui.panel.fileoperations;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.util.input.ComboboxInputPanel;
import com.mscg.jmp3.ui.util.input.InputPanel;


public class RenameFileTab extends GenericFileoperationTab {

    private static final long serialVersionUID = -257273605874322878L;
    private InputPanel filenamePatternPanel;

    public RenameFileTab() throws FileNotFoundException {
        super();
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        DefaultComboBoxModel patternModel = new DefaultComboBoxModel();

        patternModel.addElement("${artist} - ${track} - ${title}");
        patternModel.addElement("${track} - ${artist} - ${title}");
        patternModel.addElement("${track} - ${title}");
        patternModel.addElement("${year} - ${artist} - ${track} - ${title}");
        patternModel.addElement("${year} - ${track} - ${artist} - ${title}");
        patternModel.addElement("${year} - ${track} - ${title}");

        filenamePatternPanel = new ComboboxInputPanel(Messages.getString("operations.file.rename.label"),
                                                      0, 10,
                                                      patternModel,
                                                      true);

        add(filenamePatternPanel);

        JLabel infoLabel = new JLabel(Messages.getString("operations.file.rename.description"));
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(infoLabel, BorderLayout.PAGE_START);
        add(wrapper);
    }

    /**
     * @return the filenamePatternPanel
     */
    public InputPanel getFilenamePatternPanel() {
        return filenamePatternPanel;
    }

}
