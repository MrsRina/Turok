package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.turok.RenderHelper;
import com.oldturok.turok.gui.turok.component.SettingsPanel;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;

import com.oldturok.turok.util.TurokGL;

public class TurokSettingsPanelUI extends AbstractComponentUI<SettingsPanel> {
    @Override
    public void renderComponent(SettingsPanel component, FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);

        TurokGL.refresh_color(0, 0, 0, 150);
        RenderHelper.drawFilledRectangle(0, 0, component.getWidth() + 4, component.getHeight());
    }
}
