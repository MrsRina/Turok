package com.oldturok.turok.util;

import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;
import java.awt.*;
import java.io.*;

import de.matthiasmann.twl.utils.PNGDecoder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;

// Coded by Rina, in 07/03/20.
// Ok.
public class TurokGL {
	public static Minecraft mc = Minecraft.getMinecraft();

	public static void refresh_color(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red / 255, green / 255, blue / 255, alpha / 255);
	}

	public static void TurokRGBA(float alpha, float tick) {
		float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};

		int color_rgb = Color.HSBtoRGB(tick_color[0], 1, 1);

		GL11.glColor4f(((color_rgb >> 16) & 0xFF) / 255, ((color_rgb >> 8) & 0xFF) / 255, (color_rgb & 0xFF) / 255, alpha / 255);
		
		tick_color[0] += tick;
	}

	public static void DisableGL(int opengl) {
		GL11.glDisable(opengl);
	}

	public static void EnableGL(int opengl) {
		GL11.glEnable(opengl);
	}

	public static void FixRefreshColor() {
		DisableGL(GL11.GL_TEXTURE_2D);
        DisableGL(GL11.GL_BLEND);
	}

	public static void LineGL(float size) {
		GL11.glLineWidth(size);
	}
}