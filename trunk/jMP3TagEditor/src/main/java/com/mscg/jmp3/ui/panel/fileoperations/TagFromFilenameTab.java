package com.mscg.jmp3.ui.panel.fileoperations;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mscg.jID3tags.util.Costants;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.util.input.CheckboxInputPanel;
import com.mscg.jmp3.ui.util.input.ComboboxInputPanel;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.ui.util.input.TextBoxInputPanel;
import com.mscg.jmp3.util.transformation.FilenameTransformationsPanel;


public class TagFromFilenameTab extends GenericFileoperationTab {

    private static final long serialVersionUID = 1071623235908117442L;
    private InputPanel regExpPanel;
    private InputPanel authorPanel;
    private InputPanel albumPanel;
    private InputPanel titlePanel;
    private InputPanel numberPanel;
    private InputPanel genrePanel;
    private InputPanel yearPanel;
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
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoScroller.setViewportView(infoPanel);
        add(infoScroller, BorderLayout.CENTER);

        regExpPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.regex"), 0, 10);
        infoPanel.add(regExpPanel);
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
        removeTagsPanel = new CheckboxInputPanel(Messages.getString("operations.file.taginfo.info.removeold"));
        infoPanel.add(removeTagsPanel);
    }

    /**
     * @return the transformationsPanel
     */
    public FilenameTransformationsPanel getTransformationsPanel() {
        return transformationsPanel;
    }

    /**
     * @return the regExpPanel
     */
    public InputPanel getRegExpPanel() {
        return regExpPanel;
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

}
