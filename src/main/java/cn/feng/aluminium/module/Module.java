package cn.feng.aluminium.module;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class Module extends Util {
    private final String name;
    private String suffix = "";
    private final ModuleCategory category;
    private int keyBind = -1;
    private boolean enabled = false;
    private boolean locked = false;
    private final List<Value<?>> valueList = new ArrayList<>();

    public Module(String name, ModuleCategory category) {
        this.name = name;
        this.category = category;
    }

    public Module(String name, ModuleCategory category, int keyBind) {
        this.name = name;
        this.category = category;
        this.keyBind = keyBind;
    }

    protected void lock() {
        locked = true;
    }

    protected void unlock() {
        locked = false;
    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }

    public void toggle() {
        boolean newState = !enabled;
        if (newState) {
            if (!locked)
                Aluminium.INSTANCE.eventManager.register(this);
            onEnable();
        } else {
            if (!locked)
                Aluminium.INSTANCE.eventManager.unregister(this);
            onDisable();
        }
        if (!locked) enabled = newState;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        toggle();
    }

    public boolean isLocked() {
        return locked;
    }

    public List<Value<?>> getValueList() {
        return valueList;
    }
}
