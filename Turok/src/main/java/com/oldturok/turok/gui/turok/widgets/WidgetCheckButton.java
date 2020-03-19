package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.font.CFontRenderer;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.TurokGUI;

import java.awt.*;
import java.io.*;

import org.lwjgl.opengl.GL11;

import com.oldturok.turok.util.TurokGL;

// Modify by Rina in 06/03/20.
public class WidgetCheckButton<T extends CheckButton> extends AbstractComponentUI<CheckButton> {
    protected Color backgroundColour = new Color(0, 0, 255);
    protected Color backgroundColourHover = new Color(0, 0, 255);

    protected Color idleColourNormal = new Color(0, 0, 255);
    protected Color downColourNormal = new Color(0, 0, 255);

    protected Color idleColourToggle = new Color(0, 0, 255);
    protected Color downColourToggle = idleColourToggle.brighter();

    boolean effect_button_one = true;
    Boolean effect_button_r   = true;

    int color_button_r = 105;

    @Override
    public void renderComponent(CheckButton component, FontRenderer ff) {
        int color = component.isPressed() ? 0xdddddd : component.isToggled() ? 0xdddddd : 0xdddddd;
        
        if (component.isHovered()) color = (color & 0x9dc4dc) << 1;
        if (component.isToggled()) {
            if (effect_button_one) {
                if (effect_button_r) {
                    color_button_r += 1;
                } else {
                    color_button_r -= 1;
                }

                if (color_button_r >= 255) {
                    effect_button_r = false;
                }

                if (color_button_r <= 105) {
                    effect_button_r = true;
                }
            }

            TurokGL.refresh_color(color_button_r, 0, 0, 150);
            RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());

            TurokGUI.fontRenderer.drawString(1, 1, color, component.getName());
        } else {
            TurokGUI.fontRenderer.drawString(1, 1, color, component.getName());
        }

        TurokGL.FixRefreshColor();
    }

    @Override
    public void handleAddComponent(CheckButton component, Container container) {
        component.setWidth(TurokGUI.fontRenderer.getStringWidth(component.getName()) + 1);
        component.setHeight(TurokGUI.fontRenderer.getFontHeight() + 1);
    }
}
