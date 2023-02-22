import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class WebServer {
    public static void main(String[] args) throws IOException {
        //HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        new WebServer().start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();
        RequestHandler requestHandler = new RequestHandler(clientSocket);

        while (true){
            requestHandler.dealWithRequest();
        }
    }

    public static class RequestHandler implements Runnable{
        Socket clientSocket;

        BufferedReader input;
        BufferedWriter output;

        String methodVerb;
        String path;
        String protocol;

        public RequestHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }

        public void dealWithRequest() throws IOException {
            String[] inputArray = input.readLine().split(" ");

            if(inputArray.length != 3){
                System.out.println("Invalid Request");
                return;
            }
            this.methodVerb = inputArray[0].toLowerCase();
            this.path = "src" + inputArray[1].toLowerCase();
            this.protocol = inputArray[2].toLowerCase();

            if(validateRequests()){
                System.out.println("OK");
                //HttpHeaderBuilder.ok()
            }
        }

        private Boolean validateRequests() {
            if(!methodVerb.equals("get")){
                System.out.println("Invalid verb \nOnly Allows: GET");
                return false;
            }
            if (!path.startsWith("src/")){
                System.out.println("Invalid Path Request");
                return false;
            }else {
                Path directory = Path.of(path);
                if(Files.notExists(directory)){
                    System.out.println("File doesn't exist");
                    return false;
                    //todo send 404
                }
            }
            if (!protocol.equals("http/1.1")){
                System.out.println("InvalidProtocol");
                return false;
            }
            return true;
        }

        @Override
        public void run() {
            try {
                dealWithRequest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
