package cn.feng.aluminium.util.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class DataUtil {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
}
