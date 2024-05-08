import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


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
            String inputLine = in.readLine();
            inputLine = in.readLine();
            inputLine = in.readLine();
            while (inputLine != null) {
                System.out.println(inputLine);
                printWriter.print("+PONG\r\n");
                printWriter.flush();
                inputLine = in.readLine();
                inputLine = in.readLine();
                inputLine = in.readLine();
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
