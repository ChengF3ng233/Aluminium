package cn.feng.aluminium.util.data;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class HttpUtil extends Util {
    public static BufferedImage downloadImage(String imageUrl) {
        return downloadImage(imageUrl, 0);
    }

    public static void downloadImage(String imageURL, File file, boolean rewrite) throws IOException {
        if (file.exists() && !rewrite) return;

        URL url = new URL(imageURL);

        try (InputStream inputStream = url.openStream()) {
            // 将图像下载到临时文件
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Downloaded image: {} to {}", url, file.getName());
        } catch (IOException e) {
            logger.error("IO Exception: {}", e.getMessage(), e);
        }
    }

    private static BufferedImage downloadImage(String imageUrl, int count) {
        try {
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                return image == null ? ImageIO.read(new URL(Aluminium.INSTANCE.musicManager.getUser().getAvatarUrl())) : image;
            }
        } catch (IOException e) {
            if (count >= 4) {
                throw new NullPointerException("试了五遍都不行，哥们你网是不是断了");
            }
            logger.error("Failed to download image: {}, retry...", imageUrl);
            return downloadImage(imageUrl, count + 1);
        }
    }
}
