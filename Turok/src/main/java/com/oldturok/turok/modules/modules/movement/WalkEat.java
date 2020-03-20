package com.oldturok.turok.module.modules.movement;

import net.minecraftforge.client.event.InputUpdateEvent;
import com.oldturok.turok.module.Module;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

// Rina.
@Module.Info(name = "Walk Eat", description = "Walk eating.", category = Module.Category.TUROK_MOVEMENT)
public class WalkEat extends Module {
	@EventHandler
	private Listener<InputUpdateEvent> eventListener = new Listener<>(event -> {
		if (mc.player.isHandActive() && !mc.player.isRiding()) {
			event.getMovementInput().moveStrafe *= 5;
			event.getMovementInput().moveForward *= 5;
		}
	});
}