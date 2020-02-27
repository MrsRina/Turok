package com.oldturok.turok.gui.turok.theme.turok;

import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.component.SettingsPanel;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

import static org.lwjgl.opengl.GL11.*;

public class TurokSettingsPanelUI extends AbstractComponentUI<SettingsPanel> {

    @Override
    public void renderComponent(SettingsPanel component, FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);

        glLineWidth(2f);
        glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
        RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());
        glColor4f(1f, 1f, 1f, 1.0f);
        glLineWidth(1.5f);
        RenderHelper.drawRectangle(0, 0, component.getWidth(), component.getHeight());
    }
}
