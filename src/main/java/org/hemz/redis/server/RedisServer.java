package org.hemz.redis.server;

public class RedisServer {
    private int port = 6379;
    private Mode mode = Mode.MASTER;
    private String masterHost;
    private String masterPort;
    private String masterReplId;
    private String masterReplOffset;
    public RedisServer(String[] args) {
        int i = 0;
        while(i < args.length) {
            if(("--port".equals(args[i]))) {
                this.port = Integer.parseInt(args[i + 1]);
                i++;
            } else if("--replicaof".equals(args[i])) {
                this.mode = Mode.SLAVE;
                this.masterHost = args[i + 1];
                this.masterPort = args[i + 2];
                i += 2;
            } else {
                throw new RuntimeException("Unrecognized option from command line");
            }
            i++;
        }
        if(this.mode == Mode.MASTER) {
            this.masterReplId = "8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb";
            this.masterReplOffset = "0";
        }
    }

    public int getPort() {
        return port;
    }

    public Mode getMode() {
        return mode;
    }

    public String getMasterReplId() {
        return masterReplId;
    }

    public String getMasterReplOffset() {
        return masterReplOffset;
    }
}
