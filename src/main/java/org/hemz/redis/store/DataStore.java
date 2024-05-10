package org.hemz.redis.store;

import java.util.HashMap;
import java.util.Map;


public class DataStore {
    Map<String, String> data = new HashMap<>();
    public String put(String key, String value) {
        return data.put(key, value);
    }
    public String get(String key) {
        return data.get(key);
    }
}
