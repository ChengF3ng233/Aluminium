package cn.feng.aluminium.module.modules.visual;

import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;
import cn.feng.aluminium.value.impl.BoolValue;

public class Camera extends Module {
    public Camera() {
        super("Camera", ModuleCategory.VISUAL);
    }

    public final BoolValue motion = new BoolValue("Motion Camera", true);
}
