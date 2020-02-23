package com.txt.filereader.processor;

import static com.txt.filereader.constants.Constants.BLANK_STRING;
import com.txt.filereader.utils.Utility;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class TextProcessorTask implements Callable<Map<String, Integer>> {
    private BlockingQueue<String> blockingQueue;
    private AtomicBoolean isCompleted;
    private String wordStringRegex;

    public TextProcessorTask(BlockingQueue<String> blockingQueue, AtomicBoolean isCompleted, String wordStringRegex) {
        this.blockingQueue = blockingQueue;
        this.isCompleted = isCompleted;
        this.wordStringRegex = wordStringRegex;
    }

    public Map<String, Integer> call() {
        Map<String, Integer> wordFreqCountMap = new HashMap<>();
        try {
            while (true) {
                if (isCompleted.get() && blockingQueue.isEmpty()) {
                    break;
                }
                String textLine = blockingQueue.take();
                List<String> wordList = Arrays.asList(textLine.split(BLANK_STRING)).stream().map(str -> {
                    return str.replaceAll(wordStringRegex, "");
                }).collect(Collectors.toList());

                Set<String> wordSet = new HashSet<>(wordList);
                wordSet.forEach(word -> {
                    if (!StringUtils.isEmpty(word)) {
                        int freqCount = Utility.countWordFreqInTextLine(wordList, word);
                        wordFreqCountMap.put(word, wordFreqCountMap.getOrDefault(word, 0) + freqCount);
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return wordFreqCountMap;
    }
}
