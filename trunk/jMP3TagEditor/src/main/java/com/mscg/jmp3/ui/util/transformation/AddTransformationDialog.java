package com.mscg.jmp3.ui.util.transformation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.transformator.StringTransformatorFactory;
import com.mscg.jmp3.util.Util;

public class AddTransformationDialog extends JDialog {

    private static final long serialVersionUID = 7630936043764420978L;

    private static Integer panelSize;

    protected Logger LOG;

    protected DefaultComboBoxModel transformatorsModel;
    protected JComboBox transformators;
    protected List<StringTransformator> transformatorInstances;
    protected JPanel parametersPanel;
    protected JScrollPane parametersScroller;

    protected boolean saved;
    protected StringTransformator transformator;

    public AddTransformationDialog() throws FileNotFoundException {
        super();
        initComponents();
    }

    public AddTransformationDialog(Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents();
    }

    public AddTransformationDialog(Dialog owner) throws FileNotFoundException {
        super(owner);
        initComponents();
    }

    public AddTransformationDialog(Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents();
    }

    public AddTransformationDialog(Frame owner) throws FileNotFoundException {
        super(owner);
        initComponents();
    }

    public AddTransformationDialog(Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
        initComponents();
    }

    public AddTransformationDialog(Window owner) throws FileNotFoundException {
        super(owner);
        initComponents();
    }

    protected void initComponents() throws FileNotFoundException {
        LOG = LoggerFactory.getLogger(this.getClass());

        saved = false;

        setTitle(Messages.getString("operations.file.maintransform.adddialod.title"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(ThemeManager.getIcon(IconType.EDIT_SMALL)));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel comboboxPanel = new JPanel();
        comboboxPanel.setLayout(new BoxLayout(comboboxPanel, BoxLayout.LINE_AXIS));
        comboboxPanel.add(new JLabel(Messages.getString("operations.file.maintransform.adddialod.combolabel")));
        comboboxPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        parametersScroller = new JScrollPane();
        parametersScroller.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.maintransform.parameters.title")));
        contentPanel.add(parametersScroller, BorderLayout.CENTER);

        initTransformatorsModel();
        transformators = new JComboBox(transformatorsModel);
        transformators.addActionListener(new TransformatorSelectedListener());
        transformators.setSelectedIndex(0);
        comboboxPanel.add(transformators);

        contentPanel.add(comboboxPanel, BorderLayout.PAGE_START);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());

        JButton saveButton = new JButton(Messages.getString("operations.file.maintransform.adddialod.save"));
        saveButton.addActionListener(new SaveButtonListener());
        buttonsPanel.add(saveButton);

        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton cancelButton = new JButton(Messages.getString("operations.file.maintransform.adddialod.cancel"));
        cancelButton.addActionListener(new CancelButtonListener());
        buttonsPanel.add(cancelButton);

        buttonsPanel.add(Box.createHorizontalGlue());

        contentPanel.add(buttonsPanel, BorderLayout.PAGE_END);

        setContentPane(contentPanel);

        setMinimumSize(new Dimension(450, 250));
        setPreferredSize(getMinimumSize());
        setResizable(false);

        setLocationRelativeTo(getOwner());

    }

    /**
     * @return the saved
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * @return the transformator
     */
    public StringTransformator getTransformator() {
        return transformator;
    }

    /**
     * @param transformator the transformator to set
     */
    public void setTransformator(StringTransformator transformator) {
        if(transformator != null) {
            this.transformator = transformator;

            int index = 0;
            int j = 0;
            for(StringTransformator tmp : transformatorInstances) {
                if(tmp.getClass() == transformator.getClass()) {
                    index = j;
                    break;
                }
                j++;
            }
            transformators.setSelectedIndex(index);

            int parametersCount = parametersPanel.getComponentCount();
            if(parametersCount != 0) {
                for(int i = 0; i < parametersCount; i++) {
                    ParameterPanel paramPanel = null;
                    Component childComponent = parametersPanel.getComponent(i);
                    if(!(childComponent instanceof ParameterPanel))
                        continue;

                    paramPanel = (ParameterPanel)parametersPanel.getComponent(i);
                    paramPanel.setParameterValue(transformator.getParameters()[i]);
                }
            }

        }
    }

