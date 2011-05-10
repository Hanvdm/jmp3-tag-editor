package com.mscg.jmp3.util;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class Util {

    public static int getPanelHeightForFont(Font font) {
        Rectangle2D bound = font.getStringBounds("_^", new FontRenderContext(font.getTransform(), true, false));
        return ((int)Math.floor((bound.getHeight() - bound.getY()) / 10) * 10);
    }

}
