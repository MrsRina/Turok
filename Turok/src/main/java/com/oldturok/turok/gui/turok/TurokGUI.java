package com.oldturok.turok.gui.turok;

import com.oldturok.turok.gui.rgui.component.container.use.Scrollpane;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.listen.TickListener;
import com.oldturok.turok.gui.turok.component.SettingsPanel;
import com.oldturok.turok.gui.turok.component.ActiveModules;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.rgui.component.use.Label;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.gui.turok.widgets.Widgets;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.LagCompensator;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokMessage;
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
    public static Frame frame_wattermark;
    public static Frame frame_counts_totem;
    public static Frame frame_counts_crystal;
    public static Frame frame_counts_gapple;
    public static Frame frame_counts_exp;
    public static Frame frame_array;
    public static Frame frame_coords;

    public static int x = 10;
    public static int y = 10;
    public static int nexty = y;

    public static boolean state_arrays = true;
    public static boolean state_counts = true;
    public static boolean state_coords = true;
    public static Boolean state_users  = false;

    public TurokGUI() {
        super(new Widgets());
        theme = getTheme();
    }

    @Override
    public void drawGUI() {
        super.drawGUI();
    }

    @Override
    public void initializeGUI() {
        HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>> categoryScrollpaneHashMap = new HashMap<>();
        for (Module module : ModuleManager.getModules()) {
            if (module.getCategory().isHidden()) {
                continue;
            }

            Module.Category moduleCategory = module.getCategory();
            if (!categoryScrollpaneHashMap.containsKey(moduleCategory)) {
                Stretcherlayout stretcherlayout = new Stretcherlayout(1);
                stretcherlayout.setComponentOffsetWidth(0);
                Scrollpane scrollpane = new Scrollpane(getTheme(), stretcherlayout, 300, 260);
                scrollpane.setMaximumHeight(600);
                scrollpane.setMaximumWidth(600);
                categoryScrollpaneHashMap.put(moduleCategory, new Pair<>(scrollpane, new SettingsPanel(getTheme(), null)));
            }

            Pair<Scrollpane, SettingsPanel> pair = categoryScrollpaneHashMap.get(moduleCategory);
            Scrollpane scrollpane = pair.getKey();

            CheckButton checkButton = new CheckButton(module.getName());
            checkButton.setToggled(module.isEnabled());

            int old_height = getHeight();

            checkButton.addTickListener(() -> {
                checkButton.setToggled(module.isEnabled());
                checkButton.setName(module.getName());
            });

            checkButton.addMouseListener(new MouseListener() {
                @Override
                public void onMouseDown(MouseButtonEvent event) {
                    if (event.getButton() == 1) { 
                        pair.getValue().setModule(module);
                        pair.getValue().setX(1);
                        pair.getValue().setY(checkButton.getY() + fontRenderer.getStringHeight(checkButton.getName()));
                    }
                }

                @Override
                public void onMouseRelease(MouseButtonEvent event) {

                }

                @Override
                public void onMouseDrag(MouseButtonEvent event) {

                }

                @Override
                public void onMouseMove(MouseMoveEvent event) {

                }

                @Override
                public void onScroll(MouseScrollEvent event) {

                }
            });
            checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>() {
                @Override
                public void execute(CheckButton component, CheckButtonPoofInfo info) {
                    if (info.getAction().equals(CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE)) {
                        module.setEnabled(checkButton.isToggled());
                    }
                }
            });

            scrollpane.addChild(checkButton);
        }

        x = 10;
        y = 10;
        nexty = y;
        for (Map.Entry<Module.Category, Pair<Scrollpane, SettingsPanel>> entry : categoryScrollpaneHashMap.entrySet()) {
            Stretcherlayout stretcherlayout = new Stretcherlayout(1);
            stretcherlayout.COMPONENT_OFFSET_Y = 1;
            Frame frame = new Frame(getTheme(), stretcherlayout, entry.getKey().getName());
            Scrollpane scrollpane = entry.getValue().getKey();
            frame.addChild(scrollpane);
            frame.addChild(entry.getValue().getValue());
            scrollpane.setOriginOffsetY(0);
            scrollpane.setOriginOffsetX(0);
            frame.setCloseable(false);

            frame.setX(x);
            frame.setY(y);

            addChild(frame);

            nexty = Math.max(y + frame.getHeight() + 10, nexty);
            x += frame.getWidth() + 10;
            if (x > Wrapper.getMinecraft().displayWidth / 1.2f) {
                y = nexty;
                nexty = y;
            }
        }

        this.addMouseListener(new MouseListener() {
            private boolean isBetween(int min, int val, int max) {
                return !(val > max || val < min);
            }

            @Override
            public void onMouseDown(MouseButtonEvent event) {
                List<SettingsPanel> panels = ContainerHelper.getAllChildren(SettingsPanel.class, TurokGUI.this);
                for (SettingsPanel settingsPanel : panels) {
                    if (!settingsPanel.isVisible()) continue;
                    int[] real = GUI.calculateRealPosition(settingsPanel);
                    int pX = event.getX() - real[0];
                    int pY = event.getY() - real[1];
                    if (!isBetween(0, pX, settingsPanel.getWidth()) || !isBetween(0, pY, settingsPanel.getHeight()))
                        settingsPanel.setVisible(false);
                }
            }

            @Override
            public void onMouseRelease(MouseButtonEvent event) {

            }

            @Override
            public void onMouseDrag(MouseButtonEvent event) {

            }

            @Override
            public void onMouseMove(MouseMoveEvent event) {

            }

            @Override
            public void onScroll(MouseScrollEvent event) {

            }
        });

        // Rina.
        frame_users = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok");
        frame_users.setCloseable(false);
        frame_users.setPinneable(true);
        frame_users.setMaximumWidth(600);

        Label users = new Label("");
        users.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            String name = "";           
               
            name = mc.player.getName();
            users.setText(name + ChatFormatting.RED + " - " + "Turok " + TurokMod.TUROK_MOD_VERSION + " - " + Wrapper.getMinecraft().debugFPS);
        });

        frame_users.addChild(users);
        users.setFontRenderer(fontRendererBig);
        users.setShadow(false);

        // Wattermark.
        frame_wattermark = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "We are Turok");
        frame_wattermark.setCloseable(false);
        frame_wattermark.setPinneable(true);
        frame_wattermark.setMaximumWidth(600);

        Label wattermark = new Label("");
        wattermark.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            String name = "";           
               
            name = mc.player.getName();

            wattermark.setText(ChatFormatting.RED + "We Are Turok!");
        });

        frame_wattermark.addChild(wattermark);
        wattermark.setFontRenderer(fontRendererBig);
        wattermark.setShadow(false);

        // array.
        frame_array = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Modules Array");
        frame_array.setCloseable(false);
        frame_array.addChild(new ActiveModules());
        frame_array.setPinneable(true);
        frame_array.setMaximumWidth(600);

        // Coords.
        frame_coords = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Coordinates");
        frame_coords.setCloseable(false);
        frame_coords.setPinneable(true);
        frame_coords.setMaximumWidth(600);

        Label coords = new Label("");
        coords.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            boolean on_nether = (mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell"));

            float value = !on_nether ? 0.125f : 8;

            float posX = (float) (mc.player.posX);
            float posY = (float) (mc.player.posY);
            float posZ = (float) (mc.player.posZ);

            float poshX = (float) (mc.player.posX * value);
            float poshZ = (float) (mc.player.posZ * value);

            coords.setText("");
            coords.addLine(ChatFormatting.RED  + String.format(poshX + " - " + posY + " - " + poshZ));
            coords.addLine(ChatFormatting.BLUE + String.format(posX + " - " +  posY + " - " + posZ));
        });

        frame_coords.addChild(coords);
        coords.setFontRenderer(fontRendererBig);
        coords.setShadow(false);

        /// Turok Totem.
        frame_counts_totem = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
        frame_counts_totem.setCloseable(false);
        frame_counts_totem.setPinneable(true);
        frame_counts_totem.setMaximumWidth(600);

        Label count_totem = new Label("");
        count_totem.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            int totems = 0;

            for (int items = 0; items < 45; items++) {
                ItemStack itemStack = Wrapper.getMinecraft().player.inventory.getStackInSlot(items);

                if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                    totems += itemStack.stackSize;
                }
            }

            count_totem.setText(ChatFormatting.RED + "Totems " + String.valueOf(totems));
        });

        frame_counts_totem.addChild(count_totem);
        count_totem.setFontRenderer(fontRendererBig);
        count_totem.setShadow(false);

        // Turok Gapple.
        frame_counts_gapple = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
        frame_counts_gapple.setCloseable(false);
        frame_counts_gapple.setPinneable(true);
        frame_counts_gapple.setMaximumWidth(600);

        Label count_gapple = new Label("");
        count_gapple.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            int gapples = 0;

            for (int items = 0; items < 45; items++) {
                ItemStack itemStack = Wrapper.getMinecraft().player.inventory.getStackInSlot(items);

                if (itemStack.getItem() == Items.GOLDEN_APPLE) {
                    gapples += itemStack.stackSize;
                }
            }

            count_gapple.setText(ChatFormatting.RED + "Gapples " + String.valueOf(gapples));
        });

        frame_counts_gapple.addChild(count_gapple);
        count_gapple.setFontRenderer(fontRendererBig);
        count_gapple.setShadow(false);

        // Turok Crystal.
        frame_counts_crystal = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
        frame_counts_crystal.setCloseable(false);
        frame_counts_crystal.setPinneable(true);
        frame_counts_crystal.setMaximumWidth(600);

        Label count_crystal = new Label("");
        count_crystal.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            int crystals = 0;

            for (int items = 0; items < 45; items++) {
                ItemStack itemStack = Wrapper.getMinecraft().player.inventory.getStackInSlot(items);

                if (itemStack.getItem() == Items.END_CRYSTAL) {
                    crystals += itemStack.stackSize;
                }
            }

            count_crystal.setText(ChatFormatting.RED + "Crystals " + String.valueOf(crystals));
        });

        frame_counts_crystal.addChild(count_crystal);
        count_crystal.setFontRenderer(fontRendererBig);
        count_crystal.setShadow(false);

        // Turok EXP.
        frame_counts_exp = new Frame(getTheme(), new Stretcherlayout(1), ChatFormatting.RED + "Turok Info");
        frame_counts_exp.setCloseable(false);
        frame_counts_exp.setPinneable(true);
        frame_counts_exp.setMaximumWidth(600);

        Label count_exp = new Label("");
        count_exp.addTickListener(() -> {
            Minecraft mc = Minecraft.getMinecraft();

            int expbottles = 0;

            for (int items = 0; items < 45; items++) {
                ItemStack itemStack = Wrapper.getMinecraft().player.inventory.getStackInSlot(items);

                if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE) {
                    expbottles += itemStack.stackSize;
                }
            }

            count_exp.setText(ChatFormatting.RED + "EXPBottles " + String.valueOf(expbottles));
        });

        frame_counts_exp.addChild(count_exp);
        count_exp.setFontRenderer(fontRendererBig);
        count_exp.setShadow(false);

        frame_users.setY(60);
        frame_coords.setY(70);
        frame_array.setY(90);
        frame_wattermark.setY(100);

        frame_counts_totem.setY(10);
        frame_counts_crystal.setY(20);
        frame_counts_gapple.setY(30);
        frame_counts_exp.setY(40);

        addChild(frame_users);
        addChild(frame_coords);
        addChild(frame_array);
        addChild(frame_wattermark);

        addChild(frame_counts_totem);
        addChild(frame_counts_crystal);
        addChild(frame_counts_gapple);
        addChild(frame_counts_exp);
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