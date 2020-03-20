package com.oldturok.turok.gui.turok.component;

import com.oldturok.turok.gui.rgui.component.container.OrganisedContainer;
import com.oldturok.turok.setting.impl.numerical.IntegerSetting;
import com.oldturok.turok.setting.impl.numerical.NumberSetting;
import com.oldturok.turok.setting.impl.numerical.DoubleSetting;
import com.oldturok.turok.setting.impl.numerical.FloatSetting;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.component.use.Slider;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.setting.impl.BooleanSetting;
import com.oldturok.turok.gui.turok.Stretcherlayout;
import com.oldturok.turok.setting.impl.EnumSetting;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.Bind;

// Rina.
import com.oldturok.turok.util.TurokMath; // TurokMath;

import java.util.Arrays;

// Update by Rina in 20/03/20.
public class SettingsPanel extends OrganisedContainer {
    Module module;

    public SettingsPanel(Theme theme, Module module) {
        super(theme, new Stretcherlayout(1));
        
        setAffectLayout(false);

        this.module = module;
        prepare();
    }

    @Override
    public void renderChildren() {
        super.renderChildren();
    }

    public Module getModule() {
        return module;
    }

    private void prepare() {
        getChildren().clear();

        if (module == null) {
            setVisible(false);
            return;
        }

        if (!module.settingList.isEmpty()) {
            for (Setting setting : module.settingList) {
                if (!setting.isVisible()) {
                    continue;
                }

                String name = setting.getName();

                boolean is_number  = setting instanceof NumberSetting;
                boolean is_boolean = setting instanceof BooleanSetting;
                boolean is_enum    = setting instanceof EnumSetting;

                if (setting.getValue() instanceof Bind) {
                    addChild(new BindButton("Bind", module));
                }

                if (is_number) {
                    NumberSetting numberSetting = (NumberSetting) setting;
                    boolean is_bound = numberSetting.isBound();

                    double value = Double.parseDouble(numberSetting.getValue().toString());

                    if (!is_bound) {
                        UnboundSlider slider = new UnboundSlider(value, name, setting instanceof IntegerSetting);
                        slider.addPoof(new Slider.SliderPoof<UnboundSlider, Slider.SliderPoof.SliderPoofInfo>() {
                            @Override
                            public void execute(UnboundSlider component, SliderPoofInfo info) {
                                if (setting instanceof IntegerSetting)
                                    setting.setValue((int) info.getNewValue());
                                else if (setting instanceof FloatSetting)
                                    setting.setValue((float) info.getNewValue());
                                else if (setting instanceof DoubleSetting)
                                    setting.setValue(info.getNewValue());
                                setModule(module);
                            }
                        });

                        if (numberSetting.getMax() != null) slider.setMax(numberSetting.getMax().doubleValue());
                        if (numberSetting.getMin() != null) slider.setMin(numberSetting.getMin().doubleValue());
                        
                        addChild(slider);
                    } else {
                        double min = Double.parseDouble(numberSetting.getMin().toString());
                        double max = Double.parseDouble(numberSetting.getMax().toString());
                        
                        Slider slider = new Slider (
                                value, min, max,
                                Slider.getDefaultStep(min, max),
                                name,
                                setting instanceof IntegerSetting);

                        slider.addPoof(new Slider.SliderPoof<Slider, Slider.SliderPoof.SliderPoofInfo>() {
                            @Override
                            public void execute(Slider component, SliderPoofInfo info) {
                                if (setting instanceof IntegerSetting)
                                    setting.setValue((int) info.getNewValue());
                                else if (setting instanceof FloatSetting)
                                    setting.setValue((float) info.getNewValue());
                                else if (setting instanceof DoubleSetting)
                                    setting.setValue(info.getNewValue());
                            }
                        });

                        addChild(slider);
                    }

                } else if (is_boolean) {
                    CheckButton checkButton = new CheckButton(name);
                    checkButton.setToggled(((BooleanSetting) setting).getValue());
                    checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>() {
                        @Override
                        public void execute(CheckButton checkButton1, CheckButtonPoofInfo info) {
                            if (info.getAction() == CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE) {
                                setting.setValue(checkButton.isToggled());
                                setModule(module);
                            }
                        }
                    });

                    addChild(checkButton);

                } else if (is_enum) {
                    Class<? extends Enum> type = ((EnumSetting) setting).clazz;
                    Object[] con   = type.getEnumConstants();
                    String[] modes = Arrays.stream(con).map(o -> o.toString().toUpperCase()).toArray(String[]::new);
                    
                    EnumButton enumbutton = new EnumButton(name, modes);
                    enumbutton.addPoof(new EnumButton.EnumbuttonIndexPoof<EnumButton, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo>() {
                        @Override
                        public void execute(EnumButton component, EnumbuttonInfo info) {
                            setting.setValue(con[info.getNewIndex()]);
                            setModule(module);
                        }
                    });

                    enumbutton.setIndex(Arrays.asList(con).indexOf(setting.getValue()));
                    addChild(enumbutton);
                }
            }
        }

        if (children.isEmpty()) {
            setVisible(false);
            return;
        } else {
            setVisible(true);
            return;
        }
    }

    public void setModule(Module module) {
        this.module = module;
        setMinimumWidth((int) (getParent().getWidth() * 1.0f));
        prepare();

        setAffectLayout(false);
        for (Component component : children) {
            component.setX(1);
            component.setWidth(getWidth() - 1);

            if (getHeight() - component.getHeight() + component.getY() > getHeight()) {
                setHeight(getHeight() + component.getHeight() + 2);
            }
        }
    }
}
