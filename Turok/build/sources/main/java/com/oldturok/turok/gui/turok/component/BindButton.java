package com.oldturok.turok.gui.turok.component;

import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.listen.KeyListener;
import com.oldturok.turok.util.TurokBind;
import com.oldturok.turok.module.Module;

import org.lwjgl.input.Keyboard;

// Update by Rina 05/03/20.
public class BindButton extends EnumButton {
    static String[] lookingFor = new String[] {"..."};
    static String[] none       = new String[] {"None"};

    boolean waiting = false;
    Module m;

    public BindButton(String name, Module m) {
        super(name, none);
        this.m = m;

        TurokBind bind = m.getBind();
        modes          = new String[] {
            bind.toString()
        };

        addKeyListener(new KeyListener() {
            @Override
            public void onKeyDown(KeyEvent event) {
                if (!waiting) return;
                int key = event.getKey();
                
                if (key == Keyboard.KEY_BACK) {
                    m.getBind().set_key(-1);

                    modes   = new String[] {m.getBind().toString()};
                    waiting = false;
                } else if (key == Keyboard.KEY_DELETE) {
                    if (!(m.getBind().is_empty())) {
                        m.getBind().set_key(-1);

                        modes   = new String[] {m.getBind().toString()};
                        waiting = false;
                    }
                } else {
                    m.getBind().set_key(key);

                    modes   = new String[] {m.getBind().toString()};
                    waiting = false;
                }
            }

            @Override
            public void onKeyUp(KeyEvent event) {}
        });

        addMouseListener(new MouseListener() {
            @Override
            public void onMouseDown(MouseButtonEvent event) {
                setModes(lookingFor);
                waiting = true;
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
    }
}