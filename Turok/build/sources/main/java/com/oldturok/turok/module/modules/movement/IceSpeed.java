package com.oldturok.turok.module.modules.movement;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import net.minecraft.init.Blocks;

@Module.Info(name = "IceSpeed", description = "Ice Speed", category = Module.Category.TUROK_MOVEMENT)
public class IceSpeed extends Module {

    private Setting<Float> slipperiness = register(Settings.floatBuilder("Slipperiness").withMinimum(0.2f).withValue(0.4f).withMaximum(1.0f).build());

    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = slipperiness.getValue();
        Blocks.PACKED_ICE.slipperiness = slipperiness.getValue();
        Blocks.FROSTED_ICE.slipperiness = slipperiness.getValue();
    }

    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }

}
