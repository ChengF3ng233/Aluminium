package cn.feng.aluminium.ui.music.thread;

import cn.feng.aluminium.ui.music.api.MusicApi;
import cn.feng.aluminium.ui.music.api.bean.Cover;

public class FetchCoverThread extends Thread {
    private final Cover cover;
    public FetchCoverThread(Cover cover) {
        this.cover = cover;
    }

    @Override
    public void run() {
        if (cover.getCoverImage() != null) return;
        cover.setCoverImage(MusicApi.downloadImage(cover.getCoverUrl()));
    }
}
