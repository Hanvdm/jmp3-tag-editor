package com.mscg.jmp3.transformator.exception;

import com.mscg.jmp3.ui.util.input.InputPanel;

public class InvalidTransformatorParameterException extends Exception {

    private static final long serialVersionUID = -4468040594746197957L;

    private int parameterIndex;
    private InputPanel inputPanel;

    public InvalidTransformatorParameterException(int parameterIndex, InputPanel inputPanel) {
        super();
        this.parameterIndex = parameterIndex;
        this.inputPanel = inputPanel;
    }

    public InvalidTransformatorParameterException(int parameterIndex, InputPanel inputPanel, String message, Throwable cause) {
        super(message, cause);
        this.parameterIndex = parameterIndex;
        this.inputPanel = inputPanel;
    }

    public InvalidTransformatorParameterException(int parameterIndex, InputPanel inputPanel, String message) {
        super(message);
        this.parameterIndex = parameterIndex;
        this.inputPanel = inputPanel;
    }

    public InvalidTransformatorParameterException(int parameterIndex, InputPanel inputPanel, Throwable cause) {
        super(cause);
        this.parameterIndex = parameterIndex;
        this.inputPanel = inputPanel;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public InputPanel getInputPanel() {
        return inputPanel;
    }

}
