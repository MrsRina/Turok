package com.oldturok.turok.gui.rgui.poof.use;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;

public abstract class FramePoof<T extends Component, S extends PoofInfo> extends Poof<T, S> {
    public static class FramePoofInfo extends PoofInfo {
        private Action action;

        public FramePoofInfo(Action action) {
            this.action = action;
        }

        public Action getAction() {
            return action;
        }
    }

    public enum Action {
        MINIMIZE,MAXIMIZE,CLOSE
    }
}
