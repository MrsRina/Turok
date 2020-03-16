package com.oldturok.turok.gui.rgui.component.listen;

import com.oldturok.turok.gui.rgui.component.Component;

public interface UpdateListener<T extends Component> {
    public void updateSize(T component, int oldWidth, int oldHeight);
    public void updateLocation(T component, int oldX, int oldY);
}
