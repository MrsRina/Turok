package com.oldturok.turok.setting.converter;

import com.google.gson.JsonElement;

public class BoxedIntegerConverter extends AbstractBoxedNumberConverter<Integer> {
    @Override
    protected Integer doBackward(JsonElement s) {
        return s.getAsInt();
    }
}
