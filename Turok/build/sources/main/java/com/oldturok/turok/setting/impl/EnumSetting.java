package com.oldturok.turok.setting.impl;

import com.google.common.base.Converter;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.converter.EnumConverter;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class EnumSetting<T extends Enum> extends Setting<T> {

    private EnumConverter converter;
    public final Class<? extends Enum> clazz;

    public EnumSetting(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name, Predicate<T> visibilityPredicate, Class<? extends Enum> clazz) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.converter = new EnumConverter(clazz);
        this.clazz = clazz;
    }

    @Override
    public Converter converter() {
        return converter;
    }

}
