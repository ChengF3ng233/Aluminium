package cn.feng.aluminium.ui.music.ui.page.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.SkijaFontLoader;
import cn.feng.aluminium.ui.font.SkijaFontRenderer;
import cn.feng.aluminium.ui.font.awt.AWTFontLoader;
import cn.feng.aluminium.ui.font.awt.AWTFontRenderer;
import cn.feng.aluminium.ui.font.awt.CenterType;
import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.User;
import cn.feng.aluminium.ui.music.api.bean.login.LoginState;
import cn.feng.aluminium.ui.music.api.bean.login.QRCode;
import cn.feng.aluminium.ui.music.ui.Theme;
import cn.feng.aluminium.ui.music.ui.page.AbstractPage;
import cn.feng.aluminium.util.misc.TimeUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.time.Period;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class UserPage extends AbstractPage {
    private Thread fetchThread = null;
    private QRCode qrCode = null;

    @Override
    public void update(float renderX, float renderY, float width, float height, int mouseX, int mouseY, float centerX, float centerY, float scale) {
        super.update(renderX, renderY, width, height, mouseX, mouseY, centerX, centerY, scale);
        this.pageHeight = height * 2;
    }

    @Override
    public void render() {
        // Launch thread
        User user = Aluminium.INSTANCE.musicManager.getUser();

        if (user.getCookie().isEmpty() && fetchThread == null) {
            fetchThread = new Thread(() -> {
                qrCode = MusicApi.generateQRCode();

                while (qrCode.getResult().getState() != LoginState.SUCCEEDED) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                user.setCookie(qrCode.getResult().getResponse().get("cookie").getAsString());
                MusicApi.getUserInfo();
                qrCode = null;
            });
            fetchThread.setName("Music-Login");
            fetchThread.start();
        }

        if (fetchThread != null && !fetchThread.isAlive()) {
            fetchThread = null;
        }

        // Render
        AWTFontRenderer noto = AWTFontLoader.noto(20f);
        float centerX = renderX + width / 2f;
        preDraw();
        if (qrCode != null) {
            LoginState state = qrCode.getResult().getState();
            String text = state == LoginState.WAITING_SCAN? "请用手机APP扫码登陆" : (state == LoginState.WAITING_CONFIRM? "请在手机APP上确认登陆" : (state == LoginState.EXPIRED? "二维码已过期，正在重新获取……" : "登陆成功！"));
            noto.drawCenteredString(text, centerX, scrolledY + 20f, Color.WHITE, CenterType.Horizontal);
            RenderUtil.drawImage(qrCode.getImage(), centerX - 64f, scrolledY + 50f, 128f, 128f);
        } else if (user.getNickname() == null) {
            noto.drawCenteredString("请稍后……", centerX, scrolledY + 20f, Color.WHITE, CenterType.Horizontal);
        } else {
            RenderUtil.bindTexture(user.getAvatarImage());
            ShaderUtil.drawRoundTextured(centerX - 20f, scrolledY + 20f, 40f, 40f, 20f, 1f);
            noto.drawCenteredString(user.getNickname(), centerX, scrolledY + 70f, Color.WHITE, CenterType.Horizontal);
            noto.drawCenteredString(user.getSignature(), centerX, scrolledY + 85f, Theme.grey, CenterType.Horizontal);

            Period timeBetween = TimeUtil.getTimeBetween(user.getCreateTime(), System.currentTimeMillis());
            String age = timeBetween.getYears() + "年" + timeBetween.getMonths() + "月" + timeBetween.getDays() + "天";
            noto.drawCenteredString("lv." + user.getLevel() + "  |  听过 " + user.getListenedSongs() + " 首歌  |  村龄 " + age, centerX, scrolledY + 95f, Theme.grey, CenterType.Horizontal);
        }
        postDraw();
    }

    public void logout() {
        new Thread(() -> {
            fetchThread = null;
            qrCode = null;
            Aluminium.INSTANCE.musicManager.getUser().logout();
            MusicApi.logout();
        }).start();
    }
}
