package cn.feng.aluminium.ui.music.api.bean;

import java.awt.image.BufferedImage;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class User {
    // Metadata
    private long id;
    private String cookie = "";

    // Display information
    private String nickname;
    private String avatarUrl;
    private String signature;
    private int level;
    private int listenedSongs;
    private long createTime;

    private transient BufferedImage avatarImage;

    public void logout() {
        id = 0;
        cookie = "";
        nickname = null;
        avatarUrl = null;
        signature = null;
        avatarImage = null;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getListenedSongs() {
        return listenedSongs;
    }

    public void setListenedSongs(int listenedSongs) {
        this.listenedSongs = listenedSongs;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getSignature() {
        return signature;
    }

    public String getCookie() {
        return cookie;
    }

    public BufferedImage getAvatarImage() {
        return avatarImage;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setAvatarImage(BufferedImage avatarImage) {
        this.avatarImage = avatarImage;
    }
}
