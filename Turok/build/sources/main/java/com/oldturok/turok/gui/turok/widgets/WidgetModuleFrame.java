package com.oldturok.turok.gui.turok.widgets;

import com.oldturok.turok.gui.rgui.component.listen.UpdateListener;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.AlignedComponent;
import com.oldturok.turok.gui.rgui.render.AbstractComponentUI;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.poof.use.FramePoof;
import com.oldturok.turok.gui.rgui.util.Docking;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.util.ColourHolder;
import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.util.Wrapper;
import com.oldturok.turok.TurokMessage;
import com.oldturok.turok.gui.turok.*;
import com.oldturok.turok.util.Bind;
import com.oldturok.turok.TurokMod;

import org.lwjgl.opengl.GL11;

import com.oldturok.turok.util.TurokGL; // TurokGL.

// Rina.
// Modfify.
public class WidgetModuleFrame <T extends Frame> extends AbstractComponentUI<Frame> {
    ColourHolder frameColour   = TurokGUI.primaryColour.setA(100);
    ColourHolder outlineColour = frameColour.darker();

    Component yLineComponent = null;
    Component xLineComponent = null;
    Component centerXComponent = null;
    Component centerYComponent = null;

    boolean centerX = false;
    boolean centerY = false;

    int xLineOffset = 0;

    boolean effect_one = false;
    Boolean effect_two = false;

    int effect_tick = 0;

    int widht;
    int height;

    int color = 0;

    public static int speed_effect = 1;

    private static final RootFontRenderer ff = new RootFontRenderer(0.90f);

    @Override
    public void renderComponent(Frame component, FontRenderer fontRenderer) {
        if (component.getOpacity() == 0) return;

        TurokGL.DisableGL(GL11.GL_TEXTURE_2D);

        if (component.isPinneable()) {
            if (component.isPinned()) {
                TurokGL.refresh_color(color, 0, 0, 150);

                RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());
            } else {
                if (effect_one) {
                    color += 1;
                } else {
                    color -= 1;
                }

                if (color >= 255) {
                    effect_one = false;
                }

                if (color <= 0) {
                    effect_one = true;
                }

                TurokGL.refresh_color(color, 0, 0, 150);

                RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());
            }

