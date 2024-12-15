package cn.feng.aluminium.module;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventKey;
import cn.feng.aluminium.module.modules.movement.Sprint;
import cn.feng.aluminium.module.modules.visual.Camera;
import cn.feng.aluminium.module.modules.visual.ClickGuiMod;
import cn.feng.aluminium.module.modules.visual.MusicScreenMod;
import cn.feng.aluminium.module.modules.visual.StressTest;
import cn.feng.aluminium.module.modules.visual.hud.HUD;
import cn.feng.aluminium.ui.widget.Widget;
import cn.feng.aluminium.util.exception.ModuleNotFoundException;
import cn.feng.aluminium.util.exception.ValueLoadException;
import cn.feng.aluminium.value.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class ModuleManager {
    private final List<Module> moduleList = new ArrayList<>();

    public ModuleManager() {
        Aluminium.INSTANCE.eventManager.register(this);
    }

    /**
     * Register module
     *
     * @param module module
     */
    private void register(Module module) {
        register(module, module.getClass().getDeclaredFields());
    }

    /**
     * Register widget as a module
     *
     * @param widget widget
     */
    public void register(Widget widget) {
        Module widgetModule = new Module(widget.getName(), ModuleCategory.WIDGET);
        if (widget.isDefaultOn()) widgetModule.toggle();
        this.moduleList.add(widgetModule);

        for (Field field : widget.getClass().getDeclaredFields()) {
            addValue(field, widgetModule, widget);
        }
    }

    private void addValue(Field field, Module module, Object obj) {
        field.setAccessible(true);
        if (field.getType().getSuperclass() == Value.class) {
            try {
                module.getValueList().add((Value<?>) field.get(obj));
            } catch (IllegalAccessException e) {
                throw new ValueLoadException("Failed to load " + module.getName() + ", " + field.getName());
            }
        }
    }

    private void register(Module module, Field[] classFields) {
        this.moduleList.add(module);

        for (Field field : classFields) {
            addValue(field, module, module);
        }

        if (module.isEnabled()) {
            Aluminium.INSTANCE.eventManager.register(module);
        }
    }

    public Module getModule(Class<? extends Module> klass) {
        for (Module module : moduleList) {
            if (klass == module.getClass()) return module;
        }

        throw new ModuleNotFoundException(klass.getName());
    }


    public Module getModule(Widget widget) {
        for (Module module : moduleList) {
            if (widget.getName().equals(module.getName())) return module;
        }

        throw new ModuleNotFoundException(widget.getName());
    }

    public List<Module> getModuleByCategory(ModuleCategory category) {
        return moduleList.stream().filter(it -> it.getCategory() == category).sorted(Comparator.comparing(Module::getName)).collect(Collectors.toList());
    }


    public void init() {
        register(new MusicScreenMod());
        register(new HUD());
        register(new ClickGuiMod());
        register(new StressTest());
        register(new Camera());
        register(new Sprint());

        for (ModuleCategory category : ModuleCategory.values()) {
            int count = ThreadLocalRandom.current().nextInt(5, 30);
            for (int i = 0; i < count; i++) {
                register(new Module("TestModule" + i, category));
            }
        }
    }

    @EventTarget
    private void onKey(EventKey e) {
        moduleList.stream().filter(module -> module.getKeyBind() == e.getKey()).forEach(Module::toggle);
    }
}
