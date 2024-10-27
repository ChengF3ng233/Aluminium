package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.font.impl.UFontRenderer;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.User;
import cn.feng.aluminium.ui.music.api.bean.login.LoginState;
import cn.feng.aluminium.ui.music.api.bean.login.QRCode;
import cn.feng.aluminium.ui.music.gui.page.Page;
import cn.feng.aluminium.ui.music.thread.FetchPlaylistThread;
import cn.feng.aluminium.util.misc.TimeUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;

import java.awt.*;
import java.time.Period;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class UserPage extends Page {
    private Thread fetchThread = null;
    private QRCode qrCode = null;

    @Override
    public void render() {
        preRender();
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
                new FetchPlaylistThread().start();
                qrCode = null;
            });
            fetchThread.setName("Music-Login");
            fetchThread.start();
        }

        if (fetchThread != null && !fetchThread.isAlive()) {
            fetchThread = null;
        }

        // Render
        UFontRenderer noto = FontManager.noto(17);
        float centerX = x + width / 2f;
        if (qrCode != null) {
            LoginState state = qrCode.getResult().getState();
            String text = state == LoginState.WAITING_SCAN ? "请用手机APP扫码登陆" : (state == LoginState.WAITING_CONFIRM ? "请在手机APP上确认登陆" : (state == LoginState.EXPIRED ? "二维码已过期，正在重新获取……" : "登陆成功！"));
            noto.drawCenteredStringH(text, centerX, y + 20f, Color.WHITE.getRGB());
            RenderUtil.drawImage(qrCode.getImage(), centerX - 64f, y + 50f, 128f, 128f);
        } else if (user.getNickname() == null) {
            noto.drawCenteredStringH("请稍侯……", centerX, y + 20f, Color.WHITE.getRGB());
        } else {
            RenderUtil.bindTexture(user.getAvatarImage());
            ShaderUtil.drawRoundTextured(centerX - 20f, y + 20f, 40f, 40f, 20f, 1f);
            noto.drawCenteredStringH(user.getNickname(), centerX, y + 70f, Color.WHITE.getRGB());
            FontManager.noto(15).drawCenteredStringH(user.getSignature(), centerX, y + 85f, Theme.layerBackground.getRGB());

            Period timeBetween = TimeUtil.getTimeBetween(user.getCreateTime(), System.currentTimeMillis());
            String age = timeBetween.getYears() + "年" + timeBetween.getMonths() + "月" + timeBetween.getDays() + "天";
            FontManager.noto(15).drawCenteredStringH("lv." + user.getLevel() + "  |  听过 " + user.getListenedSongs() + " 首歌  |  村龄 " + age, centerX, y + 95f, Theme.layerBackground.getRGB());
        }
        postRender();
    }

    public void logout() {
        new Thread(() -> {
            fetchThread = null;
            qrCode = null;
            Aluminium.INSTANCE.musicManager.getUser().logout();
            MusicApi.logout();
            Aluminium.INSTANCE.musicManager.getScreen().changePage(this, false);
        }).start();
    }
}
