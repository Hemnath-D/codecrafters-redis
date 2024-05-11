package org.hemz.redis.handler;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import org.hemz.redis.response.BulkStringEncoder;
import org.hemz.redis.server.Mode;
import org.hemz.redis.server.RedisServer;
import org.hemz.redis.store.DataStore;


public class Handler {
    private DataStore dataStore;
    private RedisServer redisServer;
    public Handler(RedisServer redisServer, DataStore dataStore) {
        this.redisServer = redisServer;
        this.dataStore = dataStore;
    }
    public void processCommandList(PrintWriter printWriter, List<String> commandList) {
        String commandIdentifier = commandList.get(0).toUpperCase();
        if (Command.PING.name().equals(commandIdentifier)) {
            printWriter.print("+PONG\r\n");
            printWriter.flush();
        } else if (Command.ECHO.name().equals(commandIdentifier)) {
            String echoString = commandList.get(1);
            String returnString = BulkStringEncoder.encode(echoString);
            System.out.println(returnString);
            printWriter.print(returnString);
            printWriter.flush();
        } else if(Command.SET.name().equals(commandIdentifier)) {
            String key = commandList.get(1);
            String value = commandList.get(2);
            if(commandList.size() == 3) {
                dataStore.put(key, value);
            } else {
                if("PX".equalsIgnoreCase(commandList.get(3))) {
                    dataStore.put(key, value, Long.parseLong(commandList.get(4)));
                }
            }
            String returnString = String.format("+OK\r\n");
            printWriter.print(returnString);
            printWriter.flush();
        } else if(Command.GET.name().equals(commandIdentifier)) {
            String key = commandList.get(1);
            Optional<String> valueOpt = dataStore.get(key);
            String returnString;
            if(valueOpt.isPresent()) {
                String value = valueOpt.get();
                returnString = BulkStringEncoder.encode(value);
            } else {
                returnString = "$-1\r\n";
            }
            printWriter.print(returnString);
            printWriter.flush();
        } else if(Command.INFO.name().equals(commandIdentifier)) {
            String argument = commandList.get(1);
            if(argument.equalsIgnoreCase("replication")) {
                String replicationMode = redisServer.getMode().getDisplayName();
                String returnString = BulkStringEncoder.encode("role:" + replicationMode);
                if(redisServer.getMode() == Mode.MASTER) {
                    returnString = BulkStringEncoder.encode(String.format("role:%1$s\nmaster_replid:%2$s\nmaster_repl_offset:%3$s", replicationMode,
                            redisServer.getMasterReplId(), redisServer.getMasterReplOffset()));
                }
                printWriter.print(returnString);
                printWriter.flush();
            }
        }
    }
}
