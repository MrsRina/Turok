package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.use.Button;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.render.theme.AbstractTheme;

public class TurokTheme extends AbstractTheme {
    FontRenderer fontRenderer;

    public TurokTheme() {
        installUI(new TurokButtonUI<Button>());
        installUI(new GUIUI());
        installUI(new TurokGroupboxUI());
        installUI(new TurokFrameUI<Frame>());
        installUI(new TurokScrollpaneUI());
        installUI(new TurokInputFieldUI());
        installUI(new TurokLabelUI());
        installUI(new TurokChatUI());
        installUI(new TurokCheckButtonUI());
        installUI(new TurokActiveModulesUI());
        installUI(new TurokSettingsPanelUI());
        installUI(new TurokSliderUI());
        installUI(new TurokEnumbuttonUI());
        installUI(new TurokColorizedCheckButtonUI());
        installUI(new TurokUnboundSliderUI());

        fontRenderer = TurokGUI.fontRenderer;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public class GUIUI extends AbstractComponentUI<TurokGUI> {
    }
}
