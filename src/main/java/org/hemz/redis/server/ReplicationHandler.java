package org.hemz.redis.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import org.hemz.redis.response.IOUtils;


public class ReplicationHandler {
    public void performReplicationHandshake(RedisServer redisServer) throws IOException {
        Socket replicationClient = new Socket(redisServer.getMasterHost(), redisServer.getMasterPort());
        PrintWriter printWriter = new PrintWriter(replicationClient.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(replicationClient.getInputStream()));
        List<String> commandList = IOUtils.encodeAsList("PING");
        for(String command: commandList) {
            printWriter.print(command);
            printWriter.flush();
        }
        printWriter.flush();
        List<String> outputCommandList = IOUtils.readCommands(bufferedReader);
        System.out.println("Received Response");
        outputCommandList.stream().forEach(System.out::println);
        commandList = IOUtils.encodeAsList("REPLCONF", "listening-port", String.valueOf(redisServer.getPort()));
        for(String command: commandList) {
            printWriter.print(command);
            printWriter.flush();
        }
        printWriter.flush();
        outputCommandList = IOUtils.readCommands(bufferedReader);
        System.out.println("Received Response");
        outputCommandList.stream().forEach(System.out::println);
        commandList = IOUtils.encodeAsList("REPLCONF", "capa", "psync2");
        for(String command: commandList) {
            printWriter.print(command);
            printWriter.flush();
        }
        printWriter.flush();
        outputCommandList = IOUtils.readCommands(bufferedReader);
        System.out.println("Received Response");
        outputCommandList.stream().forEach(System.out::println);
    }
}
