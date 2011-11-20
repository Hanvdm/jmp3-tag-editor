package com.mp3.ui.parsers;

import com.mscg.jmp3.exception.InvalidTagValueException;
import com.mscg.jmp3.ui.util.input.InputPanel;

public interface FilenamePatternParser {

    /**
     * Returns the unique ID of the parser.
     * Each parser must have a different ID or the
     * loader function will fail.
     *
     * @return The unique ID of the parser.
     */
    public String getParserID();

    /**
     * Returns the name of the parser that
     * will be shown in the selector.
     *
     * @return The name of the parser that
     * will be shown in the selector.
     */
    public String getParserName();

    /**
     * Returns the {@link InputPanel} UI
     * that will be shown in the tag editor
     * tab.
     *
     * @return The {@link InputPanel} UI
     * that will be shown in the tag editor
     * tab.
     */
    public InputPanel getParserInputPanel();

    /**
     * Inits the parser before it will be used
     * to create file tags.
     */
    public void initParser();

    /**
     * Sets the filename on which the parser has to
     * work.
     *
     * @param filename The filename on which the parser has to
     * work.
     */
    public void setFilename(String filename);

    /**
     * Parses the provided value and returns
     * the corresponding portion of the filename
     * or the input string, if it isn't s group
     * matcher.
     *
     * @param value The user provided value that has to
     * be used to match a portion of the filename.
     * @return The portion of the filename pointed
     * by a group matcher or the provided string if
     * it isn't a group matcher.
     *
     * @throws InvalidTagValueException If the matched value is not a valid
     * tag value.
     * @throws IndexOutOfBoundsException If there is no group corresponding
     * to the provided group matcher.
     * @throws Exception If a generic error occurs.
     */
    public String parseValue(String value) throws InvalidTagValueException,
                                                  IndexOutOfBoundsException,
                                                  Exception;
}
