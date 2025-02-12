import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.hemz.redis.handler.Handler;
import org.hemz.redis.response.IOUtils;
import org.hemz.redis.server.Mode;
import org.hemz.redis.server.RedisServer;
import org.hemz.redis.server.ReplicationHandler;
import org.hemz.redis.store.DataStore;


public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        DataStore dataStore = new DataStore();
        ReplicationHandler replicationHandler = new ReplicationHandler();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        RedisServer redisServer = new RedisServer(args);
        try {
            if(redisServer.getMode() == Mode.SLAVE) {
                replicationHandler.performReplicationHandshake(redisServer);
            }
            ServerSocket serverSocket = new ServerSocket(redisServer.getPort());
            System.out.println("Running in port: " + redisServer.getPort());
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.

            for(int i = 0; i < 10; i++) {
                executorService.submit(() -> handleCommand(serverSocket, dataStore, redisServer));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }



    private static void handleCommand(ServerSocket serverSocket, DataStore dataStore, RedisServer redisServer) {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while(true) {
                List<String> commandList = IOUtils.readCommands(in);
                Handler handler = new Handler(redisServer, dataStore);
                handler.processCommandList(clientSocket.getOutputStream(), commandList);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }
    }
}
