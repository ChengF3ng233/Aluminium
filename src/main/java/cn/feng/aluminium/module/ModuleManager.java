package cn.feng.aluminium.module;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventKey;
import cn.feng.aluminium.module.modules.visual.MusicScreenMod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class ModuleManager {
    private final List<Module> moduleList = new ArrayList<>();

    public ModuleManager() {
        Aluminium.INSTANCE.eventManager.register(this);
    }

    private void addModule(Module module) {
        moduleList.add(module);
    }

    public void init() {
        addModule(new MusicScreenMod());
    }

    @EventTarget
    private void onKey(EventKey e) {
        moduleList.stream().filter(module -> module.getKeyBind() == e.getKey()).forEach(Module::toggle);
    }
}
