package com.oldturok.turok.gui.turok.theme.turok;

import com.oldturok.turok.gui.turok.TurokGUI;
import com.oldturok.turok.gui.turok.theme.staticui.TabGuiUI;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.use.Button;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.render.theme.AbstractTheme;

public class TurokTheme extends AbstractTheme {
    FontRenderer fontRenderer;

    public TurokTheme() {
        installUI(new RootButtonUI<Button>());
        installUI(new GUIUI());
        installUI(new RootGroupboxUI());
        installUI(new TurokFrameUI<Frame>());
        installUI(new RootScrollpaneUI());
        installUI(new RootInputFieldUI());
        installUI(new RootLabelUI());
        installUI(new RootChatUI());
        installUI(new RootCheckButtonUI());
        installUI(new TurokActiveModulesUI());
        installUI(new TurokSettingsPanelUI());
        installUI(new RootSliderUI());
        installUI(new TurokEnumbuttonUI());
        installUI(new RootColorizedCheckButtonUI());
        installUI(new TurokUnboundSliderUI());

        installUI(new TabGuiUI());

        fontRenderer = TurokGUI.fontRenderer;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public class GUIUI extends AbstractComponentUI<TurokGUI> {
    }
}
