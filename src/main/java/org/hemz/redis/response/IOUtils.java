package org.hemz.redis.response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class IOUtils {
    public static String encodeAsBulkString(String input) {
        return String.format("$%1$s\r%n%2$s\r%n", input.length(), input);
    }

    public static List<String> encodeAsList(String... input) {
        List<String> inputList = new ArrayList<>();
        inputList.add(String.format("*%1$s\r\n", input.length));
        for(String s: input) {
            inputList.add(IOUtils.encodeAsBulkString(s));
        }
        return inputList;
    }

    public static List<String> readCommands(BufferedReader in) throws IOException {
        String inputLine = in.readLine();
        List<String> commandList = new ArrayList<>();
        if(inputLine.startsWith("+")) {
            commandList.add(inputLine.substring(1));
        } else {
            int commandLength = Integer.parseInt(inputLine.substring(1));
            for (int i = 0; i < commandLength; i++) {
                inputLine = in.readLine();
                int stringLength = Integer.parseInt(inputLine.substring(1));
                inputLine = in.readLine();
                String command = inputLine.substring(0, stringLength);
                commandList.add(command);
            }
        }
        return commandList;
    }
}
