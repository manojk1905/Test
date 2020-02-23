package com.txt.filereader.processor;

import com.txt.filereader.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.txt.filereader.constants.Constants.PROCESSOR_TH_COUNT;
import static com.txt.filereader.constants.Constants.WORD_STR_REGEX;

public class TestProcessor {
    /*
    processor will fetch the record from blocking queue and process it in word frequency count.
    Here we are using 2 partition, mean 2 thread are reading the blocking queue in parallel.
    Than finally combining the each partition into a sorted final map.
    we can increase the PROCESSOR_TH_COUNT as per the need.

    If the file size getting increase to 1 TB it is not better to use the data in memory, in that scenario the
    best approach will in each partition thread will update the word freq count into database, better to use no-sql db.
     */
    public Map<String, Integer> processFile(BlockingQueue<String> blockingQueue, ConcurrentHashMap<String, Integer> wordFreqCountMap, AtomicBoolean isCompleted, ExecutorService executorService) throws InterruptedException {
        List<Future<Map<String, Integer>>> completableFuture = new ArrayList<>();

        completableFuture = executorService.invokeAll(getProcessorTask(blockingQueue, isCompleted));
        AtomicInteger partitionCounter = new AtomicInteger(1);
        Map<String, Integer> finalData = new HashMap<>();
        completableFuture.forEach(future -> {
            try {
                processEachPartitionAndGetAggregateResult(finalData, future, partitionCounter);

            } catch (InterruptedException | ExecutionException exception) {
                System.out.println("Error while processing data :: " + exception);
            }
        });
        return finalData;
    }

    private void processEachPartitionAndGetAggregateResult( Map<String, Integer> finalData, Future<Map<String, Integer>> future, AtomicInteger partitionCounter) throws ExecutionException, InterruptedException {
        int partitionCount = partitionCounter.getAndIncrement();
        Map<String, Integer> data = future.get();
        System.out.println("Partition :: "+partitionCount+" total word count :: " + Utility.totalWordCount(data));
        data.forEach((k, v) -> {
            System.out.println("Partition :: "+partitionCount+" word-- "+(k + " :: frequency-- " + v));
            finalData.put(k, finalData.getOrDefault(k, 0) + v);
        });
        Map<String, Integer> freqWordInPartitionMap = Utility.topNFreqOfWordInMap(data, 1);
        freqWordInPartitionMap.forEach((k, v) -> {
            System.out.println("Partition :: "+partitionCount+" most used word-- "+(k + " :: frequency-- " + v));
        });
    }

    private List<TextProcessorTask> getProcessorTask(BlockingQueue<String> blockingQueue, AtomicBoolean isCompleted){
        List<TextProcessorTask> textProcessorCalls = new ArrayList<>();
        for (int processorTaskCount = 0; processorTaskCount < PROCESSOR_TH_COUNT; processorTaskCount++) {
            textProcessorCalls.add(new TextProcessorTask(blockingQueue, isCompleted, WORD_STR_REGEX));
        }
        return textProcessorCalls;
    }
}
