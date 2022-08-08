package server;

import server.logic.data.Manage;
import server.logic.enums.Status;
import server.logic.users.Professor;
import server.logic.users.Student;
import server.logic.users.User;
import server.network.ClientHandler;
import shared.request.RequestFromServer;
import shared.response.Response;
import shared.response.ResponseStatus;
import util.Config;
import util.extra.Image;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Server {
    private final ArrayList<ClientHandler> clients;
    private static int clientCount = 0;

    private ServerSocket serverSocket;
    private final int port;
    private boolean running;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;

            listenForNewConnections();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForNewConnections() {
        while (running){
            try {
                clientCount++;
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientCount, this, socket);
                clients.add(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ClientHandler getClientHandler(int clientId) {
        for(ClientHandler clientHandler: clients) {
            if (clientHandler.getId() == clientId) {
                return clientHandler;
            }
        }
        return null;
    }

    public void handleRequest(int Id, RequestFromServer request){
        switch (request.getRequestType()){
            case LOGIN :
                System.out.println("login");
                handleLoginResponse(Id, request);
                break;
            case REFRESH:
                System.out.println("refreshing");
            case INIT:
                System.out.println("initializing");
                handleInitRequest(Id, request);
            default:
                System.out.println("WOW");
                break;
        }
    }

    private void handleInitRequest(int Id, RequestFromServer request) {
        String page = (String)request.getData("page");
        boolean result = false;
        if(page.equals("login")){
            result = true;
            ClientHandler clientHandler =  getClientHandler(Id);
            clientHandler.setClientUsername((String) request.getData("username"));
        }else{
            String name = (String)request.getData("username");
            if(Manage.getUserByUserName(name) != null){
                result = true;
            }
        }
        sendInitResponse(Id, result, page);
    }

    private void handleLoginResponse(int id, RequestFromServer request) {
        boolean result = Manage.userIsValid((String) request.getData("username"),
                (String) request.getData("password"));
        String cap = (String) request.getData("captcha");
        ClientHandler clientHandler = getClientHandler(id);
        boolean capResult = (cap != null && cap.equals(clientHandler.getClientCaptcha()));
        User user = Manage.getUserByUserName((String) request.getData("username"));
        boolean isStudent = user instanceof Student;
        boolean withdraw = (result) && (user instanceof Student) && (((Student)user).getStatus() != Status.WITHDRAW);
        sendLoginResponse(id, result, capResult, isStudent, withdraw, (String)request.getData("username"));
    }

    private void findClientAndSendResponse(int clientId, Response response) {
        ClientHandler clientHandler = getClientHandler(clientId);
        if (clientHandler != null) {
            clientHandler.sendResponse(response);
        }
    }

    private void sendInitResponse(int clientId, boolean result, String page){
        Response response = null;
        switch (page){
            case "login":
                if(result){
                    response = new Response(ResponseStatus.OK);
                    String path = Manage.setCaptcha();
                    String captchaImage = Image.encode(path);
                    response.addData("captcha", captchaImage);
                    ClientHandler clientHandler = getClientHandler(clientId);
                    assert clientHandler != null;
                    clientHandler.setClientCaptcha(path.substring(path.length() - 9, path.length() - 4));
                    System.out.println(clientHandler.getClientCaptcha());
                }
                break;
            case "bachelorMainMenu", "masterMainMenu", "PhDMainMenu":
                if(result){
                    response = new Response(ResponseStatus.OK);
                    ClientHandler clientHandler = getClientHandler(clientId);
                    String username = clientHandler.getClientUsername();
                    User user = Manage.getUserByUserName(username);
                    String path = user.getImage();
                    String userImage = Image.encode(path);
                    response.addData("userImage", userImage);
                    response.addData("user", user);
                    path = Config.getConfig().getProperty(String.class, "studentHeaderImage");
                    userImage = Image.encode(path);
                    response.addData("headerImage", userImage);
                    path = Config.getConfig().getProperty(String.class, "sharifLogo");
                    userImage = Image.encode(path);
                    response.addData("sharifLogo", userImage);
                    path = Config.getConfig().getProperty(String.class, "mainImage");
                    userImage = Image.encode(path);
                    response.addData("mainImage", userImage);
                }else{
                    response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "userNotFoundError"));
                }
                break;
            case "professorMainMenu", "DOFMainMenu":
                if(result){

                    response = new Response(ResponseStatus.OK);
                    ClientHandler clientHandler = getClientHandler(clientId);
                    String username = clientHandler.getClientUsername();
                    User user = Manage.getUserByUserName(username);
                    String path = user.getImage();
                    String userImage = Image.encode(path);
                    response.addData("userImage", userImage);
                    response.addData("user", user);
                    path = Config.getConfig().getProperty(String.class, "professorHeaderImage");
                    userImage = Image.encode(path);
                    response.addData("headerImage", userImage);
                    path = Config.getConfig().getProperty(String.class, "sharifLogo");
                    userImage = Image.encode(path);
                    response.addData("sharifLogo", userImage);
                    path = Config.getConfig().getProperty(String.class, "mainImage");
                    userImage = Image.encode(path);
                    response.addData("mainImage", userImage);
                }else{
                    response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "userNotFoundError"));
                }
                break;
            default:
                //TODO
                break;
        }
        findClientAndSendResponse(clientId, response);
    }

    private void sendLoginResponse(int clientId, boolean result, boolean capResult, boolean isStudent, boolean withdraw, String username) {
        Response response = null;

        if (result) {
            if(capResult) {
                if(isStudent) {
                    if (withdraw) {
                        User user = Manage.getUserByUserName(username);
                        if (user != null) {
                            if (user.getLastEntered() != null && ChronoUnit.HOURS.between(LocalDateTime.parse(user.getLastEntered()), LocalDateTime.now()) > 2) {
                                response = new Response(ResponseStatus.CHANGE_PASSWORD);
                            } else {
                                response = new Response(ResponseStatus.OK);
                                response.addData("student", true);
                                String level = ((Student)user) .getLevel().toString();
                                response.addData("level", level.toLowerCase());

                            }
                            response.addData("username", username);
                        }
                    } else {
                        response = new Response(ResponseStatus.ERROR);
                        String message = Config.getConfig().getProperty(String.class, "withdrawError");
                        response.setErrorMessage(message);
                    }
                }else{
                    User user = Manage.getUserByUserName(username);
                    if (user != null) {
                        if (user.getLastEntered() != null && ChronoUnit.HOURS.between(LocalDateTime.parse(user.getLastEntered()), LocalDateTime.now()) > 2) {
                            response = new Response(ResponseStatus.CHANGE_PASSWORD);
                        } else {
                            response = new Response(ResponseStatus.OK);
                            response.addData("student", false);
                            boolean isDOF = ((Professor)user).isDeanOfFaculty();
                            response.addData("isDOF", isDOF);
                        }
                        response.addData("username", username);
                    }
                }
            }else{
                response = new Response(ResponseStatus.ERROR);
                String message = Config.getConfig().getProperty(String.class, "captchaError");
                response.setErrorMessage(message);
            }
        } else {
            response = new Response(ResponseStatus.ERROR);
            String message = Config.getConfig().getProperty(String.class, "passwordWrongError");
            response.setErrorMessage(message);
        }

        findClientAndSendResponse(clientId, response);
    }

}
