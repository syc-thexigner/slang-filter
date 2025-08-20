package org.dolgo.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlangFilterTest {

    private SlangFilter filter;

    @BeforeEach
    void setUp() {
        SlangDictionary dictionary = new SlangDictionary();
        filter = new SlangFilter(dictionary);
    }

    @Test
    void testDetectSlang() {
        assertTrue(filter.contains("씨발")); // 한국어
        assertTrue(filter.contains("fuck")); // 영어
        assertTrue(filter.contains("puta")); // 스페인어
    }
}
