package org.hemz.redis.server;

public enum Mode {
    MASTER("master"),
    SLAVE("slave");
    private String displayName;

    Mode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
