package com.kyfstore.tinystore.api.v2;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class TinyStoreV2Test {

    private static final String TEST_FILE = "test_v2.store";
    TinyStore store;

    @BeforeEach
    void setup() {
        store = TinyStore.create(TEST_FILE);
        store.maxWriteLength = 2000;
        store.type = com.kyfstore.tinystore.api.v1.StoreType.JSON;
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    void testPutGetWithNewTypes() {
        store.put("list", ValueType.LIST, java.util.List.of("a", "b", "c"));
        var list = store.get("list", java.util.List.class, null);
        assertNotNull(list);
        assertEquals(3, list.size());
    }

    @Test
    void testMetadata() {
        store.putMeta("author", "kyf");
        store.putMeta("version", 2);

        assertEquals("kyf", store.getMeta("author"));
        assertEquals(2, store.getMeta("version"));
        assertTrue(store.getAllMeta().containsKey("author"));
    }

    @Test
    void testCompatibilityWithV1() {
        store.put("number", ValueType.INTEGER, 42);
        int number = store.get("number", Integer.class, 0);
        assertEquals(42, number);
    }

    @Test
    void testSaveAndReloadPreservesValues() {
        store.put("theme", ValueType.STRING, "dark");
        store.save();

        TinyStore reloaded = TinyStore.create(TEST_FILE);
        reloaded.load();

        String theme = reloaded.get("theme", String.class, null);
        assertEquals("dark", theme);
    }
}
