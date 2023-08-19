package com.example.common.tools;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 使用 Gson 进行 JSON 数据的序列化、反序列化
 * @author WuQinglong
 */
public class GsonTool {

    /**
     * 实例化
     */
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
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
     * @param json  Json数据
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * 读取 Json 数据转为对象
     * @param json      Json数据
     * @param typeToken 类型
     * @param <T>       外部类
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }

    /**
     * LocalDateTime 序列化反序列化桥接类
     */
    public static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        //序列化
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        //反序列化
        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            return LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    /**
     * LocalDateTime 序列化反序列化桥接类
     */
    public static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        //序列化
        @Override
        public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_DATE));
        }

        //反序列化
        @Override
        public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            return LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_DATE);
        }
    }

}
