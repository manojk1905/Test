package com.txt.filereader.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class UtilityTest {

    @Test
    public void countWordFreqInTextLineTest() {
        List<String> list = Arrays.asList(new String[]{"test1", "test2", "test3", "test1", "test2", "test1"});
        assertEquals(Utility.countWordFreqInTextLine(list, "test1"), 3);
        assertEquals(Utility.countWordFreqInTextLine(list, "test2"), 2);
        assertEquals(Utility.countWordFreqInTextLine(list, "test3"), 1);
        assertEquals(Utility.countWordFreqInTextLine(list, "test4"), 0);
    }

    @Test
    public void topNFreqOfWordInMapTest() {
        assertEquals(Utility.topNFreqOfWordInMap(getMapData(), 3).size(), 3);
        HashMap<String, Integer> dataMap = Utility.topNFreqOfWordInMap(getMapData(), 3);
        assertTrue(dataMap.get("test2") == 2);
    }

    @Test
    public void totalWordCountTest() {
        assertEquals(Utility.totalWordCount(getMapData()), 7);
    }

    private ConcurrentHashMap<String, Integer> getMapData() {
        ConcurrentHashMap<String, Integer> wordFreqCountMap = new ConcurrentHashMap<>();
        wordFreqCountMap.put("test1", 3);
        wordFreqCountMap.put("test2", 2);
        wordFreqCountMap.put("test3", 1);
        wordFreqCountMap.put("test4", 1);
        return wordFreqCountMap;
    }
}
