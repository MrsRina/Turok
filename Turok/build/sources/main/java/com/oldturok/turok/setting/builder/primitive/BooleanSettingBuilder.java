package com.oldturok.turok.setting.builder.primitive;

import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.setting.impl.BooleanSetting;

public class BooleanSettingBuilder extends SettingBuilder<Boolean> {
    @Override
    public BooleanSetting build() {
        return new BooleanSetting(initialValue, predicate(), consumer(), name, visibilityPredicate());
    }

    @Override
    public BooleanSettingBuilder withName(String name) {
        return (BooleanSettingBuilder) super.withName(name);
    }
}
