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
@Module.Info(name = "AnnoucSpam", category = Module.Category.TUROK_CHAT)
public class AnnoucSpam extends Module {
	private Setting<Integer> tick_ = register(Settings.integerBuilder("Tick").withRange(1, 50).withValue(10).build());
	private Setting<Integer> delay = register(Settings.integerBuilder("Delay").withRange(2000, 10000).withValue(2000).build());

	public int tick;
	public int no_spam;

	public boolean send_m = false;
	public boolean moving = true;
	public float moved;

	public boolean block_break = false;
	public String type_block;

	@EventHandler
	private Listener<PacketEvent.Send> packetEventSendListener = new Listener<>(event -> {
		CPacketPlayerDigging player = (CPacketPlayerDigging) event.getPacket();

		if (player.getAction().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
			type_block = mc.world.getBlockState(p.getPosition()).getBlock().getLocalizedName();

			block_break = true;
		}
	});

	public void update_tick() {
		Date hora   = new Date();
		String data = new SimpleDateFormat("HH:mm:ss").format(hora);

		tick += tick_.getValue();

		if (mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) {
			moved  = 0.0f;
			send_m = true;
		} else {
			moved  += 0.1f;
			send_m = false;
		}

		if (tick >= delay.getValue()) {
			if (send_m) {
				if (block_break) {
					breaked_block(type_block);

					block_break = false;
				} else {
					send("I walked " + Float.toString(moved) + ", thanks Turok.");
				}

			} else {
				if (block_break) {
					breaked_block(type_block);

					block_break = false;
				} else {
					send("Im just stoped, thanks Turok " + data);
				}
			}

			tick = 0;
		}
	}

	public void breaked_block(String block) {
		send("I breaked " + block + ", thanks Turok.")
	}

	@Override
	public void onUpdate() {
		update_tick();
	}

	public void send(String message) {
		mc.player.connection.sendPacket(new CPacketChatMessage(message));
	}
}