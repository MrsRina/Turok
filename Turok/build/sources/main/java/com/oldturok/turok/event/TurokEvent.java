package com.oldturok.turok.event;

import me.zero.alpine.type.Cancellable;
import com.oldturok.turok.util.Wrapper;

public class TurokEvent extends Cancellable {

    private Era era = Era.PRE;
    private final float partialTicks;

    public TurokEvent() {
        partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }

    public Era getEra() {
        return era;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public enum Era {
        PRE, PERI, POST
    }

}
