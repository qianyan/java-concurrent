package com.tw.concurrentCollections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccessingMap {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Using Plain vanilla HashMap");
        useMap(new HashMap<>());
        System.out.println("Using Synchronized HashMap");
        useMap(Collections.synchronizedMap(new HashMap<>()));
        System.out.println("Using Concurrent HashMap");
        useMap(new ConcurrentHashMap<>());
    }

    private static void useMap(final Map<String, Integer> scores) throws InterruptedException {
        scores.put("Fred", 10);
        scores.put("Sara", 12);

        try {
            for(final String key : scores.keySet()) {
                System.out.println(key + " score " + scores.get(key));
                scores.put("Joe", 14);
            }
        } catch(Exception e) {
            System.out.println("Failed: " + e);
        }

        System.out.println(scores.keySet().size() + " elements in the map: " + scores);
    }
}
