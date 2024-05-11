package org.hemz.redis.response;

public class BulkStringEncoder {
    public static String encode(String input) {
        return String.format("$%1$s\r%n%2$s\r%n", input.length(), input);
    }
}
