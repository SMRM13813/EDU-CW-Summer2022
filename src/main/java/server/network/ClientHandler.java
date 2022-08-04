package server.network;

import org.codehaus.jackson.map.ObjectMapper;
import server.Server;
import shared.request.RequestFromServer;
import shared.response.Response;
import util.Jackson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {
    private PrintStream printStream;
    private Scanner scanner;

    private ObjectMapper objectMapper;
    private final Server server;

    private final int clientId;
    private String clientCaptcha;

    public ClientHandler(int id, Server server, Socket socket) {
        this.clientId = id;
        this.server = server;

        try {
            printStream = new PrintStream(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
            objectMapper = Jackson.getNetworkObjectMapper();

            makeListenerThread();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeListenerThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                String requestString = scanner.nextLine();
                try {
                    RequestFromServer request = objectMapper.readValue(requestString, RequestFromServer.class);
                    handleRequest(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void handleRequest(RequestFromServer request) {
        server.handleRequest(clientId, request);
    }

    public void sendResponse(Response response) {
        try {
            String responseString = objectMapper.writeValueAsString(response);
            printStream.println(responseString);
            printStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId(){
        return clientId;
    }

    public String getClientCaptcha() {
        return clientCaptcha;
    }

    public void setClientCaptcha(String clientCaptcha) {
        this.clientCaptcha = clientCaptcha;
    }
}
