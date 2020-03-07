package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.util.TurokTessellator;
import com.oldturok.turok.util.GeometryMasks;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.module.Module;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import org.lwjgl.opengl.GL11;

// Rina.
@Module.Info(name = "BlockHighLight", category = Module.Category.TUROK_RENDER)
public class BlockHighLight extends Module {
	private Setting<Integer> r = register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> g = register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> b = register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> a = register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(70));
	
	private Setting<Boolean> outline = register(Settings.b("Outline", true));
	
	public RayTraceResult result;

	@Override
	public void onUpdate() {
		if (mc.world == null || mc.player == null) {
			return;
		}

		result = mc.objectMouseOver;
		if (result == null) {
			return;
		}
	}

	@Override
	public void onWorldRender(RenderEvent event) {
		if (mc.world == null || mc.player == null) {
			return;
		}

		if (result == null) {
			return;
		}

		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = result.getBlockPos();
			IBlockState block_state = mc.world.getBlockState(pos);

			int prepare = 0;
			int mask = 0;


			if (outline.getValue()) {
				prepare = GL11.GL_LINES;
				mask = GeometryMasks.Line.ALL;
			} else {
				prepare = GL11.GL_QUADS;
				mask = GeometryMasks.Quad.ALL;
			}

			TurokTessellator.prepare(prepare);

			if (outline.getValue()) {
				TurokTessellator.drawLines(pos, r.getValue(), g.getValue(), b.getValue(), a.getValue(), mask);
			} else {
				TurokTessellator.drawBox(pos, r.getValue(), g.getValue(), b.getValue(), a.getValue(), mask);
			}

			TurokTessellator.release();
		}
	}
}