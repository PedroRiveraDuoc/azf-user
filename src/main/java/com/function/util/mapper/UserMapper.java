package com.function.util.mapper;

import com.google.gson.Gson;
import com.function.domain.model.User;

public class UserMapper {
    private static final Gson gson = new Gson();

    public static String toJson(User user) {
        return gson.toJson(user);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static User fromJson(String json) {
        return gson.fromJson(json, User.class);
    }
}