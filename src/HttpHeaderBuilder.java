import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpHeaderBuilder implements HttpHandler {

    public static String notFound(String fileName, long length) {
        return "HTTP/1.0 404 Not Found\r\n" +
                getContentType(fileName) +
                "Content-Length: " + length + "\r\n\r\n";
    }

    public static String ok(String fileName, long length) {
        return "HTTP/1.0 200 Document Follows\r\n" +
                getContentType(fileName) +
                "Content-Length: " + length + "\r\n\r\n";
    }

    private static String getContentType(String fileName) {
        String contentType = "";
        try {
            contentType = Files.probeContentType(Path.of("webResources/" + fileName));
            //return contentType;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contentType;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
