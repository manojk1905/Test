package com.txt.filereader;

import static com.txt.filereader.constants.Constants.RESULT_COUNT;
import static com.txt.filereader.constants.Constants.READER_TH_COUNT;
import static com.txt.filereader.constants.Constants.PROCESSOR_TH_COUNT;
import static com.txt.filereader.constants.Constants.QUEUE_SIZE;
import com.txt.filereader.processor.TestProcessor;
import com.txt.filereader.reader.TextReader;
import com.txt.filereader.utils.Utility;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileReaderApplication {

    private ExecutorService executorService;

    public static void main(String[] args) {
        FileReaderApplication fileReaderApplication = new FileReaderApplication();
        fileReaderApplication.initializeResourceHandler();
    }

    private void initializeResourceHandler() {
        try {
            executorService = Executors.newFixedThreadPool(READER_TH_COUNT + PROCESSOR_TH_COUNT);
            BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
            AtomicBoolean isCompleted = new AtomicBoolean(false);

            new TextReader().readTextData(blockingQueue, isCompleted, executorService);
            ConcurrentHashMap<String, Integer> wordFreqCountMap = new ConcurrentHashMap<>();
            Map<String, Integer> finalData = new TestProcessor().processFile(blockingQueue, wordFreqCountMap, isCompleted, executorService);
            printFinalResult(finalData);
        } catch (IOException | InterruptedException exception) {
            System.out.println("Error while Reading/Processing file data :: " + exception);
        } finally {
            executorService.shutdown();
        }
    }

    public void printFinalResult(Map<String, Integer> finalData) {
        Map<String, Integer> result = Utility.topNFreqOfWordInMap(finalData, RESULT_COUNT);
        System.out.println("Top " + RESULT_COUNT + " word and their frequency.");
        result.forEach((word, count) -> System.out.println((word + " :: " + count)));
        System.out.println("Total word count -----" + Utility.totalWordCount(finalData));
    }
}
