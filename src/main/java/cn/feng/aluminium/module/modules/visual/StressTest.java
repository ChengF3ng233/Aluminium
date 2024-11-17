package cn.feng.aluminium.module.modules.visual;

import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventNano;
import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.nanovg.NanoFontLoader;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class StressTest extends Module {
    public StressTest() {
        super("StressTest", ModuleCategory.VISUAL);
    }

    @EventTarget
    private void onNano(EventNano e) {
        for (int i = 0; i < 1000; i++) {
            NanoFontLoader.pingfang.drawGlowString("This is a stress test.", ThreadLocalRandom.current().nextInt(0, mc.displayWidth), ThreadLocalRandom.current().nextInt(0, mc.displayHeight), 20f, 5f, NanoVG.NVG_ALIGN_CENTER, Color.WHITE);
            //FontManager.pingfang(20).drawString("This is a stress test.", ThreadLocalRandom.current().nextInt(0, mc.displayWidth), ThreadLocalRandom.current().nextInt(0, mc.displayHeight),  Color.WHITE.getRGB());
        }
    }
}
