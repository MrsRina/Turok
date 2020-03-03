package com.oldturok.turok.module.modules.render;

import com.oldturok.turok.module.Module;

import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import com.oldturok.turok.event.events.RenderEvent;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.util.EntityUtil;
import com.oldturok.turok.util.GeometryMasks;
import com.oldturok.turok.util.TurokTessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

// Rina.
@Module.Info(name = "BlockHighLight", category = Module.Category.TUROK_RENDER)
public class BlockHighLight extends Module {
	private Setting<Integer> r = register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> g = register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> b = register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(200));
	private Setting<Integer> a = register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(70));
	
	private Setting<Boolean> outline = register(Settings.b("Outline", true));
	
	@Override
	public void onWorldRender(RenderEvent event) {
		mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> mc.player == entity).map(entity -> (EntityLivingBase) entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> (true && entity instanceof EntityPlayer)).forEach(this::drawLine);
	}

	private void drawLine(EntityLivingBase event_) {
		RayTraceResult result = event_.rayTrace(6, Minecraft.getMinecraft().getRenderPartialTicks());
		if (result == null) 
			return;

		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
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
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			BlockPos block_pos = result.getBlockPos();

			float x = block_pos.x - 0.01f;
			float y = block_pos.y - 0.01f;
			float z = block_pos.z - 0.01f;

			if (outline.getValue()) {
				TurokTessellator.drawLines(TurokTessellator.getBufferBuilder(), x, y, z, 1.01f, 1.01f, 1.01f, r.getValue(), g.getValue(), b.getValue(), a.getValue(), mask);
			} else {
				TurokTessellator.drawBox(TurokTessellator.getBufferBuilder(), x, y, z, 1.01f, 1.01f, 1.01f, r.getValue(), g.getValue(), b.getValue(), a.getValue(), mask);
			}

			TurokTessellator.release();
		}

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
	}
}