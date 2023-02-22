import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {
        //HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        new WebServer().start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();
        RequestHandler requestHandler = new RequestHandler(clientSocket);
    }

    public static class RequestHandler implements Runnable{

        Socket clientSocket = new Socket( "localhost", 8080);

        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        public RequestHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

        }
    }
}
