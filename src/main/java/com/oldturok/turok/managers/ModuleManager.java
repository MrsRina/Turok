package com.oldturok.turok.managers;

import com.oldturok.turok.modules.*;
import com.oldturok.turok.modules.api.Module;

import java.util.ArrayList;

public class ModuleManager {
    public ArrayList<Module> moduleList = new ArrayList<Module>();

    public ModuleManager() {
        moduleList.add(new Sprint());
        moduleList.add(new Fullbright());
        moduleList.add(new AutoClicker());
        moduleList.add(new NoFOV());
        moduleList.add(new QuickEat());

        moduleList.add(new Panic());
        moduleList.add(new Rainbow());

        moduleList.add(new ClickGUI());
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : moduleList) {
            if (module.getState()) {
                enabledModules.add(module);
            }
        }

        return enabledModules;
    }

    public Module getModule(String name) {
        Module the_module = null;
        for (Module module : moduleList) {
            if (module.name.equalsIgnoreCase(name)) {
                the_module = module;
                break;
            }
        }

        return the_module;
    }
}
