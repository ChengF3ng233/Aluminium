package cn.feng.aluminium.ui.music.api.player;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.config.ConfigManager;
import cn.feng.aluminium.event.events.EventChangeMusic;
import cn.feng.aluminium.event.events.EventPlayerStatus;
import cn.feng.aluminium.event.events.EventResetLyric;
import cn.feng.aluminium.ui.music.Theme;
import cn.feng.aluminium.ui.music.api.bean.Music;
import cn.feng.aluminium.ui.music.api.bean.Playlist;
import cn.feng.aluminium.ui.music.thread.FetchMusicURLThread;
import cn.feng.aluminium.util.render.RenderUtil;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class MusicPlayer {
    private Music music;
    private Playlist playlist;
    private List<Music> musicList;
    private MediaPlayer mediaPlayer;
    private float[] magnitudes;

    public MusicPlayer() {
        new JFXPanel();
    }

    public boolean available() {
        return mediaPlayer != null && music != null;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
        Aluminium.INSTANCE.eventManager.call(new EventChangeMusic(music));
        new FetchMusicURLThread(music).start();
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public void play(String songURL) {
        Media media;

        if (songURL.endsWith("flac")) {
            File converted = new File(ConfigManager.musicDir, music.getId() + ".wav");
            if (!converted.exists()) {
                MusicUtil.convert(songURL, converted);
            }
            media = new Media(converted.toURI().toString());
        } else {
            media = new Media(songURL);
        }

        double volume = mediaPlayer == null ? 1d : mediaPlayer.getVolume();
        if (mediaPlayer != null) mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            this.magnitudes = magnitudes;
        });
        mediaPlayer.setOnEndOfMedia(this::next);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setOnPlaying(() -> {
            Aluminium.INSTANCE.eventManager.call(new EventPlayerStatus(MediaPlayer.Status.PLAYING));
        });
        mediaPlayer.setOnPaused(() -> {
            Aluminium.INSTANCE.eventManager.call(new EventPlayerStatus(MediaPlayer.Status.PAUSED));
        });
        mediaPlayer.setOnStopped(() -> {
            Aluminium.INSTANCE.eventManager.call(new EventPlayerStatus(MediaPlayer.Status.STOPPED));
        });
        mediaPlayer.play();
    }

    public float[] getMagnitudes() {
        return magnitudes;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void play() {
        if (available()) {
            mediaPlayer.play();
        }
    }

    public void pause() {
        if (available()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (available()) {
            mediaPlayer.stop();
        }
    }

    public float getVolume() {
        return (float) mediaPlayer.getVolume();
    }

    public void setVolume(double volume) {
        if (available()) {
            mediaPlayer.setVolume(volume);
        }
    }

    public void previous() {
        int newIndex = musicList.indexOf(music) - 1;
        if (newIndex < 0) newIndex = musicList.size() - 1;
        setMusic(musicList.get(newIndex));
    }

    public void next() {
        int newIndex = musicList.indexOf(music) + 1;
        if (newIndex > musicList.size() - 1) newIndex = 0;
        setMusic(musicList.get(newIndex));
    }

    public float getCurrentTime() {
        return (float) mediaPlayer.getCurrentTime().toMillis();
    }

    public float getCurrentPercent() {
        return (float) (mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getStopTime().toMillis());
    }

    public void seek(float newTime) {
        mediaPlayer.seek(Duration.millis(newTime));
        Aluminium.INSTANCE.eventManager.call(new EventResetLyric(music, newTime));
    }
}
