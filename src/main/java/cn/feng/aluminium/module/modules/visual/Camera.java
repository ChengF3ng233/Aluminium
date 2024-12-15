package cn.feng.aluminium.module.modules.visual;

import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;
import cn.feng.aluminium.value.impl.BoolValue;
import cn.feng.aluminium.value.impl.NumberValue;

public class Camera extends Module {
    public Camera() {
        super("Camera", ModuleCategory.VISUAL);
    }

    public final BoolValue motion = new BoolValue("Motion Camera", true);
    public final NumberValue motionInterpolation = new NumberValue("Motion Interpolation", 0.1f, 0.01f, 0.7f, 0.01f);
}
