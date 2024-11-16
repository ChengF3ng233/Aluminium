package cn.feng.aluminium.module.modules.visual;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class ClickGuiMod extends Module {
    public ClickGuiMod() {
        super("ClickGui", ModuleCategory.VISUAL, Keyboard.KEY_RSHIFT);
        lock();
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(Aluminium.INSTANCE.uiManager.getClickGui());
    }
}
