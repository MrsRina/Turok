package com.oldturok.turok.gui.turok.theme.turok;

import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton> {

    protected Color backgroundColour = new Color(0, 0, 255);
    protected Color backgroundColourHover = new Color(0, 0, 255);

    protected Color idleColourNormal = new Color(0, 0, 255);
    protected Color downColourNormal = new Color(0, 0, 255);

    protected Color idleColourToggle = new Color(0, 0, 255);
    protected Color downColourToggle = idleColourToggle.brighter();

    @Override
    public void renderComponent(CheckButton component, FontRenderer ff) {
        String text = component.getName();
        int c = component.isPressed() ? 0xaaaaaa : component.isToggled() ? 0x0000ff : 0xdddddd;
        if (component.isHovered())
            c = (c & 0x0096ff) << 1;

        glDisable(GL_TEXTURE_2D);

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // drawFilledRectangle
        glLineWidth(2.0f);
        RenderHelper.drawRectangle(ff.getStringWidth(component.getName()) + 2, 0, component.getWidth(), component.getHeight());
        TurokGUI.fontRenderer.drawString(1, 1, c, text);
    }

    @Override
    public void handleAddComponent(CheckButton component, Container container) {
        component.setWidth(TurokGUI.fontRenderer.getStringWidth(component.getName()) + 1);
        component.setHeight(TurokGUI.fontRenderer.getFontHeight() + 1);
    }
}
