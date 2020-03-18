package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.component.use.Button;
import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.TurokGUI;

import java.awt.*;

import org.lwjgl.opengl.GL11;

public class WidgetButton <T extends Button> extends AbstractComponentUI<Button> {
    protected Color idleColour = new Color(0, 0, 255);
    protected Color downColour = new Color(0, 0, 150);

    @Override
    public void renderComponent(Button component, FontRenderer ff) {
        GL11.glColor3f(0.22f,0.22f,0.22f);
        if (component.isHovered() || component.isPressed()){
            GL11.glColor3f(0.26f,0.26f,0.26f);
        }

        RenderHelper.drawRoundedRectangle(0, 0, component.getWidth(), component.getHeight(), 3f);

        GL11.glColor3f(1,1,1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        TurokGUI.fontRenderer.drawString(1, 0, component.isPressed() ? downColour : idleColour, component.getName());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void handleAddComponent(Button component, Container container) {
        component.setWidth(TurokGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(TurokGUI.fontRenderer.getFontHeight()+2);
    }
}
