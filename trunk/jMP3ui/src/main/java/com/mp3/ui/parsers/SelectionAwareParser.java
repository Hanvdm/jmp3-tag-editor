package com.mp3.ui.parsers;

/**
 * Parsers implementing this interface will
 * be notified when the user selects or deselects
 * the parser from the UI combobox.
 *
 * @author Giuseppe Miscione
 *
 */
public interface SelectionAwareParser {

    /**
     * This event will be notified when the
     * user activates this parser.
     */
    public void onParserSelected();

    /**
     * This event will be notified when
     * the user deactivates this parser.
     */
    public void onParserUnselected();

}
