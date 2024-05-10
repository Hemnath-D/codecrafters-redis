package org.hemz.redis.handler;

import java.io.PrintWriter;
import java.util.List;
import org.hemz.redis.store.DataStore;


public class Handler {
    private DataStore dataStore;
    public Handler(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    public void processCommandList(PrintWriter printWriter, List<String> commandList) {
        String commandIdentifier = commandList.get(0).toUpperCase();
        if (Command.PING.name().equals(commandIdentifier)) {
            printWriter.print("+PONG\r\n");
            printWriter.flush();
        } else if (Command.ECHO.name().equals(commandIdentifier)) {
            String echoString = commandList.get(1);
            String returnString = String.format("$%1$s\r%n%2$s\r%n", echoString.length(), echoString);
            System.out.println(returnString);
            printWriter.print(returnString);
            printWriter.flush();
        } else if(Command.SET.name().equals(commandIdentifier)) {
            String key = commandList.get(1);
            String value = commandList.get(2);
            dataStore.put(key, value);
            String returnString = String.format("+OK\r\n");
            printWriter.print(returnString);
            printWriter.flush();
        } else if(Command.GET.name().equals(commandIdentifier)) {
            String key = commandList.get(1);
            String value = dataStore.get(key);
            String returnString = String.format("$%1$s\r%n%2$s\r%n", value.length(), value);
            printWriter.print(returnString);
            printWriter.flush();
        }
    }
}
