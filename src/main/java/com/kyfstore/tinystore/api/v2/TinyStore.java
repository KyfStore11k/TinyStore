package com.kyfstore.tinystore.api.v2;

import java.util.*;

public class TinyStore extends com.kyfstore.tinystore.api.v1.TinyStore implements TinyStoreAdvanced {
    private final Map<String, Object> metadata = new HashMap<>();

    protected TinyStore(String path) {
        super(path);
    }

    public static TinyStore create(String path) {
        return new TinyStore(path);
    }

    public void put(String name, ValueType type, Object value) {
        try {
            com.kyfstore.tinystore.api.v1.ValueType baseType =
                    com.kyfstore.tinystore.api.v1.ValueType.valueOf(type.name());
            super.put(name, baseType, value);
        } catch (IllegalArgumentException e) {
            this.store.put(name, value);
        }
    }

    @Override
    public void putMeta(String key, Object value) {
        metadata.put(key, value);
    }

    @Override
    public Object getMeta(String key) {
        return metadata.get(key);
    }

    @Override
    public Map<String, Object> getAllMeta() {
        return Collections.unmodifiableMap(metadata);
    }
}
