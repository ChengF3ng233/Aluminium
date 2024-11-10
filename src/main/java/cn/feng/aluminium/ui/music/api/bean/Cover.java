package cn.feng.aluminium.ui.music.api.bean;

import cn.feng.aluminium.ui.music.thread.FetchCoverThread;

import java.awt.image.BufferedImage;

public class Cover {
    private String coverUrl;
    private BufferedImage coverImage;
    private Thread thread;

    public Cover(String coverUrl, BufferedImage coverImage) {
        this.coverUrl = coverUrl;
        this.coverImage = coverImage;
    }

    public Cover(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void load() {
        if (coverImage != null) return;
        if (thread == null) {
            thread = new FetchCoverThread(this);
            thread.start();
        } else if (!thread.isAlive()) thread = null;
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
