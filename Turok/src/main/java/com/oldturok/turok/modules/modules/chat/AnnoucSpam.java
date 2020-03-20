package com.oldturok.turok.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import com.oldturok.turok.TurokMod;

import com.oldturok.turok.event.events.GuiScreenEvent;
import com.oldturok.turok.event.events.PacketEvent;

import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.module.Module;

import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.setting.Setting;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.block.Block;
import net.minecraft.init.Items;

import java.text.SimpleDateFormat;
import java.util.*;
import java.time.*;

// Rina.
@Module.Info(name = "AnnoucSpam", description = "For spam annouc.", category = Module.Category.TUROK_CHAT)
public class AnnoucSpam extends Module {
	private Setting<Integer> tick_ = register(Settings.integerBuilder("Tick").withRange(1, 50).withValue(10).build());
	private Setting<Integer> delay = register(Settings.integerBuilder("Delay").withRange(2000, 10000).withValue(2000).build());

	private Setting<Boolean> walk   = register(Settings.b("Walk", false));
	private Setting<Boolean> combat = register(Settings.b("Combat Modules", false));

	public int tick;
	public int no_spam;

	public boolean send_m = false;

	public int old_x;
	public int old_z;

	public static int x;
	public static int z;

	public int moved;

	public boolean block_break = false;
	public String type_block;

	public boolean get_damage = false;
	
	public float last_health;
	public float stable_health;
	public float health;
	public float damage;

	public static ArrayList<Module> combat_modules = new ArrayList<>();

	public Boolean combat_actived = false;
	public String type_module;

	@EventHandler
	private Listener<PacketEvent.Send> packetEventSendListener = new Listener<>(event -> {
		if (event.getPacket() instanceof CPacketPlayerDigging) {
			CPacketPlayerDigging player = (CPacketPlayerDigging) event.getPacket();

			if (player.getAction().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
				type_block = mc.world.getBlockState(player.getPosition()).getBlock().getLocalizedName();

				block_break = true;
			}
		}
	});

	public void get_value() {
		if (mc.player.movementInput.moveForward >= 0.1f) {
			x = old_x;
			z = old_z;

			old_x = (int) mc.player.posX;
			old_z = (int) mc.player.posZ;

			moved = (x - old_x) + (z - old_z);

			send_m = true;
		} else {
			send_m = false;
		}
	}

	public void reset_var() {
		x = old_x;
		z = old_z;

		old_x = (int) mc.player.posX;
		old_z = (int) mc.player.posZ;

		moved =  0;
	}

	public void update_tick() {
		Date hora   = new Date();
		String data = new SimpleDateFormat("HH:mm:ss").format(hora);

		tick += tick_.getValue();

		if (tick >= delay.getValue()) {
			if (send_m) {
				if (block_break) {
					breaked_block(type_block);

					block_break = false;
				} else {
					if (walk.getValue()) {
						send("I walked " + Integer.toString(moved) + ", thanks Turok.");
					}
				}
			} else {
				if (block_break) {
					breaked_block(type_block);

					block_break = false;
				} else {
					if (combat.getValue()) {
						active_module_combat();

						if (combat_actived) {
							send("I actived " + type_module + ", thanks Turok");

							type_module    = "null";
							combat_actived = false;
						}

					} else {
						if (walk.getValue()) {
							send("Im just stoped, thanks Turok " + data);
						}
					}
				}

				reset_var();
			}

			tick = 0;
		}
	}

	public void breaked_block(String block) {
		send("I breaked " + block + ", thanks Turok.");
	}

	public void active_module_combat() {
		for (Module combat : combat_modules) {
			if (ModuleManager.isModuleEnabled(combat.getName())) {
				if (combat.getCategory().equals("Turok Combat")) {
					type_module    = combat.getName();
					combat_actived = true;
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		get_value();
		update_tick();
	}

	public void send(String message) {
		mc.player.connection.sendPacket(new CPacketChatMessage(message));
	}
}