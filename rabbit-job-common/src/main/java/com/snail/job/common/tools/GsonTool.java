package com.snail.job.common.tools;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 使用 Gson 进行 JSON 数据的序列化、反序列化
 * @author 吴庆龙
 * @date 2020/5/22 3:11 下午
 */
public class GsonTool {

    /**
     * 实例化
     */
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * 将对象转为 String 类型的 Json
     * @param value 待转换的对象
     * @return JSON数据
     */
    public static String toJson(Object value) {
        return GSON.toJson(value);
    }

    /**
     * 读取 Json 数据转为对象
     * @param json Json数据
     * @param clazz 对象类型
     * @param <T> 对象泛型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * 读取 Json 数据转为对象
     * @param json Json数据
     * @param typeToken 类型
     * @param <T> 外部类
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }
}
