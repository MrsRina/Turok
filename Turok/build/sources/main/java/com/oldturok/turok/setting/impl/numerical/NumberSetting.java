package com.oldturok.turok.setting.impl.numerical;

import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.converter.AbstractBoxedNumberConverter;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class NumberSetting<T extends Number> extends Setting<T> {

    private final T min;
    private final T max;

    public NumberSetting(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name, Predicate<T> visibilityPredicate, T min, T max) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.min = min;
        this.max = max;
    }

    public boolean isBound() {
        return min != null && max != null;
    }

    @Override
    public abstract AbstractBoxedNumberConverter converter();

    @Override
    public T getValue() {
        return super.getValue();
    }

    public T getMax() {
        return max;
    }

    public T getMin() {
        return min;
    }
}
