package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.component.use.Label;

import org.lwjgl.opengl.GL11;

public class WidgetLabel <T extends Label> extends AbstractComponentUI<Label> {

    @Override
    public void renderComponent(Label component, FontRenderer a) {
        a = component.getFontRenderer();
        String[] lines = component.getLines();

        int y = 0;
        boolean shadow = component.isShadow();

        for (String str : lines){
            int x = 0;
            if (component.getAlignment() == AlignedComponent.Alignment.CENTER)
                x = component.getWidth() / 2 - a.getStringWidth(str) / 2;
            else if (component.getAlignment() == AlignedComponent.Alignment.RIGHT)
                x = component.getWidth() - a.getStringWidth(str);

            if (shadow)
                a.drawStringWithShadow(x, y, 255, 255, 255, str);
            else
                a.drawString(x, y, str);
            y += a.getFontHeight() + 3;
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void handleSizeComponent(Label component) {
        String[] lines = component.getLines();
        int y = 0;
        int w = 0;
        for (String s : lines){
            w = Math.max(w, component.getFontRenderer().getStringWidth(s));
            y += component.getFontRenderer().getFontHeight() + 3;
        }
        component.setWidth(w);
        component.setHeight(y);
    }
}
