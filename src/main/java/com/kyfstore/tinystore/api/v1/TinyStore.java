package com.kyfstore.tinystore.api.v1;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class TinyStore {
    public int maxWriteLength = Integer.MAX_VALUE;
    public StoreType type = StoreType.JSON;

    protected final Map<String, Object> store = new HashMap<>();
    protected final String filePath;

    protected TinyStore(String path) {
        this.filePath = path;
    }

    public static TinyStore create(String path) {
        return new TinyStore(path);
    }

    public void put(String name, ValueType type, Object value) {
        store.put(name, value);
    }

    public <T> T get(String name, Class<T> expector, T defaultValue) {
        Object val = store.get(name);
        if (val == null) return defaultValue;
        if (expector.isInstance(val)) return expector.cast(val);
        throw new ClassCastException("Expected type " + expector.getSimpleName());
    }

    public void remove(String name) {
        store.remove(name);
    }

    public void save() {
        try {
            String content = serialize();
            if (content.length() > maxWriteLength) {
                throw new RuntimeException("Max write length exceeded");
            }
            Files.writeString(Path.of(filePath), content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save", e);
        }
    }

    public void load() {
        try {
            String content = Files.readString(Path.of(filePath));
            deserialize(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load", e);
        }
    }

    protected String serialize() {
        return new com.google.gson.Gson().toJson(store);
    }

    protected void deserialize(String content) {
        Map<String, Object> map = new com.google.gson.Gson().fromJson(content, Map.class);
        store.clear();
        store.putAll(map);
    }
}
