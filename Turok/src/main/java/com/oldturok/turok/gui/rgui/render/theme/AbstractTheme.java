package com.oldturok.turok.gui.rgui.render.theme;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.render.ComponentUI;
import com.oldturok.turok.gui.rgui.layout.Layout;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTheme implements Theme {
    protected final Map<Class<? extends Component>, ComponentUI> uis;
    protected final Map<Class<? extends Layout>, Class<? extends Layout>> layoutMap;

    public AbstractTheme() {
        uis = new HashMap<>();
        layoutMap = new HashMap<>();
    }

    protected void installUI(ComponentUI<?> ui) {
        uis.put(ui.getHandledClass(), ui);
    }

    @Override
    public ComponentUI getUIForComponent(Component component) {
        ComponentUI a = getComponentUIForClass(component.getClass());
        if (a == null)
            throw new RuntimeException("No installed component UI for " + component.getClass().getName());
        return a;
    }

    public ComponentUI getComponentUIForClass(Class<? extends Component> componentClass) {
        if (uis.containsKey(componentClass))
            return uis.get(componentClass);
        if (componentClass == null)
            return null;
        for(Class<?> componentInterface : componentClass.getInterfaces()) {
            ComponentUI ui = uis.get(componentInterface);
            if (ui != null)
                return ui;
        }

        return getComponentUIForClass((Class<? extends Component>) componentClass.getSuperclass());
    }
}
