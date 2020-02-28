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

        glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
        RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());
    }
}
