package com.oldturok.turok.gui.rgui.component.container.use;

import com.oldturok.turok.gui.rgui.component.container.AbstractContainer;
import com.oldturok.turok.gui.rgui.render.theme.Theme;

public class Groupbox extends AbstractContainer {

    String name;

    public Groupbox(Theme theme, String name) {
        super(theme);
        this.name = name;
    }

    public Groupbox(Theme theme, String name, int x, int y) {
        this(theme, name);
        setX(x);
        setY(y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
