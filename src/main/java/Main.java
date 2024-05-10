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


public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        int port = 6379;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.

            for(int i = 0; i < 10; i++) {
                executorService.submit(() -> handleCommand(serverSocket));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void handleCommand(ServerSocket serverSocket) {
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
                processCommandList(printWriter, commandList);
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

    static void processCommandList(PrintWriter printWriter, List<String> commandList) {
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
        }
    }
}
