package cn.feng.aluminium.util.render;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class ImageUtil {
    public static BufferedImage svgToBufferedImage(InputStream stream, float width, float height) {
        try {
            // 创建一个PNGTranscoder（可以将SVG转换为PNG）
            PNGTranscoder transcoder = new PNGTranscoder();

            // 设置转换的宽度和高度
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);

            // 读取SVG文件
            TranscoderInput input = new TranscoderInput(stream);

            // 准备输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);

            // 执行转换
            transcoder.transcode(input, output);
            outputStream.flush();

            // 将PNG数据转换为BufferedImage
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            BufferedImage image = ImageIO.read(byteArrayInputStream);

            // 关闭流
            outputStream.close();

            return image;
        } catch (TranscoderException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
