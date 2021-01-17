package org.example.task2;

import java.io.BufferedOutputStream;
import java.io.IOException;

public interface Handler {

    void handle(Request request, BufferedOutputStream responseStream) throws IOException;
}
