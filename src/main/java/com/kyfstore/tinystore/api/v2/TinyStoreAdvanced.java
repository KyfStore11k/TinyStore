package com.kyfstore.tinystore.api.v2;

import java.util.Map;

/**
 * Interface defining metadata support for TinyStore API v2.
 */
public interface TinyStoreAdvanced {

    /**
     * Adds or updates a metadata entry.
     * @param key metadata key
     * @param value metadata value
     */
    void putMeta(String key, Object value);

    /**
     * Retrieves metadata by key.
     * @param key metadata key
     * @return metadata value or null if missing
     */
    Object getMeta(String key);

    /**
     * Retrieves an unmodifiable map of all metadata entries.
     * @return map of all metadata key-value pairs
     */
    Map<String, Object> getAllMeta();
}
