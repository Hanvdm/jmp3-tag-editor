package com.mscg.jmp3.ui.renderer.elements;

import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.renderer.IconedListElement;

public class IconAndTextListElement implements IconedListElement, Serializable {

    private static final long serialVersionUID = 3054694960385936175L;

    protected Icon icon;
    protected String text;

    public IconAndTextListElement(Icon icon) {
        this(icon, null);
    }

    public IconAndTextListElement(String text) {
        this(null, text);
    }

    public IconAndTextListElement(Icon icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    @Override
    public Icon getIcon() {
        try {
            if(icon == null)
                icon = new ImageIcon(ThemeManager.getIcon(IconType.FILE));
        } catch(Exception e){}
        return icon;
    }

    @Override
    public String toString() {
        return (text != null ? text : "-");
    }

    public String getText() {
        return text;
    }

}
