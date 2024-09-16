package cn.feng.aluminium.util.render.blur;

import cn.feng.aluminium.util.render.RenderUtil;
import net.minecraft.client.shader.Framebuffer;


/**
 * 用于非GuiInGame界面绘制模糊效果
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class BlurUtil {
    private static Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);
    public static Framebuffer framebuffer = new Framebuffer(1, 1, true);

    public static void blur(Runnable runnable) {
        runnable.run();
    }

    public static void processStart() {
        stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
        stencilFramebuffer.framebufferClear();
        stencilFramebuffer.bindFramebuffer(false);
    }

    public static void blurEnd() {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, 2, 1);
    }

    public static void blurEnd(int iterations, int offset) {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, iterations, offset);
    }

    public static void bloomEnd() {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, 2, 1);
    }

    public static void bloomEnd(int iterations, int offset) {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, iterations, offset);
    }
}