            TurokGL.refresh_color(255, 255, 255, 255);
            ff.drawString(1, 1, component.getTitle());
        } else {
            TurokGL.refresh_color(0, 0, 0, 150);
            RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), component.getHeight());

            TurokGL.refresh_color(0, 0, 0, 255);
            RenderHelper.drawFilledRectangle(0, 0, component.getWidth(), ff.getStringHeight(component.getTitle()) + 2);

            TurokGL.refresh_color(0, 0, 0, 255);
            ff.drawString(1, 1, component.getTitle());
        }

        TurokGL.FixRefreshColor();
    }

    @Override
    public void handleMouseRelease(Frame component, int x, int y, int button) {
        yLineComponent   = null;
        xLineComponent   = null;
        centerXComponent = null;
        centerYComponent = null;
    }

    @Override
    public void handleMouseDrag(Frame component, int x, int y, int button) {
        super.handleMouseDrag(component, x, y, button);
    }

    @Override
    public void handleAddComponent(Frame component, Container container) {
        super.handleAddComponent(component, container);
        component.setOriginOffsetY(component.getTheme().getFontRenderer().getFontHeight() + 3);
        component.setOriginOffsetX(3);

        component.addMouseListener(new MouseListener() {
            @Override
            public void onMouseDown(MouseButtonEvent event) {
                int y = event.getY();
                int x = event.getX();

                if (y < 0) {
                    if (x < component.getWidth() && x > ff.getStringWidth(component.getTitle()) - component.getWidth()) {
                        if (component.isPinneable()) {
                            if (component.isPinned()) {
                                component.setPinned(!(component.isPinned()));
                            }
                        }
                    }
                }
            }

            @Override
            public void onMouseRelease(MouseButtonEvent event) {}

            @Override
            public void onMouseDrag(MouseButtonEvent event) {}

            @Override
            public void onMouseMove(MouseMoveEvent event) {}

            @Override
            public void onScroll(MouseScrollEvent event) {}
        });

        component.addUpdateListener(new UpdateListener() {
            @Override
            public void updateSize(Component component, int oldWidth, int oldHeight) {
                if (component instanceof Frame) {
                    TurokGUI.dock((Frame) component);
                }
            }
            @Override
            public void updateLocation(Component component, int oldX, int oldY) { }
        });

        component.addPoof(new Frame.FrameDragPoof<Frame, Frame.FrameDragPoof.DragInfo>() {
            @Override
            public void execute(Frame component, DragInfo info) {
                int x = info.getX();
                int y = info.getY();
                yLineComponent = null;
                xLineComponent = null;

                component.setDocking(Docking.NONE);

                if (x < 5) {
                    x = 0;
                    ContainerHelper.setAlignment(component, AlignedComponent.Alignment.LEFT);
                    component.setDocking(Docking.LEFT);
                }

                int diff = (x+component.getWidth()) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().displayWidth;
                if (-diff < 5){
                    x = (Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale())-component.getWidth();
                    ContainerHelper.setAlignment(component, AlignedComponent.Alignment.RIGHT);
                    component.setDocking(Docking.RIGHT);
                }

                if (y < 5) {
                    y = 0;
                    if (component.getDocking().equals(Docking.RIGHT))
                        component.setDocking(Docking.TOPRIGHT);
                    else if (component.getDocking().equals(Docking.LEFT))
                        component.setDocking(Docking.TOPLEFT);
                    else
                        component.setDocking(Docking.TOP);
                }

                diff = (y+component.getHeight()) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().displayHeight;
                if (-diff < 5) {
                    y = (Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale()) - component.getHeight();

                    if (component.getDocking().equals(Docking.RIGHT))
                        component.setDocking(Docking.BOTTOMRIGHT);
                    else if (component.getDocking().equals(Docking.LEFT))
                        component.setDocking(Docking.BOTTOMLEFT);
                    else
                        component.setDocking(Docking.BOTTOM);
                }

                if (Math.abs(((x + component.getWidth() / 2) * DisplayGuiScreen.getScale() * 2) - Wrapper.getMinecraft().displayWidth) < 5) { // Component is center-aligned on the x axis
                    xLineComponent = null;
                    centerXComponent = component;
                    centerX = true;
                    x = (Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2)) - component.getWidth() / 2;
                    if (component.getDocking().isTop()) {
                        component.setDocking(Docking.CENTERTOP);
                    } else if (component.getDocking().isBottom()){
                        component.setDocking(Docking.CENTERBOTTOM);
                    } else {
                        component.setDocking(Docking.CENTERVERTICAL);
                    }
                    ContainerHelper.setAlignment(component, AlignedComponent.Alignment.CENTER);
                } else {
                    centerX = false;
                }

                if (Math.abs(((y + component.getHeight() / 2) * DisplayGuiScreen.getScale() * 2) - Wrapper.getMinecraft().displayHeight) < 5) { // Component is center-aligned on the y axis
                    yLineComponent = null;
                    centerYComponent = component;
                    centerY = true;
                    y = (Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2)) - component.getHeight() / 2;
                    if (component.getDocking().isLeft()) {
                        component.setDocking(Docking.CENTERLEFT);
                    } else if (component.getDocking().isRight()) {
                        component.setDocking(Docking.CENTERRIGHT);
                    } else if (component.getDocking().isCenterHorizontal()) {
                        component.setDocking(Docking.CENTER);
                    } else {
                        component.setDocking(Docking.CENTERHOIZONTAL);
                    }
                } else {
                    centerY = false;
                }

                info.setX(x);
                info.setY(y);
            }
        });
    }
}
