package cn.feng.aluminium.util.data;

import cn.feng.aluminium.util.Util;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class ResourceUtil extends Util {
    public static ResourceLocation getResource(String fileName, ResourceType type) {
        String folder = "";
        switch (type) {
            case FONT:
                folder = "font/";
                break;
            case ICON:
                folder = "icon/";
                break;
            case IMAGE:
                folder = "image/";
                break;
            case VIDEO:
                folder = "video/";
                break;
        }
        return new ResourceLocation("aluminium/" + folder + fileName);
    }

    public static InputStream getResourceAsStream(String fileName, ResourceType type) {
        try {
            String folder = "";
            switch (type) {
                case FONT:
                    folder = "font/";
                    break;
                case ICON:
                    folder = "icon/";
                    break;
                case IMAGE:
                    folder = "image/";
                    break;
                case VIDEO:
                    folder = "video/";
                    break;
            }
            String location = "/assets/minecraft/aluminium/" + folder + fileName;
            return ResourceUtil.class.getResourceAsStream(location);
        } catch (Exception e) {
            return null;
        }
    }

}
