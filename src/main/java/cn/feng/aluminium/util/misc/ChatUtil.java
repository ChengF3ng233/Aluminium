package cn.feng.aluminium.util.misc;

import cn.feng.aluminium.util.Util;
import net.minecraft.util.ChatComponentText;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ChatUtil extends Util {
    public static void sendMessage(String message) {
        if (message == null) return;
        mc.thePlayer.addChatMessage(new ChatComponentText("§f[§3Aluminium§f] " + message));
    }
}
