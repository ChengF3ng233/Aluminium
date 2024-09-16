package cn.feng.aluminium.ui.music.api;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.bean.login.LoginResult;
import cn.feng.aluminium.ui.music.api.bean.login.LoginState;
import cn.feng.aluminium.ui.music.api.bean.login.QRCode;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.DataUtil;
import com.google.gson.JsonObject;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicApi extends Util {
    private static final String host = "https://music.chengf3ng.top";

    public static String fetch(String api, String cookie) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = new FormBody.Builder()
                .add("cookie", cookie)
                .build();

        Request request = new Request.Builder()
                .url(host + wrap(api))
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            logger.error("Failed to fetch api: {}", api, e);
            throw new RuntimeException(e);
        }
    }

    private static String fetch(String api) {
        String result = null;

        do {
            try {
                result = fetch(api, Aluminium.INSTANCE.musicManager.getUser().getCookie());
            } catch (Exception e) {
                logger.error("Failed to fetch {}. Retry...", api, e);
            }
        } while (result == null);

        return result;
    }

    private static JsonObject fetchObject(String api) {
        String fetch = fetch(api);
        return DataUtil.gson.fromJson(fetch, JsonObject.class);
    }

    private static JsonObject fetchObject(String api, String child) {
        return fetchObject(api).get(child).getAsJsonObject();
    }

    private static String wrap(String api) {
        return api + (api.contains("?")? "&" : "?") + "timestamp=" + System.currentTimeMillis();
    }

    public static QRCode generateQRCode() {
        try {
            JsonObject keyData = fetchObject("/login/qr/key", "data");
            String key = keyData.get("unikey").getAsString();

            JsonObject codeData = fetchObject("/login/qr/create?key=" + key + "&qrimg=true", "data");
            String codeBase64 = codeData.get("qrimg").getAsString();

            String base64Image = codeBase64.split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);
            bis.close();

            return new QRCode(key, image);
        } catch (IOException e) {
            logger.error("Failed to generate qr code.", e);
            throw new RuntimeException(e);
        }
    }


    public static LoginResult getLoginResult(String key) {
        JsonObject object = fetchObject("/login/qr/check?key=" + key);
        int code = object.get("code").getAsInt();

        switch (code) {
            case 801: return new LoginResult(LoginState.WAITING_SCAN, object);
            case 802 : return new LoginResult(LoginState.WAITING_CONFIRM, object);
            case 803: return new LoginResult(LoginState.SUCCEEDED, object);
            default: return new LoginResult(LoginState.EXPIRED, object);
        }
    }
}
