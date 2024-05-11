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
import org.hemz.redis.handler.Command;
import org.hemz.redis.handler.Handler;
import org.hemz.redis.store.DataStore;


public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        DataStore dataStore = new DataStore();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        int port = 6379;
        if(args.length >= 2) {
            if("--port".equals(args[0])) {
                port = Integer.parseInt(args[1]);
            }
        }
        System.out.println("Running in port: " + port);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.

            for(int i = 0; i < 10; i++) {
                executorService.submit(() -> handleCommand(serverSocket, dataStore));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void handleCommand(ServerSocket serverSocket, DataStore dataStore) {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while(true) {
                String inputLine = in.readLine();
                int commandLength = Integer.parseInt(inputLine.substring(1));
                List<String> commandList = new ArrayList<>();
                for (int i = 0; i < commandLength; i++) {
                    inputLine = in.readLine();
                    int stringLength = Integer.parseInt(inputLine.substring(1));
                    inputLine = in.readLine();
                    String command = inputLine.substring(0, stringLength);
                    commandList.add(command);
                }
                Handler handler = new Handler(dataStore);
                handler.processCommandList(printWriter, commandList);
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
