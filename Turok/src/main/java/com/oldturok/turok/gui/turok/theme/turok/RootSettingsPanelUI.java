package com.oldturok.turok.gui.turok.theme.turok;

import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.component.SettingsPanel;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

import org.lwjgl.opengl.GL11;

public class RootSettingsPanelUI extends AbstractComponentUI<SettingsPanel> {

    @Override
    public void renderComponent(SettingsPanel component, FontRenderer fontRenderer) {
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1, 0.33f, 0.33f, 0.2f);
        RenderHelper.drawOutlinedRoundedRectangle(0, 0, component.getWidth(), component.getHeight(), 6f, 0.14f, 0.14f, 0.14f, component.getOpacity(), 1f);
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
    }

}
