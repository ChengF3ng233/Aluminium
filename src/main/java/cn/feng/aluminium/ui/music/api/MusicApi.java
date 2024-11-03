package cn.feng.aluminium.ui.music.api;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.config.ConfigManager;
import cn.feng.aluminium.ui.music.api.bean.*;
import cn.feng.aluminium.ui.music.api.bean.login.LoginResult;
import cn.feng.aluminium.ui.music.api.bean.login.LoginState;
import cn.feng.aluminium.ui.music.api.bean.login.QRCode;
import cn.feng.aluminium.ui.music.api.bean.lyric.Lyric;
import cn.feng.aluminium.ui.music.api.bean.lyric.LyricLine;
import cn.feng.aluminium.ui.music.gui.page.Pages;
import cn.feng.aluminium.util.Util;
import cn.feng.aluminium.util.data.DataUtil;
import cn.feng.aluminium.util.data.HttpUtil;
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
import java.util.*;

import static cn.feng.aluminium.util.data.HttpUtil.downloadImage;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicApi extends Util {
    private static final String host = "https://music.skidder.top";

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

    private static Music parseMusic(JsonObject musicObj, boolean formatted) {
        String name = musicObj.get("name").getAsString();
        long id = musicObj.get("id").getAsLong();
        if (Aluminium.INSTANCE.musicManager.getMusicMap().containsKey(id))
            return Aluminium.INSTANCE.musicManager.getMusicMap().get(id);

        StringBuilder artist = new StringBuilder();
        for (JsonElement e : musicObj.get(formatted ? "artists" : "ar").getAsJsonArray()) {
            JsonObject artistObj = e.getAsJsonObject();
            artist.append(artistObj.get("name").getAsString()).append(",");
        }
        if (artist.length() == 0) {
            artist.append("未知音乐家");
        } else artist.deleteCharAt(artist.length() - 1);

        int duration = musicObj.get(formatted ? "duration" : "dt").getAsInt();
        Album album = parseAlbum(musicObj.get(formatted ? "album" : "al").getAsJsonObject(), !formatted);
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

    public static List<Music> loadPlaylist(Playlist playList) throws IOException {
        JsonObject object = fetchObject("/playlist/track/all?id=" + playList.getId() + "&offset=" + playList.getMusicList().size());
        JsonArray songs = object.get("songs").getAsJsonArray();

        if (songs.size() == 0) {
            playList.setCompletelyDownloaded(true);
            return Collections.emptyList();
        }

        List<Music> musics = new ArrayList<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            musics.add(parseMusic(obj, false));
        }

        playList.getMusicList().addAll(musics);
        return musics;
    }

    public static Playlist getCloudMusic() {
        JsonArray data = fetchArray("/user/cloud", "data");
        List<Music> musics = new ArrayList<>();
        for (JsonElement e : data) {
            JsonObject songObj = e.getAsJsonObject().get("simpleSong").getAsJsonObject();
            musics.add(parseMusic(songObj, false));
        }
        return new Playlist(-1, "我的音乐云盘", "你的网易云云盘", Aluminium.INSTANCE.musicManager.getUser().getNickname(), musics.get(0).getAlbum().getCover(), musics);
    }

    public static List<Music> loadCloudMusic(Playlist playlist) {
        JsonArray songs = fetchArray("/user/cloud?offset=" + playlist.getMusicList().size(), "data");
        if (songs.size() == 0) {
            playlist.setCompletelyDownloaded(true);
            return Collections.emptyList();
        }
        List<Music> musics = new ArrayList<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject().get("simpleSong").getAsJsonObject();
            musics.add(parseMusic(obj, false));
        }
        playlist.getMusicList().addAll(musics);
        return musics;
    }

    public static BufferedImage downloadImage(String url) {
        return HttpUtil.downloadImage(wrapImage(url));
    }

    private static Album parseAlbum(JsonObject albumObj, boolean hasCover) {
        long id = albumObj.get("id").getAsLong();
        if (Aluminium.INSTANCE.musicManager.getAlbumMap().containsKey(id))
            return Aluminium.INSTANCE.musicManager.getAlbumMap().get(id);
        String name = albumObj.get("name").getAsString();
        String coverUrl = null;
        if (hasCover) {
            coverUrl = albumObj.get("picUrl").getAsString();
        }
        Album album = hasCover ? new Album(id, name, new Cover(coverUrl, downloadImage(coverUrl))) : new Album(id, name);
        Aluminium.INSTANCE.musicManager.getAlbumMap().put(id, album);
        return album;
    }

    public static Playlist getDailySongs() {
        JsonArray songs = fetchObjectArray("/recommend/songs", "data", "dailySongs");
        List<Music> musicList = new ArrayList<>();
        for (JsonElement e : songs) {
            JsonObject musicObj = e.getAsJsonObject();
            Music music = parseMusic(musicObj, false);
            musicList.add(music);
        }
        Music first = musicList.get(0);
        Playlist playlist = new Playlist(-1, "每日歌曲推荐", "根据你的音乐口味生成，每天6:00更新", "云音乐官方", first.getAlbum().getCover(), musicList);
        playlist.setCompletelyDownloaded(true);
        Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        return playlist;
    }

    public static List<Playlist> getRadars() {
        JsonArray array = fetchObjectArray("/homepage/block/page", "data", "blocks");
        List<Playlist> result = new ArrayList<>();
        for (JsonElement e : array) {
            if (!(e instanceof JsonObject)) continue;
            JsonObject obj = e.getAsJsonObject();
            if (!obj.has("blockCode") || !obj.get("blockCode").getAsString().equals("HOMEPAGE_BLOCK_MGC_PLAYLIST"))
                continue;

            for (JsonElement ee : obj.get("creatives").getAsJsonArray()) {
                JsonObject creativeObj = ee.getAsJsonObject();

                for (JsonElement eee : creativeObj.get("resources").getAsJsonArray()) {
                    JsonObject resourceObj = eee.getAsJsonObject();
                    if (!resourceObj.has("resourceType") || !resourceObj.get("resourceType").getAsString().equals("list"))
                        continue;

                    JsonObject uiElement = resourceObj.get("uiElement").getAsJsonObject();
                    long id = Long.parseLong(resourceObj.get("resourceId").getAsString());
                    String title = uiElement.get("mainTitle").getAsJsonObject().get("title").getAsString();
                    String imageUrl = uiElement.get("image").getAsJsonObject().get("imageUrl").getAsString();

                    result.add(new Playlist(id, title, new Cover(imageUrl, downloadImage(wrapImage(imageUrl)))));
                }


            }
            break;
        }
        for (Playlist playlist : result) {
            Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        }
        return result;
    }

    public static Playlist search(String keywords) {
        JsonArray songs = fetchObjectArray("/cloudsearch?keywords=" + keywords, "result", "songs");
        List<Music> musicList = new ArrayList<>();
        StringBuilder ids = new StringBuilder();

        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            Music music = parseMusic(obj, false);
            musicList.add(music);
            System.out.println(music.getArtist());
            ids.append(ids.length() == 0 ? music.getId() : "," + music.getId());
        }

        Map<Long, Cover> map = getSongCovers(ids.toString());
        for (Music music : musicList) {
            if (map.containsKey(music.getId())) {
                if (music.getAlbum().getCover() == null) {
                    music.getAlbum().setCover(map.get(music.getId()));
                }
            }
        }

        Playlist playlist = new Playlist(-1, "搜索结果：" + keywords, musicList.get(0).getAlbum().getCover());
        playlist.getMusicList().addAll(musicList);
        playlist.setCompletelyDownloaded(true);
        Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        return playlist;
    }

    public static Map<Long, Cover> getSongCovers(String ids) {
        JsonArray songs = fetchObject("/song/detail?ids=" + ids).get("songs").getAsJsonArray();
        Map<Long, Cover> result = new HashMap<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            String url = obj.get("al").getAsJsonObject().get("picUrl").getAsString();
            result.put(obj.get("id").getAsLong(), new Cover(url, downloadImage(url)));
        }
        return result;
    }

    public static List<Playlist> getPersonalized() {
        List<Playlist> result = new ArrayList<>();
        for (JsonElement e : fetchArray("/personalized", "result")) {
            JsonObject obj = e.getAsJsonObject();
            result.add(new Playlist(
                    obj.get("id").getAsLong(),
                    obj.get("name").getAsString(),
                    new Cover(
                            obj.get("picUrl").getAsString(),
                            downloadImage(wrapImage(obj.get("picUrl").getAsString()))
                    )
            ));
        }
        for (Playlist playlist : result) {
            Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        }
        return result;
    }

    public static List<Playlist> getUserPlaylists() {
        JsonObject obj = fetchObject("/user/playlist?uid=" + Aluminium.INSTANCE.musicManager.getUser().getId());
        List<Playlist> result = new ArrayList<>();
        for (JsonElement e : obj.get("playlist").getAsJsonArray()) {
            JsonObject list = e.getAsJsonObject();
            String coverImgUrl = list.get("coverImgUrl").getAsString();
            String name = list.get("name").getAsString();
            long id = list.get("id").getAsLong();
            String author = list.get("creator").getAsJsonObject().get("nickname").getAsString();
            result.add(new Playlist(id, name, null, author, new Cover(coverImgUrl, downloadImage(wrapImage(coverImgUrl)))));
            if (name.contains("喜欢的音乐")) {
                Aluminium.INSTANCE.musicManager.likeId = id;
            }
        }
        for (Playlist playlist : result) {
            Aluminium.INSTANCE.musicManager.getPlaylistMap().put(playlist.getId(), playlist);
        }
        Pages.likePage.load();
        return result;
    }

    public static Lyric getLyric(Music music) {
        JsonObject obj = fetchObject("/lyric/new?id=" + music.getId());
        boolean verbatim = obj.has("yrc") && !obj.get("yrc").getAsJsonObject().get("lyric").getAsString().isEmpty();

        Lyric lyric = new Lyric(music, verbatim);
        String lyricString = obj.get(verbatim? "yrc" : "lrc").getAsJsonObject().get("lyric").getAsString();
        String translateString = "";

        if (obj.has("ytlrc") && verbatim) {
            translateString = obj.get("ytlrc").getAsJsonObject().get("lyric").getAsString();
        } else if (obj.has("tlyric") && !verbatim) {
            translateString = obj.get("tlyric").getAsJsonObject().get("lyric").getAsString();
        }

        for (String line : lyricString.split("\n")) {
            if (line.isEmpty()) continue;

            // 创作者数据
            if (line.startsWith("{")) {
                lyric.getLyricLines().add(LyricLine.parseMetadata(line));
            } else {
                if (verbatim) {
                    // 逐字歌词
                    lyric.getLyricLines().add(LyricLine.parseYrc(line));
                } else {
                    // 普通歌词
                    lyric.getLyricLines().add(LyricLine.parseLrc(line));
                }
            }
        }

        if (!translateString.isEmpty()) {
            lyric.setTranslated(true);
        } else lyric.setTranslated(false);

        lyric.init();
        return lyric;
    }

    public static List<Playlist> getRecommended() throws IOException {
        JsonArray playlistArray = fetchArray("/recommend/resource", "recommend");

        List<Playlist> result = new ArrayList<>();

        for (JsonElement element : playlistArray) {
            JsonObject obj = element.getAsJsonObject();
            String description = (obj.get("description") instanceof JsonNull || obj.get("description") == null) ? "没有描述，你自己进去看看" : obj.get("description").getAsString();
            File file = new File(ConfigManager.coverDir, "playlist_" + obj.get("id").getAsLong() + ".jpg");

            HttpUtil.downloadImage(wrapImage(obj.get("picUrl").getAsString()), file, true);

            result.add(new Playlist(
                    obj.get("id").getAsLong(),
                    obj.get("name").getAsString(),
                    description,
                    "我懒得找谁整的这个歌单了",
                    new Cover(obj.get("picUrl").getAsString(),
                            ImageIO.read(file))
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
