package server;

import server.logic.data.Manage;
import shared.model.enums.Status;
import server.logic.users.Professor;
import server.logic.users.Student;
import server.logic.users.User;
import server.network.ClientHandler;
import shared.model.objects.Course;
import shared.model.users.StudentView;
import shared.model.users.UserView;
import shared.request.RequestFromServer;
import shared.response.Response;
import shared.response.ResponseStatus;
import shared.util.Config;
import shared.util.extra.Image;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForNewConnections() {
        while (running) {
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
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getId() == clientId) {
                return clientHandler;
            }
        }
        return null;
    }

    public void handleRequest(int Id, RequestFromServer request) {
        switch (request.getRequestType()) {
            case LOGIN -> {
                System.out.println("login");
                handleLoginRequest(Id, request);
            }
            case INIT -> {
                System.out.println("initializing");
                handleInitRequest(Id, request);
            }
            case EDIT -> {
                System.out.println("editing");
                handleEditRequest(Id, request);
            }
            case ADD_USER -> {
                System.out.println("adding user");
                handleAddRequest(Id, request);
            }
            case CHECK -> {
                System.out.println("checking supervisor");
                handleCheckSuperVisorRequest(Id, request);
            }
            case GET_USER -> {
                System.out.println("sending user");
                handleGetUserRequest(Id, request);
            }
        }
    }


    private void handleInitRequest(int Id, RequestFromServer request) {
        String page = (String) request.getData("page");
        boolean result = false;
        if (page.equals("login")) {
            //TODO
            result = true;
            ClientHandler clientHandler = getClientHandler(Id);
            clientHandler.setClientUsername((String) request.getData("username"));
            System.out.println(clientHandler.getClientUsername());
        } else {
            String name = (String) request.getData("username");
            if (Manage.getUserByUserName(name) != null) {
                result = true;
            }
        }
        sendInitResponse(Id, result, page);
    }

    private void handleLoginRequest(int id, RequestFromServer request) {
        boolean result = Manage.userIsValid((String) request.getData("username"),
                (String) request.getData("password"));
        String cap = (String) request.getData("captcha");
        ClientHandler clientHandler = getClientHandler(id);
        boolean capResult = (cap != null && cap.equals(clientHandler.getClientCaptcha()));
        User user = Manage.getUserByUserName((String) request.getData("username"));
        if (user != null) {
            clientHandler.setClientUsername((String) request.getData("username"));
        }
        boolean isStudent = user instanceof Student;
        boolean withdraw = (result) && (user instanceof Student) && (((Student) user).getStatus() != Status.WITHDRAW);
        sendLoginResponse(id, result, capResult, isStudent, withdraw, (String) request.getData("username"));
    }

    public void handleEditRequest(int id, RequestFromServer request) {
        boolean result = false;

        switch ((String) request.getData("type")) {

            case "user":
                User user = Manage.getUserByUserName((String) request.getData("target"));
                result = user != null;

                if (result) {
                    switch ((String) request.getData("field")) {
                        case "lastEntered" -> user.setLastEntered((String) request.getData("value"));
                        case "password" -> {
                            String password = (String) request.getData("value");
                            String passwordAgain = (String) request.getData("repeat");
                            String res = checkNewPassword(user, password, passwordAgain);
                            Response response;
                            switch (res) {
                                case "OK":
                                    user.setPassWord(password);
                                    response = new Response(ResponseStatus.OK);
                                    response.addData("message", Config.getConfig().getProperty(String.class, "passChange"));
                                    findClientAndSendResponse(id, response);
                                    return;
                                case "notLong":
                                    response = new Response(ResponseStatus.ERROR);
                                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "passwordLongError"));
                                    findClientAndSendResponse(id, response);
                                    return;
                                case "repeat":
                                    response = new Response(ResponseStatus.ERROR);
                                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "notSamePassError"));
                                    findClientAndSendResponse(id, response);
                                    return;
                                case "old":
                                    response = new Response(ResponseStatus.ERROR);
                                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "oldPassError"));
                                    findClientAndSendResponse(id, response);
                                    return;
                            }

                        }
                        default -> {

                        }
                    }
                }
                break;
            case "course":
                Course course = Manage.getCourseByCode((String) request.getData("target"));

                break;
        }
        sendEditResponse(id, result);
    }

    public void handleAddRequest(int id, RequestFromServer request) {

        UserView userView = (UserView) request.getData("user");
        String pass = (String) request.getData("password");
        boolean canAdd;

        if (userView instanceof StudentView) {
            Student student = (Student) Manage.getUserByUserView(userView, pass);
            canAdd = Manage.addStud(student);
            if (canAdd) {
                Response response = new Response(ResponseStatus.OK);
                response.addData("message", Config.getConfig().getProperty(String.class, "studAddMessage"));
                findClientAndSendResponse(id, response);
            } else {
                Response response = new Response(ResponseStatus.ERROR);
                response.setErrorMessage(Config.getConfig().getProperty(String.class, "cantAddUserError"));
                findClientAndSendResponse(id, response);
            }
        } else {
            Professor professor = (Professor) Manage.getUserByUserView(userView, pass);
            canAdd = Manage.addProf(professor);
            if (canAdd) {
                Response response = new Response(ResponseStatus.OK);
                response.addData("message", Config.getConfig().getProperty(String.class, "profAddMessage"));
                findClientAndSendResponse(id, response);
            } else {
                Response response = new Response(ResponseStatus.ERROR);
                response.setErrorMessage(Config.getConfig().getProperty(String.class, "cantAddUserError"));
                findClientAndSendResponse(id, response);
            }
        }

    }

    private void handleCheckSuperVisorRequest(int id, RequestFromServer request) {
        String code = (String) request.getData("supervisor");
        System.out.println(code);
        boolean res = Manage.getProfessorByCode(code) != null;
        Response response = new Response();
        if (res) {
            response.setStatus(ResponseStatus.OK);
        } else {
            response.setStatus(ResponseStatus.ERROR);
            response.setErrorMessage(Config.getConfig().getProperty("invalidSuperVisorError"));
        }
        findClientAndSendResponse(id, response);
    }

    public String checkNewPassword(User user, String pass, String passAgain) {
        if (pass.equals(passAgain)) {
            if (pass.length() < 4) {
                return "notLong";
            } else if (pass.equals(user.getPassWord())) {
                return "old";
            } else {
                return "OK";
            }
        } else {
            return "repeat";
        }
    }

    private void handleGetUserRequest(int id, RequestFromServer request) {
        String username = (String) request.getData("username");
        User user = Manage.getUserByUserName(username);
        if (user == null) {
            Response response = new Response(ResponseStatus.ERROR);
            response.setErrorMessage(Config.getConfig().getProperty(String.class, "userNotFoundError"));
            findClientAndSendResponse(id, response);
        } else {
            Response response = new Response(ResponseStatus.OK);
            findClientAndSendResponse(id, response);
        }
    }

    private void findClientAndSendResponse(int clientId, Response response) {
        ClientHandler clientHandler = getClientHandler(clientId);
        if (clientHandler != null) {
            clientHandler.sendResponse(response);
        }
    }

    private void sendInitResponse(int clientId, boolean result, String page) {
        Response response = null;
        switch (page) {
            case "login":
                if (result) {
                    response = new Response(ResponseStatus.OK);
                    String path = Manage.setCaptcha();
                    String captchaImage = Image.encode(path);
                    response.addData("captcha", captchaImage);
                    ClientHandler clientHandler = getClientHandler(clientId);
                    assert clientHandler != null;
                    clientHandler.setClientCaptcha(path.substring(path.length() - 9, path.length() - 4));
                    //clientHandler.setClientUsername((String) response.getData("username"));
                    System.out.println(clientHandler.getClientCaptcha());
                }
                break;
            case "bachelorMainMenu", "masterMainMenu", "PhDMainMenu":
                if (result) {
                    response = new Response(ResponseStatus.OK);
                    ClientHandler clientHandler = getClientHandler(clientId);
                    String username = clientHandler.getClientUsername();
                    UserView user = Manage.getUserViewByUserName(username);
                    User user1 = Manage.getUserByUserName(username);
                    System.out.println(user1.getUserName());
                    String path = user1.getImage();
                    String supervisorCode = ((Student) user1).getSupervisor();
                    if (Manage.getProfessorByCode(supervisorCode) != null) {
                        String supervisor = Manage.getProfessorByCode(supervisorCode).getName() + " " +
                                Manage.getProfessorByCode(supervisorCode).getLastName();
                        response.addData("supervisor", supervisor);
                    } else {
                        response.addData("supervisor", "");
                    }
                    //TODO --> auth token + ping!!!
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
                } else {
                    response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "userNotFoundError"));
                }
                break;
            case "professorMainMenu", "DOFMainMenu":
                if (result) {

                    response = new Response(ResponseStatus.OK);
                    ClientHandler clientHandler = getClientHandler(clientId);
                    String username = clientHandler.getClientUsername();
                    UserView user = Manage.getUserViewByUserName(username);
                    User user1 = Manage.getUserByUserName(username);
                    System.out.println(user1.getUserName());
                    String path = user1.getImage();
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
                } else {
                    response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "userNotFoundError"));
                }
                break;
            case "changePassword":
                if (result) {
                    response = new Response(ResponseStatus.OK);
                    ClientHandler clientHandler = getClientHandler(clientId);
                    String username = clientHandler.getClientUsername();
                    UserView user = Manage.getUserViewByUserName(username);
                    User user1 = Manage.getUserByUserName(username);
                    String path = user1.getImage();
                    String userImage = Image.encode(path);
                    response.addData("userImage", userImage);
                    response.addData("user", user);
                    if (user1 instanceof Professor) {
                        path = Config.getConfig().getProperty(String.class, "professorHeaderImage");
                        userImage = Image.encode(path);
                    } else {
                        path = Config.getConfig().getProperty(String.class, "studentHeaderImage");
                        userImage = Image.encode(path);
                    }
                    response.addData("headerImage", userImage);
                    path = Config.getConfig().getProperty(String.class, "sharifLogo");
                    userImage = Image.encode(path);
                    response.addData("sharifLogo", userImage);
                    path = Config.getConfig().getProperty(String.class, "mainImage");
                    userImage = Image.encode(path);
                    response.addData("mainImage", userImage);
                } else {
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
            if (capResult) {
                if (isStudent) {
                    if (withdraw) {
                        User user = Manage.getUserByUserName(username);
                        if (user != null) {
                            if (user.getLastEntered() != null && ChronoUnit.HOURS.between(LocalDateTime.parse(user.getLastEntered()), LocalDateTime.now()) > 2) {
                                response = new Response(ResponseStatus.CHANGE_PASSWORD);
                            } else {
                                response = new Response(ResponseStatus.OK);
                                response.addData("student", true);
                                String level = ((Student) user).getLevel().toString();
                                response.addData("level", level.toLowerCase());

                            }
                            response.addData("username", username);
                        }
                    } else {
                        response = new Response(ResponseStatus.ERROR);
                        String message = Config.getConfig().getProperty(String.class, "withdrawError");
                        response.setErrorMessage(message);
                    }
                } else {
                    User user = Manage.getUserByUserName(username);
                    if (user != null) {
                        if (user.getLastEntered() != null && ChronoUnit.HOURS.between(LocalDateTime.parse(user.getLastEntered()), LocalDateTime.now()) > 2) {
                            response = new Response(ResponseStatus.CHANGE_PASSWORD);
                        } else {
                            response = new Response(ResponseStatus.OK);
                            response.addData("student", false);
                            boolean isDOF = ((Professor) user).isDeanOfFaculty();
                            response.addData("isDOF", isDOF);
                        }
                        response.addData("username", username);
                    }
                }
            } else {
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

    public void sendEditResponse(int clientId, boolean result) {
        Response response;
        if (result) {
            response = new Response(ResponseStatus.OK);
        } else {
            response = new Response(ResponseStatus.ERROR);
            response.setErrorMessage(Config.getConfig().getProperty(String.class, "userNotFoundError"));
        }
        findClientAndSendResponse(clientId, response);
    }
}
