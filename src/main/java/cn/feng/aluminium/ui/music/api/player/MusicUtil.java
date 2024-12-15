package cn.feng.aluminium.ui.music.api.player;

import org.jflac.sound.spi.FlacAudioFileReader;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MusicUtil {
    public static void convert(String url, File file) {
        try {
            // 从 URL 下载 FLAC 文件
            AudioInputStream flacInputStream = new FlacAudioFileReader().getAudioInputStream(new URL(url));

            // 创建 WAV 格式
            AudioFormat wavFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    flacInputStream.getFormat().getSampleRate(),
                    16, // 16 位样本
                    flacInputStream.getFormat().getChannels(),
                    flacInputStream.getFormat().getChannels() * 2,
                    flacInputStream.getFormat().getSampleRate(),
                    false // 小端序
            );

            // 创建输出 WAV 文件
            AudioInputStream wavOutputStream = AudioSystem.getAudioInputStream(wavFormat, flacInputStream);
            AudioSystem.write(wavOutputStream, AudioFileFormat.Type.WAVE, file);

            // 关闭流
            wavOutputStream.close();
            flacInputStream.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
