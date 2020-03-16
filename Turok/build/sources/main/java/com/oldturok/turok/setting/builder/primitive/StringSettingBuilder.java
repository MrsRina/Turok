package com.oldturok.turok.setting.builder.primitive;

import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.setting.impl.StringSetting;

public class StringSettingBuilder extends SettingBuilder<String> {
    @Override
    public StringSetting build() {
        return new StringSetting(initialValue, predicate(), consumer(), name, visibilityPredicate());
    }
}
