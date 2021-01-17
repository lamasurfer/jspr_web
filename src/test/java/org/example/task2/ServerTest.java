package org.example.task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ServerTest {

    @Test
    void test_sendManualHttpRequest_expectedBehaviour() {

        final String expected = "HTTP/1.1 200 OK";

        try (final var clientSocket = new Socket("127.0.0.1", 9999);
             final var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             final var out = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String request =
                    "GET /index.html HTTP/1.1\r\n" +
                            "Host: localhost:9999\r\n" +
                            "Connection: keep-alive\r\n" +
                            "Cache-Control: max-age=0\r\n" +
                            "Upgrade-Insecure-Requests: 1\r\n" +
                            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36\r\n" +
                            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
                            "Sec-Fetch-Site: none\r\n" +
                            "Sec-Fetch-Mode: navigate\r\n" +
                            "Sec-Fetch-User: ?1\r\n" +
                            "Sec-Fetch-Dest: document\r\n" +
                            "Accept-Encoding: gzip, deflate, br\r\n" +
                            "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                            "Cookie: Idea-cd236088=46c5250c-d46d-4c45-81a5-5f6c27aa24f1\r\n" +
                            "\r\n" +
                            "Hello\r\n" +
                            "World!\r\n" +
                            "\r\n";

            out.write((request).getBytes());
            out.flush();

            String response = in.readLine();
            System.out.println(response);

            Assertions.assertEquals(expected, response);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}