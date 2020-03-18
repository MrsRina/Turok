package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.theme.AbstractTheme;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.component.use.Button;
import com.oldturok.turok.gui.turok.TurokGUI;

public class Widgets extends AbstractTheme {
    FontRenderer fontRenderer;

    public Widgets() {
        installUI(new WidgetButton<Button>());

        installUI(new implement_gui());

        installUI(new WidgetGroupbox());
        installUI(new WidgetModuleFrame<Frame>());

        installUI(new WidgetScrollpane());
        installUI(new WidgetInputField());

        installUI(new WidgetLabel());
        installUI(new WidgetChat());

        installUI(new WidgetCheckButton());
        installUI(new WidgetActiveModules());

        installUI(new WidgetModuleSettings());
        installUI(new WidgetSlider());

        installUI(new WidgetEnummButton());
        installUI(new WidgetColoredCheckButton());

        installUI(new WidgetUnboundSlider());

        fontRenderer = TurokGUI.fontRenderer;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public class implement_gui extends AbstractComponentUI<TurokGUI> {}
}
