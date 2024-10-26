package cn.feng.aluminium.ui.music.api;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.config.ConfigManager;
import cn.feng.aluminium.ui.music.api.bean.Album;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.api.bean.User;
import cn.feng.aluminium.ui.music.api.bean.login.LoginResult;
import cn.feng.aluminium.ui.music.api.bean.login.LoginState;
import cn.feng.aluminium.ui.music.api.bean.login.QRCode;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.DataUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static cn.feng.aluminium.util.data.HttpUtil.downloadImage;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicApi extends Util {
    private static final String host = "https://music.skidder.top";

    public static String fetch(String api, String cookie) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897));
        OkHttpClient client = new OkHttpClient.Builder()/*.proxy(proxy)*/.build();
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
        if (Aluminium.INSTANCE.musicManager.getMusicMap().containsKey(id))
            return Aluminium.INSTANCE.musicManager.getMusicMap().get(id);
        StringBuilder artist = new StringBuilder();
        for (JsonElement e : musicObj.get("ar").getAsJsonArray()) {
            JsonObject artistObj = e.getAsJsonObject();
            artist.append(artistObj.get("name").getAsString()).append(",");
        }
        if (artist.length() == 0) {
            artist.append("未知音乐家");
        } else artist.deleteCharAt(artist.length() - 1);
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

    public static List<Music> fetchMusicList(Playlist playList) throws IOException {
        JsonObject object = fetchObject("/playlist/track/all?id=" + playList.getId() + "&offset=" + playList.getMusicList().size());
        JsonArray songs = object.get("songs").getAsJsonArray();

        if (songs.size() == 0) {
            playList.setCompletelyDownloaded(true);
            return Collections.emptyList();
        }

        List<Music> musics = new ArrayList<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            musics.add(parseMusic(obj));
        }

        playList.getMusicList().addAll(musics);
        return musics;
    }

    public static Playlist getCloudMusics() {
        JsonArray data = fetchArray("/user/cloud", "data");
        List<Music> musics = new ArrayList<>();
        for (JsonElement e : data) {
            JsonObject songObj = e.getAsJsonObject().get("simpleSong").getAsJsonObject();
            musics.add(parseMusic(songObj));
        }
        return new Playlist(-1, "我的音乐云盘", "你的网易云云盘", Aluminium.INSTANCE.musicManager.getUser().getNickname(), musics.get(0).getAlbum().getCoverUrl(), musics.get(0).getAlbum().getCoverImage(), musics);
    }

    public static List<Music> fetchCloudMusics(Playlist playlist) {
        JsonArray songs = fetchArray("/user/cloud?offset=" + playlist.getMusicList().size(), "data");
        if (songs.size() == 0) {
            playlist.setCompletelyDownloaded(true);
            return Collections.emptyList();
        }
        List<Music> musics = new ArrayList<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject().get("simpleSong").getAsJsonObject();
            musics.add(parseMusic(obj));
        }
        playlist.getMusicList().addAll(musics);
        return musics;
    }

    private static Album parseAlbum(JsonObject albumObj) {
        long id = albumObj.get("id").getAsLong();
        if (Aluminium.INSTANCE.musicManager.getAlbumMap().containsKey(id))
            return Aluminium.INSTANCE.musicManager.getAlbumMap().get(id);
        String name = albumObj.get("name").getAsString();
        String coverUrl = albumObj.get("picUrl").getAsString();
        Album album = new Album(id, name, coverUrl, downloadImage(wrapImage(coverUrl)));
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
        Playlist playlist = new Playlist(-1, "每日歌曲推荐", "根据你的音乐口味生成，每天6:00更新", "云音乐官方", first.getAlbum().getCoverUrl(), downloadImage(wrapImage(first.getAlbum().getCoverUrl())), musicList);
        playlist.setCompletelyDownloaded(true);
        Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        return playlist;
    }

    public static List<Playlist> getRecommendedPlayLists() throws IOException {
        JsonArray playlistArray = fetchArray("/recommend/resource", "recommend");

        List<Playlist> result = new ArrayList<>();

        for (JsonElement element : playlistArray) {
            JsonObject obj = element.getAsJsonObject();
            String description = (obj.get("description") instanceof JsonNull || obj.get("description") == null) ? "没有描述，你自己进去看看" : obj.get("description").getAsString();
            File file = new File(ConfigManager.coverDir, "playlist_" + obj.get("id").getAsLong() + ".jpg");

            downloadImage(obj.get("picUrl").getAsString(), file, true);

            result.add(new Playlist(
                    obj.get("id").getAsLong(),
                    obj.get("name").getAsString(),
                    description,
                    "Unknown artist",
                    obj.get("picUrl").getAsString(),
                    ImageIO.read(file)
            ));
        }

        for (Playlist playlist : result) {
            Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        }
        return result;
    }

    private static String wrapImage(String url) {
        return url + "?param=300y300";
    }

    public static String getSongUrl(long id) {
        JsonArray array = fetchArray("/song/url/v1?id=" + id + "&level=exhigh", "data");
        for (JsonElement e : array) {
            JsonObject obj = e.getAsJsonObject();
            return obj.get("url").getAsString();
        }
        return null;
    }

    private static JsonObject fetchObject(String api) {
        String fetch = fetch(api);
        return DataUtil.gson.fromJson(fetch, JsonObject.class);
    }

    private static JsonArray fetchArray(String api, String array) {
        String fetch = fetch(api);
        return DataUtil.gson.fromJson(fetch, JsonObject.class).get(array).getAsJsonArray();
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
        return api + (api.contains("?") ? "&" : "?") + "timestamp=" + System.currentTimeMillis();
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
        user.setAvatarImage(downloadImage(user.getAvatarUrl()));
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
            case 801:
                return new LoginResult(LoginState.WAITING_SCAN, object);
            case 802:
                return new LoginResult(LoginState.WAITING_CONFIRM, object);
            case 803:
                return new LoginResult(LoginState.SUCCEEDED, object);
            default:
                return new LoginResult(LoginState.EXPIRED, object);
        }
    }
}
