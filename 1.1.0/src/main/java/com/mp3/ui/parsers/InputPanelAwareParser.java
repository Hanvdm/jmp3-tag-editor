package com.mp3.ui.parsers;

import com.mscg.jmp3.ui.util.input.InputPanel;

/**
 * Parsers implementing this interface
 * will be injected with references to UI {@link InputPanel}s
 * used to store the track tag informations.
 *
 * @author Giuseppe Miscione
 *
 */
public interface InputPanelAwareParser {

    /**
     * This method will be called to set
     * references to UI {@link InputPanel}s used
     * to store tag informations.
     *
     * @param authorPanel The author panel
     * @param albumPanel The album panel
     * @param titlePanel The title panel
     * @param numberPanel The track number panel
     * @param yearPanel The track year panel
     */
    public void setReferences(InputPanel authorPanel, InputPanel albumPanel, InputPanel titlePanel,
                              InputPanel numberPanel, InputPanel yearPanel);

}
