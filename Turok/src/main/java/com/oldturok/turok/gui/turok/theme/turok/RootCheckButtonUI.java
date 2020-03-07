package com.oldturok.turok.gui.turok.theme.turok;

import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.gui.font.CFontRenderer;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

import java.awt.*;
import java.io.*;

import org.lwjgl.opengl.GL11;

// Modify by Rina in 06/03/20.
public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton> {
    protected Color backgroundColour = new Color(0, 0, 255);
    protected Color backgroundColourHover = new Color(0, 0, 255);

    protected Color idleColourNormal = new Color(0, 0, 255);
    protected Color downColourNormal = new Color(0, 0, 255);

    protected Color idleColourToggle = new Color(0, 0, 255);
    protected Color downColourToggle = idleColourToggle.brighter();

    @Override
    public void renderComponent(CheckButton component, FontRenderer ff) {
        int color = component.isPressed() ? 0xdddddd : component.isToggled() ? 0xdddddd : 0xdddddd;
        
        if (component.isHovered()) color = (color & 0x9dc4dc) << 1;
        if (component.isToggled()) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());

            TurokGUI.fontRenderer.drawString(1, 1, color, component.getName());
        } else {
            TurokGUI.fontRenderer.drawString(1, 1, color, component.getName());
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void handleAddComponent(CheckButton component, Container container) {
        component.setWidth(TurokGUI.fontRenderer.getStringWidth(component.getName()) + 1);
        component.setHeight(TurokGUI.fontRenderer.getFontHeight() + 1);
    }
}
