package org.example.task2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Request {

    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final String message;

    public Request(RequestBuilder requestBuilder) {
        this.method = requestBuilder.method;
        this.path = requestBuilder.path;
        this.version = requestBuilder.version;
        this.headers = requestBuilder.headers;
        this.message = requestBuilder.message;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(">>Request:\n");
        sb.append("method: ").append(method).append("\n");
        sb.append("path: ").append(path).append("\n");
        sb.append("version: ").append(version).append("\n");
        sb.append(">Headers:\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry).append("\n");
        }
        sb.append(">Message:\n").append(message).append("\n");
        return sb.toString();
    }

    public static class RequestBuilder {

        private static final String NO_METHOD = "no_method";
        private static final String NO_PATH = "no_path";
        private static final String NO_VERSION = "no_version";
        private static final String NO_MESSAGE = "no_message";

        private String method;
        private String path;
        private String version;
        private final Map<String, String> headers = new HashMap<>();
        private String message;

        public RequestBuilder setRequestLine(List<String> requestList) {
            if (requestList == null || requestList.isEmpty()) {
                this.method = NO_METHOD;
                this.path = NO_PATH;
                this.version = NO_VERSION;
                return this;
            }
            String requestLine = requestList.get(0);
            requestList.remove(0);
            String[] parts = requestLine.split("\\s");
            this.method = parts[0];
            this.path = parts[1];
            this.version = parts[2];
            return this;
        }

        public RequestBuilder setHeaders(List<String> requestList) {
            if (requestList == null || requestList.isEmpty()) {
                return this;
            }
            Iterator<String> it = requestList.listIterator();
            while (it.hasNext()) {
                String line = it.next();
                if (line.isBlank()) {
                    it.remove();
                    break;
                }
                String[] header = line.split(":\\s");
                headers.put(header[0], header[1]);
                it.remove();
            }
            return this;
        }

        public RequestBuilder setMessage(List<String> requestList) {
            if (requestList == null || requestList.isEmpty()) {
                this.message = NO_MESSAGE;
                return this;
            }
            Iterator<String> it = requestList.listIterator();
            StringBuilder message = new StringBuilder();
            while (it.hasNext()) {
                String line = it.next();
                if (line.isBlank()) {
                    it.remove();
                    break;
                }
                message.append(line).append("\n");
                it.remove();
            }
            this.message = message.toString();
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
