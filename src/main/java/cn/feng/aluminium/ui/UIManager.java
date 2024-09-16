package cn.feng.aluminium.ui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventRender2D;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.login.LoginState;
import cn.feng.aluminium.ui.music.api.bean.login.QRCode;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.misc.ChatUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.RoundedUtil;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class UIManager extends Util {
    public UIManager() {
        Aluminium.INSTANCE.eventManager.register(this);
    }

    private QRCode qrCode;
    private Thread t;

    @EventTarget
    private void onRender2D(EventRender2D event) {
        SkijaFontLoader.playwrite.drawGlowString("Aluminium", 10, 10, 45, Color.WHITE, false);
        RoundedUtil.drawVaryingRound(50, 50, 100, 100, 0, 0, 5, 5, Theme.light);

        if (t == null) {
            t = new Thread(() -> {
                qrCode = MusicApi.generateQRCode();
            });
            t.start();
        }

        if (qrCode != null) {
            RenderUtil.drawImage(qrCode.getImage(), 100,  20, 128, 128);
            if (qrCode.getResult().getState() == LoginState.SUCCEEDED) {
                Aluminium.INSTANCE.musicManager.getUser().setCookie(qrCode.getResult().getResponse().get("cookie").getAsString());
                ChatUtil.sendMessage(Aluminium.INSTANCE.musicManager.getUser().getCookie());
                qrCode = null;
            }
        }
    }
}
