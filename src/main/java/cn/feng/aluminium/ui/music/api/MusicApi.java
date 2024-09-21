package cn.feng.aluminium.ui.music.api;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.music.api.bean.Album;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.api.bean.User;
import cn.feng.aluminium.ui.music.api.bean.login.LoginResult;
import cn.feng.aluminium.ui.music.api.bean.login.LoginState;
import cn.feng.aluminium.ui.music.api.bean.login.QRCode;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.DataUtil;
import cn.feng.aluminium.util.data.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    private static Music parseMusic(JsonObject musicObj) {
        String name = musicObj.get("name").getAsString();
        long id = musicObj.get("id").getAsLong();
        StringBuilder artist = new StringBuilder();
        for (JsonElement e : musicObj.get("ar").getAsJsonArray()) {
            JsonObject artistObj = e.getAsJsonObject();
            artist.append(artistObj.get("name").getAsString()).append(",");
        }
        if (artist.length() == 0) artist.append("未知音乐家");
        int duration = musicObj.get("dt").getAsInt();
        Album album = parseAlbum(musicObj.get("al").getAsJsonObject());
        Music music = new Music(
                id,
                name,
                artist.toString(),
                album,
                duration
        );
        Aluminium.INSTANCE.musicManager.getMusicMap().put(id, music);
        return music;
    }

    private static Album parseAlbum(JsonObject albumObj) {
        long id = albumObj.get("id").getAsLong();
        String name = albumObj.get("name").getAsString();
        String coverUrl = albumObj.get("picUrl").getAsString();
        Album album = new Album(id, name, coverUrl, HttpUtil.downloadImage(wrapImage(coverUrl)));
        Aluminium.INSTANCE.musicManager.getAlbumMap().put(id, album);
        return album;
    }

    public static Playlist getDailySongs() {
        JsonArray songs = fetchObjectArray("/recommend/songs", "data", "dailySongs");
        List<Music> musicList = new ArrayList<>();
        for (JsonElement e : songs) {
            JsonObject musicObj = e.getAsJsonObject();
            Music music = parseMusic(musicObj);
            musicList.add(music);
        }
        Music first = musicList.get(0);
        Playlist playlist = new Playlist(-1, "每日推荐", "根据常听推荐", "云音乐官方", first.getAlbum().getCoverUrl(), HttpUtil.downloadImage(wrapImage(first.getAlbum().getCoverUrl())), musicList);
        Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        return playlist;
    }

    private static String wrapImage(String url) {
        return url + "?param=300y300";
    }

    public static String getSongUrl(long id) {
        JsonObject obj = fetchObject("/song/download/url/v1?id=" + id + "&level=exhigh&os=pc", "data");
        return obj.get("url").getAsString();
    }

    private static JsonObject fetchObject(String api) {
        String fetch = fetch(api);
        return DataUtil.gson.fromJson(fetch, JsonObject.class);
    }

    private static JsonObject fetchObject(String api, String... children) {
        JsonObject object = fetchObject(api);
        for (String child : children) {
            object = object.get(child).getAsJsonObject();
        }
        return object;
    }

    private static JsonArray fetchObjectArray(String api, String obj, String array) {
        JsonObject object = fetchObject(api).get(obj).getAsJsonObject();
        return object.get(array).getAsJsonArray();
    }

    private static String wrap(String api) {
        return api + (api.contains("?")? "&" : "?") + "timestamp=" + System.currentTimeMillis();
    }

    public static void getUserInfo() {
        JsonObject profile = fetchObject("/login/status", "data", "profile");
        User user = Aluminium.INSTANCE.musicManager.getUser();
        user.setId(profile.get("userId").getAsLong());
        updateUserInfo();
    }

    public static void updateUserInfo() {
        User user = Aluminium.INSTANCE.musicManager.getUser();
        JsonObject detail = fetchObject("/user/detail?uid=" + user.getId());
        user.setLevel(detail.get("level").getAsInt());
        user.setListenedSongs(detail.get("listenSongs").getAsInt());

        JsonObject profile = detail.get("profile").getAsJsonObject();
        user.setNickname(profile.get("nickname").getAsString());
        user.setAvatarUrl(profile.get("avatarUrl").getAsString());
        user.setSignature(profile.get("signature").getAsString());
        user.setCreateTime(profile.get("createTime").getAsLong());
        user.setAvatarImage(HttpUtil.downloadImage(user.getAvatarUrl()));
    }

    public static void logout() {
        fetch("/logout");
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
