package com.oldturok.turok.modules;

import com.oldturok.turok.modules.api.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class QuickEat extends Module {
    private boolean attemptedEat = false;
    private int lastSlot;
    public QuickEat() {
        super("QuickEat", Keyboard.KEY_F);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onGui(int offset) {}

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (!attemptedEat) { lastSlot = minecraft.player.inventory.currentItem; }
        if (module.isKeyDown()) {
            int foodSlot = findFoodFromHotbar();
            if (foodSlot != -1) {
                KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindUseItem.getKeyCode(), true);
                minecraft.player.inventory.currentItem = foodSlot;
                attemptedEat = true;
            }
        }
        else {
            KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindUseItem.getKeyCode(), false);
            minecraft.player.inventory.currentItem = lastSlot;
            attemptedEat = false;
        }
    }

    private int findFoodFromHotbar() {
        int foodSlot = -1;
        for (int i = 0; i < 9; i++) {
            try {
                ItemFood food = (ItemFood)minecraft.player.inventory.mainInventory.get(i).getItem();
                foodSlot = i;
            }
            catch (Exception e) {}
        }

        return foodSlot;
    }
}
