package cn.feng.aluminium.ui.music.gui.page.impl;

import cn.feng.aluminium.ui.music.thread.FetchPlaylistMusicThread;
import cn.feng.aluminium.ui.music.thread.PlaylistType;

public class CloudPage extends PlaylistPage {
    @Override
    protected void fetchMusic() {
        if (thread == null && !playlist.isCompletelyDownloaded()) {
            thread = new FetchPlaylistMusicThread(this, playlist, PlaylistType.CLOUD);
            thread.start();
        }
    }
}
