package com.oldturok.turok.setting.converter;

import com.google.common.base.Converter;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;

public class StringConverter extends Converter<String, JsonElement> {
    @Override
    protected JsonElement doForward(String s) {
        return new JsonPrimitive(s);
    }

    @Override
    protected String doBackward(JsonElement s) {
        return s.getAsString();
    }
}
