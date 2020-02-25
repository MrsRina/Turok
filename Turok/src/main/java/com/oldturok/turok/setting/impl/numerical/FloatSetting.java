package com.oldturok.turok.setting.impl.numerical;

import com.oldturok.turok.setting.converter.AbstractBoxedNumberConverter;
import com.oldturok.turok.setting.converter.BoxedFloatConverter;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class FloatSetting extends NumberSetting<Float> {

    private static final BoxedFloatConverter converter = new BoxedFloatConverter();

    public FloatSetting(Float value, Predicate<Float> restriction, BiConsumer<Float, Float> consumer, String name, Predicate<Float> visibilityPredicate, Float min, Float max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }

    @Override
    public AbstractBoxedNumberConverter converter() {
        return converter;
    }

}
