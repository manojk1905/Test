package com.txt.filereader.utils;

import com.sun.tools.javac.util.MatchingUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Utility {
    public static int countWordFreqInTextLine(List<String> textLine, String word) {
       if(textLine.contains(word)) {
           return Collections.frequency(textLine, word);
       }
       return 0;
    }

    public static HashMap<String, Integer> topNFreqOfWordInMap(Map<String, Integer> wordFreqCountMap, int num) {
        List<Map.Entry<String, Integer> > wordFreqCountLLEntrySet = new LinkedList<>(wordFreqCountMap.entrySet());

        Collections.sort(wordFreqCountLLEntrySet, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> firstObj,
                               Map.Entry<String, Integer> secondObj) {
                return (secondObj.getValue()).compareTo(firstObj.getValue());
            }
        });
        List<Map.Entry<String, Integer> > topNWordFreqCountLLEntrySet = wordFreqCountLLEntrySet.stream().limit(num).collect(Collectors.toList());

        HashMap<String, Integer> topNWordFreqCountMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : topNWordFreqCountLLEntrySet) {
            topNWordFreqCountMap.put(aa.getKey(), aa.getValue());
        }
        return  topNWordFreqCountMap;
    }

    public static int totalWordCount(Map<String, Integer> wordFreqCountMap) {
        Collection<Integer> wordCounts = wordFreqCountMap.values();
        return wordCounts.stream().mapToInt(Integer::intValue).sum();
    }
}
