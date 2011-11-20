package com.mscg.jmp3.ui.panel.fileoperations;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import com.mp3.ui.MainWindowInterface;
import com.mp3.ui.parsers.FilenameParserProvider;
import com.mp3.ui.parsers.FilenamePatternParser;
import com.mp3.ui.parsers.InputPanelAwareParser;
import com.mp3.ui.parsers.SelectionAwareParser;
import com.mscg.jID3tags.util.Costants;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.util.input.CheckboxInputPanel;
import com.mscg.jmp3.ui.util.input.ComboboxInputPanel;
import com.mscg.jmp3.ui.util.input.FileSelectionInputPanel;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.ui.util.input.TextBoxInputPanel;
import com.mscg.jmp3.ui.util.input.bean.ComboboxBasicBean;
import com.mscg.jmp3.ui.util.transformation.FilenameTransformationsPanel;
import com.mscg.jmp3.util.filefilter.ImageFileFilter;


public class TagFromFilenameTab extends GenericFileoperationTab implements ItemListener {

    private static final long serialVersionUID = 1071623235908117442L;

    private JPanel infoPanel;
    private InputPanel parserSelectorPanel;
    private InputPanel parserInputPanel;
    private InputPanel authorPanel;
    private InputPanel albumPanel;
    private InputPanel titlePanel;
    private InputPanel numberPanel;
    private InputPanel genrePanel;
    private InputPanel yearPanel;
    private InputPanel coverPanel;
    private InputPanel removeTagsPanel;
    private FilenameTransformationsPanel transformationsPanel;

    public TagFromFilenameTab() throws FileNotFoundException {
        super();
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        setLayout(new BorderLayout());

        transformationsPanel = new FilenameTransformationsPanel();
        transformationsPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.maintransform.title")));
        add(transformationsPanel, BorderLayout.PAGE_START);

        JScrollPane infoScroller = new JScrollPane();
        infoScroller.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.taginfo.title")));
        infoScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoScroller.setViewportView(infoPanel);
        add(infoScroller, BorderLayout.CENTER);

        ComboboxBasicBean selectedItem = null;
        try {
            Map<String, FilenamePatternParser> parsers = FilenameParserProvider.getParsers();
            DefaultComboBoxModel parserSelectorModel = new DefaultComboBoxModel();
            String selectedItemID = Settings.getSetting("tag.filename.parser");
            for(Map.Entry<String, FilenamePatternParser> entry : parsers.entrySet()) {
                ComboboxBasicBean bean = new ComboboxBasicBean(entry.getKey(), entry.getValue().getParserName());
                parserSelectorModel.addElement(bean);
                if(entry.getKey().equals(selectedItemID))
                    selectedItem = bean;
            }
            if(selectedItem == null)
                selectedItem = (ComboboxBasicBean)parserSelectorModel.getElementAt(0);

            parserSelectorPanel = new ComboboxInputPanel(Messages.getString("operations.file.taginfo.info.parser.selector"),
                                                         parserSelectorModel);

            infoPanel.add(parserSelectorPanel);
            if(parserSelectorModel.getSize() == 1)
                parserSelectorPanel.setVisible(false);

        } catch(Exception e) {
            LOG.error("Can't build parser selector menu", e);
            MainWindowInterface.showError(e);
        }
        authorPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.author"));
        infoPanel.add(authorPanel);
        albumPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.album"));
        infoPanel.add(albumPanel);
        titlePanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.title"));
        infoPanel.add(titlePanel);
        numberPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.number"));
        infoPanel.add(numberPanel);
        List<String> genres = new LinkedList<String>();
        genres.add("");
        for(Costants.ID3v1Genre genre : Costants.ID3v1Genre.values()) {
            genres.add(genre.toString());
        }
        DefaultComboBoxModel genreModel = new DefaultComboBoxModel(genres.toArray());
        genrePanel = new ComboboxInputPanel(Messages.getString("operations.file.taginfo.info.genre"),
                                            genreModel, genres.get(1));
        infoPanel.add(genrePanel);
        yearPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.year"));
        infoPanel.add(yearPanel);
        List<FileFilter> fileFilters = new LinkedList<FileFilter>();
        fileFilters.add(new ImageFileFilter());
        coverPanel = new FileSelectionInputPanel(Messages.getString("operations.file.taginfo.info.cover"),
                                                 Messages.getString("operations.file.taginfo.info.cover.tooltip"),
                                                 IconType.FILE_IMAGE_SMALL, fileFilters);
        infoPanel.add(coverPanel);
        removeTagsPanel = new CheckboxInputPanel(Messages.getString("operations.file.taginfo.info.removeold"));
        infoPanel.add(removeTagsPanel);

        // add the listener to the parser selector after all other components have been built,
        // so that references can be correctly injected to the parsers (if needed)
        JComboBox comboBox = (JComboBox)parserSelectorPanel.getValueComponent();
        comboBox.addItemListener(this);
        // setting the selected item triggers the event listener method
        comboBox.setSelectedItem(selectedItem);
        if(comboBox.getModel().getSize() == 1) {
            // if the combobox has only one item, the event listener is not triggered,
            // so force the call
            ItemEvent e = new ItemEvent(comboBox, ItemEvent.ITEM_STATE_CHANGED, selectedItem,
                                        ItemEvent.SELECTED);
            itemStateChanged(e);
        }
    }

