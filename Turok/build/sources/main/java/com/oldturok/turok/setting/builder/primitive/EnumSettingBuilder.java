package com.oldturok.turok.setting.builder.primitive;

import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.setting.impl.EnumSetting;

public class EnumSettingBuilder<T extends Enum> extends SettingBuilder<T> {
    Class<? extends Enum> clazz;

    public EnumSettingBuilder(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Setting<T> build() {
        return new EnumSetting<>(initialValue, predicate(), consumer(), name, visibilityPredicate(), clazz);
    }
}
