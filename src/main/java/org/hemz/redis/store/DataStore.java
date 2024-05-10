package org.hemz.redis.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class DataStore {
    Map<String, Data> data = new HashMap<>();
    public void put(String key, String value) {
        data.put(key, new Data(value));
    }

    public void put(String key, String value, long expiry) {
        data.put(key, new Data(value, expiry));
    }

    public Optional<String> get(String key) {
        Data element = this.data.get(key);
        long currentTimeMillis = System.currentTimeMillis();
        if(element.getExpiryTime() <= currentTimeMillis) {
            this.data.remove(key);
            return Optional.empty();
        }
        return Optional.ofNullable(element.getValue());
    }
}
