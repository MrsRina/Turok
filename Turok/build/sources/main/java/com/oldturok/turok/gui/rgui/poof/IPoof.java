package com.oldturok.turok.gui.rgui.poof;

import com.oldturok.turok.gui.rgui.component.Component;

public interface IPoof<T extends Component, S extends PoofInfo> {
    public void execute(T component, S info);
    public Class getComponentClass();
    public Class getInfoClass();
}
