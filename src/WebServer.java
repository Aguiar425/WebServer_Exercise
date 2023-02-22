import Util.HttpHeaderBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;

        new WebServer().start(port);
    }

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        ExecutorService service = Executors.newCachedThreadPool();

        while (true) {
            serveRequests(serverSocket, service);
        }


    }

    public void serveRequests(ServerSocket socket, ExecutorService service) throws IOException {
        Socket clientSocket = socket.accept();

        RequestHandler requestHandler = new RequestHandler(clientSocket);

        service.submit(requestHandler);
    }

    public static class RequestHandler implements Runnable {
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

        @Override
        public void run() {
            try {
                dealWithRequest();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void dealWithRequest() throws IOException {
            String[] inputArray = input.readLine().split(" ");

            if (inputArray.length != 3) {
                System.out.println("Invalid Request");
                send(HttpHeaderBuilder.notAllowed());
                return;
            }

            this.methodVerb = inputArray[0].toLowerCase();

            this.path = inputArray[1].toLowerCase();

            this.protocol = inputArray[2].toLowerCase();

            if (validateRequests()) {
                sendFile();
            }
        }

        private void sendFile() throws IOException {
            String extension = path.substring(path.length()-4, path.length());
            switch(extension) {
                case ".txt" : txtSender();
                case ".png" : txtSender();
                case ".jpg":
                case ".mp3":
                case ".mp4":
            }

        }

        private void txtSender() throws FileNotFoundException {
            File file = new File("src/webresources" +path);
            Scanner scanner = new Scanner(file);

            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }
            scanner.close();
            send(HttpHeaderBuilder.ok(path,Long.valueOf(stringBuilder.length())));
            send(stringBuilder.toString());
        }

        private void imageSender() throws FileNotFoundException {
            File file = new File("src/webresources" +path);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine())


        }

        private Boolean validateRequests() throws IOException {
            if (!methodVerb.equals("get")) {
                send(HttpHeaderBuilder.notAllowed());
                return false;
            }
            if (!path.startsWith("/")) {
                send(HttpHeaderBuilder.badRequest());
                System.out.println("Invalid Path Request");
                return false;
            } else {
                Path directory = Path.of("src/webresources" + path);
                if (Files.notExists(directory)) {
                    send(HttpHeaderBuilder.notFound(path));
                    System.out.println("File doesn't exist");
                    return false;
                }
            }
            if (!protocol.equals("http/1.1")) {
                send(HttpHeaderBuilder.notSupported());
                System.out.println("InvalidProtocol");
                return false;
            }
            return true;
        }

        public void send(String message) {
            try {
                output.write(message);
                output.newLine();
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
