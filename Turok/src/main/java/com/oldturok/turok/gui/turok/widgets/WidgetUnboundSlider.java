package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.turok.component.UnboundSlider;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.RenderHelper;

import org.lwjgl.opengl.GL11;

import com.oldturok.turok.util.TurokGL;

public class WidgetUnboundSlider extends AbstractComponentUI<UnboundSlider> {
    @Override
    public void renderComponent(UnboundSlider component, FontRenderer fontRenderer) {
        String text = component.getText() + ": " + component.getValue();
        int color = component.isPressed() ? 0xdddddd : 0xdddddd;
        float value_ = (float) component.getValue();
        if (component.isHovered()) color = (color & 0x9dc4dc) << 1; 

        TurokGL.refresh_color(255, 0, 0, 255);
        fontRenderer.drawString(1, component.getHeight() - fontRenderer.getFontHeight() / 2 - 4, color, text);

        TurokGL.FixRefreshColor();
    }

    @Override
    public void handleAddComponent(UnboundSlider component, Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
        component.setWidth(component.getTheme().getFontRenderer().getStringWidth(component.getText()));
    }
}