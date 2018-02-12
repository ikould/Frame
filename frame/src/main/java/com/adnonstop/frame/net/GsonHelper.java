package com.adnonstop.frame.net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * GSON解析帮助类
 * <p>
 * Created by ALiu on 2017/10/24.
 */

public class GsonHelper {

    private static GsonHelper instance;

    public static GsonHelper getInstance() {
        if (instance == null) {
            synchronized (GsonHelper.class) {
                if (instance == null)
                    instance = new GsonHelper();
            }
        }
        return instance;
    }

    private Gson gson;

    private GsonHelper() {
        gson = new Gson();
    }

    /**
     * 解析
     *
     * @param json     JSON字符串
     * @param classOfT 待转换的class类型
     * @param <T>      泛型
     * @return 对应class对象
     */
    public <T> T parse(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
