package cn.feng.aluminium.ui.music.api.bean;

import net.minecraft.client.renderer.texture.DynamicTexture;

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
    private transient DynamicTexture avatarTexture;

    public User(long id, String nickname, String avatarUrl, String signature, String cookie, DynamicTexture avatarTexture) {
        this.id = id;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.signature = signature;
        this.cookie = cookie;
        this.avatarTexture = avatarTexture;
    }

    public User() {

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

    public DynamicTexture getAvatarTexture() {
        return avatarTexture;
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

    public void setAvatarTexture(DynamicTexture avatarTexture) {
        this.avatarTexture = avatarTexture;
    }
}
