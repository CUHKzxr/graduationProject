package com.example.demo.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    @Test
    void checkTimestamp() {
        for (int i = 0; i < 6; i++) {
            assertTrue(TimeUtils.checkTimestamp("2022-05-02T20:25:20Z",i));
            assertFalse(TimeUtils.checkTimestamp("--",i));
        }
        assertFalse(TimeUtils.checkTimestamp("----",0));
    }

    @Test
    void getTimestampValue() {
        //TODO
    }

    @Test
    void changeTimestamp2() {
        //TODO
    }

    @Test
    void parseDate() {
        //TODO
    }
}