package org.hemz.redis.server;

public class RedisServer {
    private int port = 6379;
    private Mode mode = Mode.MASTER;
    private String masterHost;
    private String masterPort;
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
    }

    public int getPort() {
        return port;
    }

    public Mode getMode() {
        return mode;
    }
}
