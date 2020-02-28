package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.util.GeometryMasks;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.module.modules.combat.InsaneCrystal;
import static com.oldturok.turok.module.modules.combat.InsaneCrystal.get_player_pos;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// Rina.
@Module.Info(name = "HoleColor", category = Module.Category.TUROK_RENDER)
public class HoleColor extends Module {
	private ConcurrentHashMap<BlockPos, Boolean> safe_holes;
	private final BlockPos[] barrier_ = {
		new BlockPos(0, -1, 0),
		new BlockPos(0, 0, -1),
		new BlockPos(1, 0, 0),
		new BlockPos(0, 0, 1),
		new BlockPos(-1, 0, 0)
	};

	private Setting<Double> range = register(Settings.d("Range", 10.0d));
	private Setting<Integer> r = register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(0));
	private Setting<Integer> g = register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(0));
	private Setting<Integer> b = register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(255));
	private Setting<Integer> a = register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(10));

	@Override
	public void onUpdate() {
		if (safe_holes == null) {
			safe_holes = new ConcurrentHashMap<>();
		} else {
			safe_holes.clear();
		}

		int range_ = (int) Math.ceil(range.getValue());

		InsaneCrystal crystal_function = (InsaneCrystal) ModuleManager.getModuleByName("InsaneCrystal");
		List<BlockPos> block_pos = crystal_function.get_sphere(get_player_pos(), range_, range_, false, true, 0);

		for (BlockPos pos : block_pos) {
			if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) continue;
			if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) continue;
			if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) continue;

			boolean safe = true;
			boolean bedrock = true;

			for (BlockPos offset : barrier_) {
				Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
				if (block != Blocks.BEDROCK) bedrock = false;
				if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
					safe = false;
					break;
				}
			}

			if (safe) safe_holes.put(pos, bedrock);
		}
	}

	@Override
	public void onWorldRender(final RenderEvent event) {
		if (mc.player == null || safe_holes == null) return;
		if (safe_holes.isEmpty()) return;

		TurokTessellator.prepare(GL11.GL_LINES);
		safe_holes.forEach((block_pos, bedrock) -> {
			draw(block_pos, r.getValue(), g.getValue(), b.getValue());
			draw(block_pos, r.getValue(), g.getValue(), b.getValue());
			draw(block_pos, r.getValue(), g.getValue(), b.getValue());
		});
		TurokTessellator.release();
	}

	private void draw(BlockPos block_pos, int r, int g, int b) {
		Color color = new Color(r, g, b, a.getValue());
		TurokTessellator.drawLines(block_pos, color.getRGB(), GeometryMasks.Line.ALL);
	}
}