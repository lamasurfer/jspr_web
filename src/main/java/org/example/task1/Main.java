package org.example.task1;

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
        server.setValidPaths(VALID_PATHS);

        server.listen(PORT);

    }
}
