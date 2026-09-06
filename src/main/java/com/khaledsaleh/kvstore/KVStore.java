package com.khaledsaleh.kvstore;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

// HashMap + Locking Logic

public class KVStore {
    
    private final ReentrantLock lock = new ReentrantLock();

    private final HashMap<String, String> store = new HashMap<>();
    
    public void set(String key, String value) {
        
        lock.lock();

        try {
            store.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public String get(String key) {
        
        lock.lock();

        try {
            return store.get(key);
        } finally {
            lock.unlock();
        }
    }

    public void del(String key){

        lock.lock();

        try {
        store.remove(key);
        } finally {
            lock.unlock();
        }
    }
    
}
