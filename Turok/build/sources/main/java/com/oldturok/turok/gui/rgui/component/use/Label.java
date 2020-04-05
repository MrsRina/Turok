package com.oldturok.turok.gui.rgui.component.use;

import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.render.theme.Theme;

public class Label extends AlignedComponent {
    String text;
    boolean multiline;

    boolean shadow;

    FontRenderer fontRenderer;

    public Label(String text) {
        this(text, false);
    }

    public Label(String text, boolean multiline) {
        this.text = text;
        this.multiline = multiline;
        setAlignment(AlignedComponent.Alignment.LEFT);
    }

    public String getText() {
        return text;
    }

    public String[] getLines() {
        String[] lines;
        if (isMultiline()){
            lines = getText().split(System.lineSeparator());
        } else {
            lines = new String[]{getText()};
        }
        return lines;
    }

    public void setText(String text) {
        this.text = text;
        getTheme().getUIForComponent(this).handleSizeComponent(this);
    }

    public void addText(String add) {
        setText(getText() + add);
    }

    public void addLine(String add){
        if (getText().isEmpty()){
            setText(add);
        } else {
            setText(getText() + System.lineSeparator() + add);
            multiline = true;
        }
    }

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isShadow() {
        return shadow;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    @Override
    public void setTheme(Theme theme) {
        super.setTheme(theme);
        setFontRenderer(theme.getFontRenderer());
        getTheme().getUIForComponent(this).handleSizeComponent(this);
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }
}