    protected void initTransformatorsModel() {
        transformatorsModel = new DefaultComboBoxModel();
        List<Class<? extends StringTransformator>> transformatorsClasses = StringTransformatorFactory.getStringTransformators();
        transformatorInstances = new LinkedList<StringTransformator>();
        for(Class<? extends StringTransformator> transformatorClass : transformatorsClasses) {
            try {
                StringTransformator transformator = transformatorClass.newInstance();
                transformatorsModel.addElement(transformator.getName());
                transformatorInstances.add(transformator);
            } catch (Exception e) {
                LOG.error("Cannot allocate string transformer", e);
            }
        }
    }

    protected class TransformatorSelectedListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            transformator = transformatorInstances.get(transformators.getSelectedIndex());

            parametersPanel = new JPanel();
            parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.PAGE_AXIS));
            parametersScroller.setViewportView(parametersPanel);

            if(transformator.getParametersNames() != null && transformator.getParametersNames().length != 0) {
                for(String parameterName : transformator.getParametersNames()) {
                    parametersPanel.add(new ParameterPanel(parameterName));
                }
            }
            else {
                parametersPanel.add(new JLabel(Messages.getString("operations.file.maintransform.parameters.noparam")));
            }

        }
    }

    protected class DialogButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AddTransformationDialog.this.setVisible(false);
        }

    }

    protected class SaveButtonListener extends DialogButtonListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            saved = false;

            int parametersCount = parametersPanel.getComponentCount();
            if(parametersCount != 0) {
                for(int i = 0; i < parametersCount; i++) {
                    ParameterPanel paramPanel = null;
                    Component childComponent = parametersPanel.getComponent(i);
                    if(!(childComponent instanceof ParameterPanel))
                        continue;

                    paramPanel = (ParameterPanel)parametersPanel.getComponent(i);

                    try {
                        transformator.setParameter(i, paramPanel.getParameterValue());
                    } catch(InvalidParameterException e) {
                        LOG.error("Cannot set parameter " + i + " for transformator " +
                                  transformator.getClass().getCanonicalName(), e);
                        String message = Messages.getString("operations.file.maintransform.adddialod.save.error").
                                         replace("${paramName}", paramPanel.getParamName());
                        AppLaunch.showError(new Exception(message));
                        return;
                    } catch(Exception e) {
                        AppLaunch.showError(e);
                        return;
                    }
                }
            }

            saved = true;
            super.actionPerformed(ev);
        }

    }

    protected class CancelButtonListener extends DialogButtonListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            saved = false;
            super.actionPerformed(e);
        }

    }

    protected class ParameterPanel extends JPanel {
        private static final long serialVersionUID = -4907069163199554627L;

        private String paramName;
        private JTextField paramValue;

        public ParameterPanel(String paramName) {
            this.paramName = paramName;
            initComponents();
        }

        private void initComponents() {
            paramValue = new JTextField();
            if(panelSize == null) {
                panelSize = Util.getPanelHeightForFont(paramValue.getFont());
            }

            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));

            add(new JLabel(paramName.endsWith(":") ? paramName : paramName + ":"));

            add(Box.createHorizontalGlue());

            paramValue.setToolTipText(paramName);
            paramValue.setMaximumSize(new Dimension(290, panelSize));
            paramValue.setMinimumSize(paramValue.getMaximumSize());
            paramValue.setPreferredSize(paramValue.getMaximumSize());

            add(paramValue);
        }

        public String getParamName() {
            return paramName;
        }

        public String getParameterValue() {
            return paramValue.getText();
        }

        public void setParameterValue(String value) {
            paramValue.setText(value);
        }
    }

}
