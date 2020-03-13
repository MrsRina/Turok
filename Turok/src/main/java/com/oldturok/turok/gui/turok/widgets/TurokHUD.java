package com.oldturok.turok.gui.turok;

import com.oldturok.turok.gui.rgui.component.container.use.Scrollpane;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.listen.TickListener;
import com.oldturok.turok.gui.turok.component.SettingsPanel;
import com.oldturok.turok.gui.turok.component.ActiveModules;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.turok.widgets.TurokTheme;
import com.oldturok.turok.gui.rgui.component.use.Label;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.LagCompensator;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.chatcmd.Chat;
import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.util.Pair;
import com.oldturok.turok.TurokMod;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.*;
import net.minecraft.init.Items;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.math.RoundingMode;
import java.util.*;

// Update by Rina 09/03/20.
public class TurokGUI extends GUI {
    public static final RootFontRenderer fontRendererBig = new RootFontRenderer(1.0f);
    public static final RootFontRenderer fontRenderer = new RootFontRenderer(1.0f);
    public Theme theme;

    public static ColourHolder primaryColour = new ColourHolder(29, 29, 29);

    public static ArrayList<Frame> frames;

    public static Frame frame_users;
    public static Frame frame_counts;
    public static Frame frame_array;
    public static Frame frame_coords;

    public static int x = 10;
    public static int y = 10;
    public static int nexty = y;

    public static boolean state_users = true;
    public static boolean state_count = true;
    public static boolean state_array = true;
    public static boolean state_coord = true;

    public TurokGUI() {
        super(new TurokTheme());
        theme = getTheme();
    }

    @Override
    public void drawGUI() {
        super.drawGUI();
    }

    @Override
    public void initializeGUI() {
        x = 10;
        y = 10;

        nexty = y;

        frames = new ArrayList<>();

        frame_users = new Frame(getTheme(), new Stretcherlayout(1), "Hiiii!!");
        frame_users.setCloseable(false);
        frame_users.setPinneable(true);

        Label users = new Label("");
        users.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            String name = "";           
            
            name = mc.player.getName();
            users.setText(name + " welcome to Turok " + TurokMod.TUROK_MOD_VERSION);
            users.addLine("Rina like you! ^^");
            users.addLine("Fps: " + Wrapper.getMinecraft().debugFPS);
        });

        frame_users.addChild(users);
        users.setFontRenderer(fontRendererBig);
        users.setShadow(true);

        frame_array = new Frame(getTheme(), new Stretcherlayout(1), "Turok Modules Array");
        frame_array.setCloseable(false);
        frame_array.addChild(new ActiveModules());
        frame_array.setPinneable(true);

        frame_coords = new Frame(getTheme(), new Stretcherlayout(1), "Coordinates");
        frame_coords.setCloseable(false);
        frame_coords.setPinneable(true);

        Label coordsLabel = new Label("");
        coordsLabel.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            boolean on_nether = (mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell"));

            float value = !on_nether ? 0.125f : 8;
            
            int posX = (int) (mc.player.posX);
            int posY = (int) (mc.player.posY);
            int posZ = (int) (mc.player.posZ);

            int poshX = (int) (mc.player.posX * value);
            int poshZ = (int) (mc.player.posZ * value);

            coordsLabel.setText("Coords:");
            coordsLabel.addLine(String.format("Nether: " + poshX + "x " + Integer.toString(posY) + "y " + poshZ + "z"));
            coordsLabel.addLine(String.format("Overworld: " + Integer.toString(posX) + "x " + Integer.toString(posY) + "y " + Integer.toString(posZ) + "z"));
        });

        frame_coords.addChild(coordsLabel);
        coordsLabel.setFontRenderer(fontRendererBig);
        coordsLabel.setShadow(true);
        frame_coords.setHeight(20);

        frame_counts = new Frame(getTheme(), new Stretcherlayout(1), "Turok Counts");
        frame_counts.setCloseable(false);
        frame_counts.setPinneable(true);

        Label count = new Label("");
        count.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            int totems = 0;
            int gapples = 0;
            int crystals = 0;
            int expbottles = 0;

            for (int items = 0; items < 45; items++) {
                ItemStack itemStack = Wrapper.getMinecraft().player.inventory.getStackInSlot(items);

                if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                    gapples += itemStack.stackSize;
                }

                if (itemStack.getItem() == Items.GOLDEN_APPLE) {
                    gapples += itemStack.stackSize;
                }

                if (itemStack.getItem() == Items.END_CRYSTAL) {
                    crystals += itemStack.stackSize;
                }

                if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE) {
                    expbottles += itemStack.stackSize;
                }
            }

            count.setText("Turok Counts.");
            count.addLine("Totems: " +  String.valueOf(totems));
            count.addLine("Crystals: " +  String.valueOf(gapples));
            count.addLine("Gapples: " + String.valueOf(crystals));
            count.addLine("EXPBottles: " +  String.valueOf(expbottles));
        });

        frame_counts.addChild(count);
        count.setFontRenderer(fontRendererBig);
        count.setShadow(true);

        frames.add(frame_users);
        frames.add(frame_array);
        frames.add(frame_coords);
        frames.add(frame_counts);

        for (Frame frame1 : frames) {
            Minecraft mc = Minecraft.getMinecraft();

            frame1.setY(x);
            frame1.setY(y);

            nexty = Math.max(y + frame1.getHeight() + 10, nexty);
            x += (frame1.getWidth() + 10);
            if (x * DisplayGuiScreen.getScale() > mc.displayWidth / 1.2f) {
                y = nexty;
                nexty = y;
                x = 10;
            }

        addChild(frame1);
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, Comparator.comparing(o -> (o.getValue())));

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public void destroyGUI() {
        kill();
    }

    private static final int DOCK_OFFSET = 0;

    public static void dock(Frame component) {
        Docking docking = component.getDocking();
        if (docking.isTop())
            component.setY(DOCK_OFFSET);
        if (docking.isBottom())
            component.setY((Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale()) - component.getHeight() - DOCK_OFFSET);
        if (docking.isLeft())
            component.setX(DOCK_OFFSET);
        if (docking.isRight())
            component.setX((Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale()) - component.getWidth() - DOCK_OFFSET);
        if (docking.isCenterHorizontal())
            component.setX((Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2));
        if (docking.isCenterVertical())
            component.setY(Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2);
    }
}