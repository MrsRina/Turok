package com.oldturok.turok.setting.impl;

import com.oldturok.turok.setting.converter.StringConverter;
import com.oldturok.turok.setting.Setting;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class StringSetting extends Setting<String> {
    private static final StringConverter converter = new StringConverter();

    public StringSetting(String value, Predicate<String> restriction, BiConsumer<String, String> consumer, String name, Predicate<String> visibilityPredicate) {
        super(value, restriction, consumer, name, visibilityPredicate);
    }

    @Override
    public StringConverter converter() {
        return converter;
    }

}
