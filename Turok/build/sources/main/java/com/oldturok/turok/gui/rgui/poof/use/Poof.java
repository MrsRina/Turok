package com.oldturok.turok.gui.rgui.poof.use;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.poof.IPoof;

import java.lang.reflect.ParameterizedType;

public abstract class Poof<T extends Component, S extends PoofInfo> implements IPoof<T, S> {
    private Class<T> componentclass;
    private Class<S> infoclass;

    public Poof() {
        this.componentclass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.infoclass = (Class<S>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public Class getComponentClass() {
        return componentclass;
    }

    @Override
    public Class<S> getInfoClass() {
        return infoclass;
    }
}
