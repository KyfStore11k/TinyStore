package com.kyfstore.tinystore.api.v2;

import java.util.*;

/**
 * TinyStore v2 extends v1 TinyStore with metadata and extended value types.
 */
public class TinyStore extends com.kyfstore.tinystore.api.v1.TinyStore implements TinyStoreAdvanced {

    /**
     * Metadata map for additional information storage.
     */
    private final Map<String, Object> metadata = new HashMap<>();

    /**
     * Protected constructor.
     * @param path file path for persistence
     */
    protected TinyStore(String path) {
        super(path);
    }

    /**
     * Factory method for creating a v2 TinyStore.
     * @param path file path for persistence
     * @return new TinyStore instance (v2)
     */
    public static TinyStore create(String path) {
        return new TinyStore(path);
    }

    /**
     * Puts a key-value pair with extended value types.
     * Falls back to direct store put if the type doesn't exist in v1.
     * @param name key name
     * @param type extended value type
     * @param value value to store
     */
    public void put(String name, ValueType type, Object value) {
        try {
            com.kyfstore.tinystore.api.v1.ValueType baseType =
                    com.kyfstore.tinystore.api.v1.ValueType.valueOf(type.name());
            super.put(name, baseType, value);
        } catch (IllegalArgumentException e) {
            this.store.put(name, value);
        }
    }

    /**
     * Adds or updates metadata.
     * @param key metadata key
     * @param value metadata value
     */
    @Override
    public void putMeta(String key, Object value) {
        metadata.put(key, value);
    }

    /**
     * Retrieves metadata by key.
     * @param key metadata key
     * @return metadata value or null if not found
     */
    @Override
    public Object getMeta(String key) {
        return metadata.get(key);
    }

    /**
     * Returns an unmodifiable map of all metadata.
     * @return all metadata entries
     */
    @Override
    public Map<String, Object> getAllMeta() {
        return Collections.unmodifiableMap(metadata);
    }
}
