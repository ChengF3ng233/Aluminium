package cn.feng.aluminium.util.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class IOUtil {
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    public static ByteBuffer toByteBuffer(InputStream inputStream) throws IOException {
        byte[] byteArray = toByteArray(inputStream);

        // 将字节数组包装为 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.flip();  // 翻转缓冲区，准备读取

        return byteBuffer;
    }
}
