package com.txt.filereader.reader;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TextReaderTask implements Runnable {

    private BlockingQueue<String> blockingQueue;
    private BufferedReader inputReader;
    private AtomicBoolean isCompleted;

    public TextReaderTask(BlockingQueue<String> blockingQueue, BufferedReader inputReader, AtomicBoolean isCompleted) {
        this.blockingQueue = blockingQueue;
        this.inputReader = inputReader;
        this.isCompleted = isCompleted;
    }

    public void run() {
        String text;
        try {
            while ((text = inputReader.readLine()) != null) {
                if (!StringUtils.isEmpty(text)) {
                    putData(text);
                }
            }
            isCompleted.set(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putData(String text) {
        try {
            blockingQueue.put(text);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

