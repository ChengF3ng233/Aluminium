package cn.feng.aluminium.ui.music.api.player;

import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.thread.FetchMusicURLThread;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class MusicPlayer {
    private Music currentMusic;
    private List<Music> currentMusicList;
    private MediaPlayer player;

    public MusicPlayer() {
        new JFXPanel();
    }

    public boolean available() {
        return player != null && currentMusic != null;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public MediaPlayer.Status getStatus() {
        return player.getStatus();
    }

    public void setCurrentMusicList(List<Music> currentMusicList) {
        this.currentMusicList = currentMusicList;
    }

    public List<Music> getCurrentMusicList() {
        return currentMusicList;
    }

    public void play(String url) {
        double volume = -1d;
        if (player != null) {
            volume = player.getVolume();
            player.stop();
        }

        Media media = new Media(url);
        player = new MediaPlayer(media);
        if (volume != -1d) player.setVolume(volume);
        player.play();
    }

    public void play() {
        if (player == null) return;
        player.play();
    }

    public void pause() {
        if (player == null) return;
        player.pause();
    }

    public void stop() {
        if (player == null) return;
        player.stop();
    }

    public void setVolume(double volume) {
        if (player == null) return;
        player.setVolume(volume);
    }

    public double getVolume() {
        return player.getVolume();
    }

    public void previous() {
        int newIndex = currentMusicList.indexOf(currentMusic) - 1;
        if (newIndex < 0) newIndex = currentMusicList.size() - 1;
        setCurrentMusic(currentMusicList.get(newIndex));
    }

    public void next() {
        int newIndex = currentMusicList.indexOf(currentMusic) + 1;
        if (newIndex > currentMusicList.size() - 1) newIndex = 0;
        setCurrentMusic(currentMusicList.get(newIndex));
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
        new FetchMusicURLThread(currentMusic.getId()).start();
    }

    public float getCurrentTime() {
        return Double.valueOf(player.getCurrentTime().toMillis()).floatValue();
    }

    public void seek(double newTime) {
        player.seek(Duration.millis(newTime));
    }
}
