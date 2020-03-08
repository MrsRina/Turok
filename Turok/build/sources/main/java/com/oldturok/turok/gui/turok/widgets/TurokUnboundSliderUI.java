package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.component.UnboundSlider;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class TurokUnboundSliderUI extends AbstractComponentUI<UnboundSlider> {
    @Override
    public void renderComponent(UnboundSlider component, FontRenderer fontRenderer) {
        String text = component.getText() + ": " + component.getValue();
        int color = component.isPressed() ? 0xdddddd : 0xdddddd;
        float value_ = (float) component.getValue();
        if (component.isHovered()) color = (color &0x9dc4dc) << 1; 
        if (component.isPressed()) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderHelper.drawFilledRectangle(0, 0, component.getWidth() + 2, component.getHeight());

            fontRenderer.drawString(1, component.getHeight() - fontRenderer.getFontHeight() / 2 - 4, color, text);
        } else {
            fontRenderer.drawString(1, component.getHeight() - fontRenderer.getFontHeight() / 2 - 4, color, text);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void handleAddComponent(UnboundSlider component, Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
        component.setWidth(component.getTheme().getFontRenderer().getStringWidth(component.getText()));
    }
}