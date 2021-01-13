package org.example.task1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Server {

    private ExecutorService executorService;
    private List<String> validPaths;

    public void listen(int port) {
        if (executorService == null || validPaths == null) {
            throw new IllegalStateException("Illegal servers state!");
        }

        System.out.println("Server started");

        try (ServerSocket serverSocket = createServerSocket(port)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted");
                executorService.execute(new Connection(clientSocket, validPaths));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setValidPaths(List<String> validPaths) {
        this.validPaths = validPaths;
    }
}
