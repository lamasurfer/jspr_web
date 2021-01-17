package org.example.task2;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Connection implements Runnable {

    private final Socket clientSocket;
    private final Map<String, Map<String, Handler>> handlers;


    public Connection(Socket clientSocket, Map<String, Map<String, Handler>> handlers) {
        this.clientSocket = clientSocket;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        try (clientSocket;
             final var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             final var out = new BufferedOutputStream(clientSocket.getOutputStream())) {

            final List<String> receivedList = readRequest(in);

            if (!checkRequestLine(receivedList)) {
                System.out.println("Wrong request line size!");
                return;
            }

            final Request request = new Request.RequestBuilder()
                    .setRequestLine(receivedList)
                    .setHeaders(receivedList)
                    .setMessage(receivedList)
                    .build();

            System.out.println(request);

            if (!checkHandlers(request)) {
                handleWrongRequest(out);
                System.out.println("No suitable handler!");
                return;
            }

            final String method = request.getMethod();
            final String path = request.getPath();

            final Handler handler = handlers.get(method).get(path);
            handler.handle(request, out);

            System.out.println("Request processed!");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> readRequest(BufferedReader in) throws IOException {
        List<String> requestAsList = new ArrayList<>();
        String received;
        while (in.ready()) { // если с message то пойдет по второму кругу
            do {
                received = in.readLine();
                if (received == null) {
                    break;
                }
                requestAsList.add(received);
            } while (!received.isEmpty());
        }
        return requestAsList;
    }

    public boolean checkRequestLine(List<String> requestAsList) {
        if (requestAsList == null || requestAsList.isEmpty()) {
            return false;
        }
        final String requestLine = requestAsList.get(0);
        final String[] parts = requestLine.split("\\s");

        return parts.length == 3;
    }

    public boolean checkHandlers(Request request) {
        final String method = request.getMethod();
        final String path = request.getPath();

        if (!handlers.containsKey(method)) {
            return false;
        } else {
            var value = handlers.get(method);
            return value.containsKey(path);
        }
    }

    public void handleWrongRequest(BufferedOutputStream responseStream) throws IOException {
        responseStream.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        responseStream.flush();
    }
}
