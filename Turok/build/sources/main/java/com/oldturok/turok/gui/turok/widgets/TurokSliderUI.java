package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.RootSmallFontRenderer;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.use.Slider;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

import org.lwjgl.opengl.GL11;

import com.oldturok.turok.util.TurokGL;

public class TurokSliderUI extends AbstractComponentUI<Slider> {
    RootSmallFontRenderer smallFontRenderer = new RootSmallFontRenderer();

    @Override
    public void renderComponent(Slider component, FontRenderer aa) {
        TurokGL.refresh_color(255, 255, 255, component.getOpacity());

        int height = component.getHeight();
        
        double value = component.getValue();
        double w     = component.getWidth() * ((value - component.getMinimum()) / (component.getMaximum() - component.getMinimum()));
        
        float downscale = 1.1f;
        float w_        = (int) w;

        TurokGL.refresh_color(255, 0, 0, 150);
        RenderHelper.drawFilledRectangle(0, 0, w_, height / downscale);
        
        TurokGL.refresh_color(255, 0, 0, 150);
        RenderHelper.drawRectangle(0, 0, component.getWidth(), height / downscale);

        String s = value + "";
        if (component.isPressed()){
            w_ -= smallFontRenderer.getStringWidth(s)/2;
            w_  = Math.max(0, Math.min(w_, component.getWidth() - smallFontRenderer.getStringWidth(s)));
            smallFontRenderer.drawString((int) w_, 2, s);

            TurokGL.refresh_color(255, 0, 0, 255); 
            RenderHelper.drawCircle((int) w_, 4, 30.0f);
        } else {
            smallFontRenderer.drawString(2, 2, component.getText());
            smallFontRenderer.drawString(component.getWidth() - smallFontRenderer.getStringWidth(s) - 2, 2, s);
        }

        TurokGL.FixRefreshColor();
    }

    @Override
    public void handleAddComponent(Slider component, Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight() + 2);
        component.setWidth(smallFontRenderer.getStringWidth(component.getText()) + smallFontRenderer.getStringWidth(component.getMaximum() + "") + 3);
    }
}