package com.kyfstore.tinystore.api.v1;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TinyStoreV1Test {

    private static final String TEST_FILE = "test_v1.store";
    TinyStore store;

    @BeforeEach
    void setup() {
        store = TinyStore.create(TEST_FILE);
        store.maxWriteLength = 1000;
        store.type = StoreType.JSON;
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    void testBasicPutGet() {
        store.put("username", ValueType.STRING, "admin");
        String value = store.get("username", String.class, null);
        assertEquals("admin", value);
    }

    @Test
    void testSaveLoad() {
        store.put("timeout", ValueType.INTEGER, 30);
        store.save();

        TinyStore newStore = TinyStore.create(TEST_FILE);
        newStore.load();

        Number timeoutNum = newStore.get("timeout", Number.class, 0);
        int timeout = timeoutNum.intValue();
        assertEquals(30, timeout);
    }

    @Test
    void testRemove() {
        store.put("key", ValueType.STRING, "value");
        store.remove("key");
        assertNull(store.get("key", String.class, null));
    }

    @Test
    void testWriteLimitExceeded() {
        store.maxWriteLength = 5;
        store.put("data", ValueType.STRING, "this will fail");
        assertThrows(RuntimeException.class, store::save);
    }
}
