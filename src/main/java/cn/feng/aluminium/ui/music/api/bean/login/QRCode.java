package cn.feng.aluminium.ui.music.api.bean.login;

import cn.feng.aluminium.ui.music.api.MusicApi;

import java.awt.image.BufferedImage;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class QRCode {
    private String key;
    private BufferedImage image;
    private LoginResult result;

    private QRCode parent;

    public QRCode(String key, BufferedImage image) {
        this.key = key;
        this.image = image;
        startMonitor();
    }

    public BufferedImage getImage() {
        return image;
    }

    public LoginResult getResult() {
        return result == null? new LoginResult(LoginState.WAITING_SCAN, null) : result;
    }

    private void startMonitor() {
        Thread thread = new Thread(() -> {
            do {
                result = MusicApi.getLoginResult(key);
                if (parent != null) parent.result = result;
            } while (result.getState() == LoginState.WAITING_SCAN || result.getState() == LoginState.WAITING_CONFIRM);

            if (result.getState() == LoginState.SUCCEEDED) return;

            // 新生成的二维码，把结果传递回来
            QRCode newCode = MusicApi.generateQRCode();
            newCode.parent = this;
            key = newCode.key;
            image = newCode.image;
        });

        thread.setName("Music-QRCode");
        thread.start();
    }
}