    /**
     * @return the transformationsPanel
     */
    public FilenameTransformationsPanel getTransformationsPanel() {
        return transformationsPanel;
    }

    /**
     * @return the parserSelectorPanel
     */
    public InputPanel getParserSelectorPanel() {
        return parserSelectorPanel;
    }

    /**
     * @return the authorPanel
     */
    public InputPanel getAuthorPanel() {
        return authorPanel;
    }

    /**
     * @return the albumPanel
     */
    public InputPanel getAlbumPanel() {
        return albumPanel;
    }

    /**
     * @return the titlePanel
     */
    public InputPanel getTitlePanel() {
        return titlePanel;
    }

    /**
     * @return the numberPanel
     */
    public InputPanel getNumberPanel() {
        return numberPanel;
    }

    /**
     * @return the genrePanel
     */
    public InputPanel getGenrePanel() {
        return genrePanel;
    }

    /**
     * @return the yearPanel
     */
    public InputPanel getYearPanel() {
        return yearPanel;
    }

    /**
     * @return the removeTagsPanel
     */
    public InputPanel getRemoveTagsPanel() {
        return removeTagsPanel;
    }

    /**
     * @return the coverPanel
     */
    public synchronized InputPanel getCoverPanel() {
        return coverPanel;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        ComboboxBasicBean bean = (ComboboxBasicBean) e.getItem();
        FilenamePatternParser filenamePatternParser = null;
        try {
            filenamePatternParser = FilenameParserProvider.getParsers().get(bean.getKey());
        } catch(Exception exc) {
            LOG.warn("Cannot get filename pattern parser instance", exc);
        }

        if(e.getStateChange() == ItemEvent.SELECTED) {
            Settings.setSetting("tag.filename.parser", bean.getKey());

            try {
                if(filenamePatternParser != null && filenamePatternParser instanceof InputPanelAwareParser) {
                    ((InputPanelAwareParser)filenamePatternParser).setReferences(authorPanel, albumPanel, titlePanel,
                                                                                 numberPanel, yearPanel);
                }

                if(parserInputPanel != null)
                    infoPanel.remove(parserInputPanel);
                parserInputPanel = filenamePatternParser.getParserInputPanel();
                infoPanel.add(parserInputPanel, 1);

                if(filenamePatternParser != null && filenamePatternParser instanceof SelectionAwareParser) {
                    try {
                        ((SelectionAwareParser)filenamePatternParser).onParserSelected();
                    } catch(Exception exc) {
                        LOG.warn("Cannot notify selection event", exc);
                    }
                }

                infoPanel.revalidate();
                infoPanel.repaint();
            } catch(Exception exc) {
                LOG.warn("Cannot update user interface", exc);
                MainWindowInterface.showError(exc);
            }
        }
        else {
            if(filenamePatternParser != null && filenamePatternParser instanceof SelectionAwareParser) {
                try {
                    ((SelectionAwareParser)filenamePatternParser).onParserUnselected();
                } catch(Exception exc) {
                    LOG.warn("Cannot notify selection event", exc);
                }
            }
        }
    }

}
