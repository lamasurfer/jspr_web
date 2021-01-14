package org.example.task2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        final List<String> VALID_PATHS = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html",
                "/styles.css", "/app.js", "/classic.html", "/links.html", "/forms.html", "/events.html", "/events.js");

        final int MAX_CONNECTIONS = 64;
        final int PORT = 9999;

        final Server server = new Server();

        server.setExecutorService(Executors.newFixedThreadPool(MAX_CONNECTIONS));

        server.addHandler("GET", VALID_PATHS, (request, responseStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var length = Files.size(filePath);
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(filePath, responseStream);
            responseStream.flush();
        });

        server.addHandler("GET", "/classic.html", (request, responseStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);

            final var template = Files.readString(filePath);
            final var content = template.replace(
                    "{time}",
                    LocalDateTime.now().toString()
            ).getBytes();
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + content.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(content);
            responseStream.flush();

        });

        server.addHandler("POST", "/messages", (request, responseStream) -> {
            // TODO: handlers code
        });

        server.listen(PORT);

    }
}
