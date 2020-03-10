package com.oldturok.turok.setting.converter;

import com.google.common.base.Converter;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;

public abstract class AbstractBoxedNumberConverter<T extends Number> extends Converter<T, JsonElement> {

    @Override
    protected JsonElement doForward(T t) {
        return new JsonPrimitive(t);
    }

}
