package com.gary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper<T> {
    private final Gson gson = (new GsonBuilder()).disableHtmlEscaping()
            .serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    public GsonHelper() {
    }

    public T convertJsonToObject(String message, Class<T> object) {
        return this.gson.fromJson(message, object);
    }

    public String convertObjectToJson(T object) {
        return this.gson.toJson(object);
    }
}
