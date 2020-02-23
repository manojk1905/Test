package com.txt.filereader.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.txt.filereader.constants.Constants.READER_TH_COUNT;
import static com.txt.filereader.constants.Constants.RESOURCE_URL;

public class TextReader {

    /*
    Reading Input file via one thread only and adding into blocking queue, we can increase the
    READER_TH_COUNT more that 1 as per the requirement.
     */
    public void readTextData(BlockingQueue<String> blockingQueue, AtomicBoolean isCompleted, ExecutorService executorService) throws IOException {
        URL url = new URL(RESOURCE_URL);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
        //BufferedReader inputReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream("2600-0.txt"), "UTF8"));
        for (int processorTaskCount = 0; processorTaskCount < READER_TH_COUNT; processorTaskCount++) {
            TextReaderTask textReaderTask = new TextReaderTask(blockingQueue, inputReader, isCompleted);
            executorService.execute(textReaderTask);
        }
    }

}
