package com.uniwa.course_recommendation.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtils {
    private static final Gson gson = new Gson();

    private JsonUtils() {
        // implicit private constructor to hide its creation, as it provides static utils only
    }
    //For individual objects
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
    //For lists
    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json,type);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
