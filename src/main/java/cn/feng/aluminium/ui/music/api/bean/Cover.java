package cn.feng.aluminium.ui.music.api.bean;

import java.awt.image.BufferedImage;

public class Cover {
    private String coverUrl;
    private BufferedImage coverImage;

    public Cover(String coverUrl, BufferedImage coverImage) {
        this.coverUrl = coverUrl;
        this.coverImage = coverImage;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setCoverImage(BufferedImage coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public BufferedImage getCoverImage() {
        return coverImage;
    }
}
