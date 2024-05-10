package org.hemz.redis.store;

public class Data {
    private String value;
    private long expiryTime;
    public Data(String value) {
        this.value = value;
        this.expiryTime = Long.MAX_VALUE;
    }
    public Data(String value, long expiryTime) {
        this.value = value;
        this.expiryTime = System.currentTimeMillis() + expiryTime;
    }

    public String getValue() {
        return value;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}
