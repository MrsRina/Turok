package com.oldturok.turok.setting.converter;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class BooleanConverter extends Converter<Boolean, JsonElement> {

    @Override
    protected JsonElement doForward(Boolean aBoolean) {
        return new JsonPrimitive(aBoolean);
    }

    @Override
    protected Boolean doBackward(JsonElement s) {
        return s.getAsBoolean();
    }

}
