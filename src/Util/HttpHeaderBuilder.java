package Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpHeaderBuilder {

    public static String notAllowed() {
        return "HTTP/1.1 Method 405 Not allowed";
    }

    public static String notFound(String fileName/*, long length*/) {
        return "HTTP/1.1 404 Not Found\r\n" +
                getContentType(fileName) +
                "Content-Length: " + /*length*/ "\r\n\r\n";
    }

    public static String ok(String fileName, long length) {
        return "HTTP/1.1 200 Document Follows\r\n" +
                getContentType(fileName) +
                "Content-Length: " + length + "\r\n";
    }

    private static String getContentType(String fileName) {
        String contentType = "";
        try {
            contentType = Files.probeContentType(Path.of("webresources" + fileName));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contentType;
    }

    public static String badRequest() {
        return "HTTP/1.1 400 Bad request\r\n";
    }

    public static String notSupported() {
        return "HTTP/1.1 505 HTTP Version not supported\r\n";
    }
}
