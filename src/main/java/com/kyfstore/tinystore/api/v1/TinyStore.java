package com.kyfstore.tinystore.api.v1;

import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * TinyStore provides a simple key-value storage backed by a file.
 * Supports configurable maximum write length and storage type.
 * Uses JSON serialization by default.
 */
public class TinyStore {

    /**
     * Maximum number of characters allowed when writing the store to disk.
     * Exceeding this throws a RuntimeException.
     */
    public int maxWriteLength = Integer.MAX_VALUE;

    /**
     * Storage type/format of the file (e.g., JSON or BINARY).
     */
    public StoreType type = StoreType.JSON;

    /**
     * Internal map holding key-value pairs.
     */
    protected final Map<String, Object> store = new HashMap<>();

    /**
     * File path where data will be saved and loaded.
     */
    protected final String filePath;

    /**
     * Creates a new TinyStore instance bound to the given file path.
     * @param path the file path where the store will persist data
     */
    protected TinyStore(String path) {
        this.filePath = path;
    }

    /**
     * Factory method to create a TinyStore instance.
     * @param path the file path to use for persistence
     * @return a new TinyStore instance
     */
    public static TinyStore create(String path) {
        return new TinyStore(path);
    }

    /**
     * Adds or updates a key-value pair in the store.
     * @param name the key name
     * @param type the type of the value
     * @param value the value to store
     */
    public void put(String name, ValueType type, Object value) {
        store.put(name, value);
    }

    /**
     * Retrieves a value by key, attempting to cast to the expected type.
     * Returns the default value if the key is missing.
     * @param <T> the expected return type
     * @param name the key name
     * @param expector the expected class of the value
     * @param defaultValue the default value to return if key not found
     * @return the value cast to expected type or defaultValue if missing
     * @throws ClassCastException if the stored value cannot be cast to expector
     */
    public <T> T get(String name, Class<T> expector, T defaultValue) {
        Object val = store.get(name);
        if (val == null) return defaultValue;
        if (expector.isInstance(val)) return expector.cast(val);
        throw new ClassCastException("Expected type " + expector.getSimpleName());
    }

    /**
     * Removes a key-value pair from the store.
     * @param name the key name to remove
     */
    public void remove(String name) {
        store.remove(name);
    }

    /**
     * Saves the current store data to the bound file path.
     * @throws RuntimeException if write length exceeds maxWriteLength or on IO failure
     */
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

    /**
     * Loads store data from the bound file path.
     * @throws RuntimeException on IO failure or parsing error
     */
    public void load() {
        try {
            String content = Files.readString(Path.of(filePath));
            deserialize(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load", e);
        }
    }

    /**
     * Serializes the store map into a JSON string.
     * Can be overridden by subclasses for custom formats.
     * @return JSON string representation of the store data
     */
    protected String serialize() {
        return new com.google.gson.Gson().toJson(store);
    }

    /**
     * Deserializes JSON string into the store map.
     * Can be overridden by subclasses for custom formats.
     * @param content JSON string to parse
     */
    protected void deserialize(String content) {
        Map<String, Object> map = new com.google.gson.Gson().fromJson(content, Map.class);
        store.clear();
        store.putAll(map);
    }
}
