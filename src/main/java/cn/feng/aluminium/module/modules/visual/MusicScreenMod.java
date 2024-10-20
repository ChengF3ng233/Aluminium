package cn.feng.aluminium.module.modules.visual;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class MusicScreenMod extends Module {
    public MusicScreenMod() {
        super("MusicScreen", ModuleCategory.VISUAL, Keyboard.KEY_RCONTROL);
        lock();
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(Aluminium.INSTANCE.musicManager.getScreen());
    }
}
