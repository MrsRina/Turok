package com.oldturok.turok.setting.converter;

import com.google.gson.JsonElement;

public class BoxedFloatConverter extends AbstractBoxedNumberConverter<Float> {
    @Override
    protected Float doBackward(JsonElement s) {
        return s.getAsFloat();
    }
}
