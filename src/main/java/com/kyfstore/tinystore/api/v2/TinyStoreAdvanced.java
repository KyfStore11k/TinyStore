package com.kyfstore.tinystore.api.v2;

import java.util.*;

public interface TinyStoreAdvanced {
    void putMeta(String key, Object value);
    Object getMeta(String key);
    Map<String, Object> getAllMeta();
}
