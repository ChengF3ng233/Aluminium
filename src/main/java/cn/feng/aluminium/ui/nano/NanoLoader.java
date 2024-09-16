package cn.feng.aluminium.ui.nano;

import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class NanoLoader {
    public static long ctx;
    public static void init() {
        ctx = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES);
        NanoVG.nvgShapeAntiAlias(ctx, true);
    }
    public static void destroy() {
        NanoVGGL3.nvgDelete(ctx);
    }
}
