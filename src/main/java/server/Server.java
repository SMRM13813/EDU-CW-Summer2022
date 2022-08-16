package server;

import server.logic.data.Data;
import server.logic.data.Manage;
import shared.model.enums.Department;
import shared.model.enums.Status;
import server.logic.users.Professor;
import server.logic.users.Student;
import server.logic.users.User;
import server.network.ClientHandler;
import shared.model.objects.Course;
import shared.model.users.ProfessorView;
import shared.model.users.StudentView;
import shared.model.users.UserView;
import shared.request.RequestFromServer;
import shared.response.Response;
import shared.response.ResponseStatus;
import shared.util.Config;
import shared.util.extra.Image;
import shared.util.extra.Token;

import java.io.IOException;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static server.logic.data.Data.*;

public class Server {
    private final ArrayList<ClientHandler> clients;
    private static int clientCount = 0;

    private ServerSocket serverSocket;
    private final int port;
    private boolean running;
    private Token token;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.token = new Token();
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
                ClientHandler clientHandler = new ClientHandler(token.generateToken(), clientCount, this, socket);
                clients.add(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clientDisconnected(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        clientCount--;
    }

    private ClientHandler getClientHandler(int clientId) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getId() == clientId) {
                return clientHandler;
            }
        }
        return null;
    }

    public void handleRequest(String token, int Id, RequestFromServer request) {
        boolean tokenTrue = false;
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getToken().equals(token)) {
                tokenTrue = true;
                break;
            }
        }
        if (tokenTrue) {
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
                case EDIT_BY_USER -> {
                    System.out.println("editing by user");
                    handleEditByUserRequest(Id, request);
                }
                case ADD -> {
                    System.out.println("adding user/course");
                    handleAddRequest(Id, request);
                }
                case REMOVE -> {
                    System.out.println("removing user/course");
                    handleRemoveRequest(Id, request);
                }
                case CHECK -> {
                    System.out.println("checking supervisor");
                    handleCheckSuperVisorRequest(Id, request);
                }
                case GET_USER -> {
                    System.out.println("sending user");
                    handleGetUserRequest(Id, request);
                }
                case GET_TABLE -> {
                    System.out.println("sending table");
                    handleGetTableRequest(Id, request);
                }
                case FILTER -> {
                    System.out.println("filtering");
                    handleFilterRequest(Id, request);
                }
            }
        } else {

        }
    }


    private void handleInitRequest(int Id, RequestFromServer request) {
        String page = (String) request.getData("page");
        boolean result = false;
        if (page.equals("login")) {
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


    private void handleEditByUserRequest(int id, RequestFromServer request) {
        Object userView = request.getData("user");
        UserView doer = (UserView) request.getData("doer");
        String field = (String) request.getData("field");
        Object value = request.getData("value");
        if (userView instanceof UserView) {
            if (userView instanceof StudentView) {

            } else {

            }
            //TODO
        } else {
            Course course = (Course) userView;
            ProfessorView professorView = (ProfessorView) userView;
            if (!course.getDepartment().equals(professorView.getDepartment())) {
                Response response = new Response(ResponseStatus.ERROR);
                response.setErrorMessage(Config.getConfig().getProperty(String.class, "notSameCourseDepartmentError"));
                findClientAndSendResponse(id, response);
            } else {
                switch (field) {
                    case "name":
                        Course course1 = Manage.getCourseByCode(course.getCourseCode());
                        course1.setName((String) value);
                        Response response = new Response(ResponseStatus.OK);
                        findClientAndSendResponse(id, response);
                        break;
                    case "unit":
                        course1 = Manage.getCourseByCode(course.getCourseCode());
                        course1.setUnits(Integer.parseInt((String) value));
                        response = new Response(ResponseStatus.OK);
                        findClientAndSendResponse(id, response);
                        break;
                    case "department":
                        course1 = Manage.getCourseByCode(course.getCourseCode());
                        Department department = Manage.StringToDep((String) value);
                        course1.setDepartment(department);
                        response = new Response(ResponseStatus.OK);
                        findClientAndSendResponse(id, response);
                        break;
                    case "finish":
                        course1 = Manage.getCourseByCode(course.getCourseCode());
                        course1.setFinish((String) value);
                        response = new Response(ResponseStatus.OK);
                        findClientAndSendResponse(id, response);
                        break;
                    case "teacher":
                        course1 = Manage.getCourseByCode(course.getCourseCode());
                        course1.setTeacher((String) value);
                        response = new Response(ResponseStatus.OK);
                        findClientAndSendResponse(id, response);
                        break;
                }
            }
        }

    }

    public void handleAddRequest(int id, RequestFromServer request) {

        String type = (String) request.getData("type");
        if (type.equals("user")) {
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
        } else {
            Course course = (Course) request.getData("course");
            boolean addCan = Manage.addCor(course);
            if (addCan) {
                Response response = new Response(ResponseStatus.OK);
                response.addData("message", Config.getConfig().getProperty(String.class, "corAddMessage"));
                findClientAndSendResponse(id, response);
            } else {
                Response response = new Response(ResponseStatus.ERROR);
                response.setErrorMessage(Config.getConfig().getProperty(String.class, "cantAddCourseError"));
                findClientAndSendResponse(id, response);
            }
        }
    }

    private void handleRemoveRequest(int id, RequestFromServer request) {
        String type = (String) request.getData("type");
        if (type.equals("user")) {
            UserView userView = (UserView) request.getData("user");
            UserView doer = (UserView) request.getData("doer");
            if (userView instanceof ProfessorView) {
                if (!((ProfessorView) userView).isPrinciple()) {
                    if (!userView.getDepartment().equals(doer.getDepartment())) {
                        Response response = new Response(ResponseStatus.ERROR);
                        response.setErrorMessage(Config.getConfig().getProperty(String.class, "notSameDepartmentError"));
                        findClientAndSendResponse(id, response);
                    } else {
                        User user = Manage.getUserByUserName(userView.getUserName());
                        professors.remove(user);
                        Response response = new Response(ResponseStatus.OK);
                        response.addData("message", Config.getConfig().getProperty(String.class, "userRemoved"));
                        findClientAndSendResponse(id, response);
                    }
                } else {
                    Response response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "priRemoveError"));
                    findClientAndSendResponse(id, response);
                }
            } else {

            }
        } else {
            UserView doer1 = (UserView) request.getData("doer");
            Course course = (Course) request.getData("course");
            if (!doer1.getDepartment().equals(course.getDepartment())) {
                Response response = new Response(ResponseStatus.ERROR);
                response.setErrorMessage(Config.getConfig().getProperty(String.class, "courseDepartmentError"));
                findClientAndSendResponse(id, response);
            } else {
                Response response = new Response(ResponseStatus.OK);
                for (Student student : students) {
                    for (String course1 : student.getCourseList()) {
                        if (course1.equals(course.getCourseCode())) {
                            student.getCourseList().remove(course1);
                        }
                    }
                }
                for (Professor professor : professors) {
                    for (String course1 : professor.getCourseList()) {
                        if (course1.equals(course.getCourseCode())) {
                            professor.getCourseList().remove(course1);
                        }
                    }
                }
                courses.remove(course);
                response.addData("message", Config.getConfig().getProperty(String.class, "courseRemoved"));
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
        String type = (String) request.getData("type");
        if (type.equals("username")) {
            String username = (String) request.getData("username");
            User user = Manage.getUserByUserName(username);
            if (user == null) {
                Response response = new Response(ResponseStatus.ERROR);
                response.setErrorMessage(Config.getConfig().getProperty(String.class, "userNotFoundError"));
                findClientAndSendResponse(id, response);
            } else {
                Response response = new Response(ResponseStatus.OK);
                UserView userview = Manage.getUserViewByUserName(username);
                response.addData("user", userview);
                findClientAndSendResponse(id, response);
            }
        } else {
            String code = (String) request.getData("code");
            String tip = (String) request.getData("userType");
            if (tip.equals("prof")) {
                Professor professor = Manage.getProfessorByCode(code);
                if (professor == null) {
                    Response response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "invalidTeacherError"));
                    findClientAndSendResponse(id, response);
                } else {
                    Response response = new Response(ResponseStatus.OK);
                    ProfessorView userView = (ProfessorView) Manage.getUserViewByUserName(professor.getUserName());
                    response.addData("user", userView);
                    findClientAndSendResponse(id, response);
                }
            } else {

            }
        }
    }

    private void handleGetTableRequest(int id, RequestFromServer request) {
        String table = (String) request.getData("table");
        switch (table) {
            case "professors":
                Response response = new Response(ResponseStatus.OK);
                List<ProfessorView> professors = new ArrayList<>();
                for (Professor professor : Data.professors) {
                    ProfessorView professorView = (ProfessorView) Manage.getUserViewByUserName(professor.getUserName());
                    professors.add(professorView);
                }
                response.addData("table", professors);
                findClientAndSendResponse(id, response);
                break;
            case "courses":
                Response response1 = new Response(ResponseStatus.OK);
                List<Course> courses = new ArrayList<>();
                courses.addAll(Data.courses);
                response1.addData("table", courses);
                findClientAndSendResponse(id, response1);
                break;
        }
    }


    private void handleFilterRequest(int id, RequestFromServer request) {
        String table = (String) request.getData("table");
        switch (table) {
            case "professors":
                int room = Integer.parseInt((String) request.getData("roomNumber"));
                String department = (String) request.getData("department");
                String degree = (String) request.getData("degree");
                List<ProfessorView> profList = new ArrayList<>();
                for (Professor professor : professors) {
                    if (professor.getRoomNumber() == room &&
                            professor.getDegree().equals(degree) &&
                            professor.getDepartment().equals(department)) {
                        profList.add((ProfessorView) Manage.getUserViewByUserName(professor.getUserName()));
                    }
                }
                if (profList.isEmpty()) {
                    Response response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "noMatchError"));
                    findClientAndSendResponse(id, response);
                } else {
                    Response response = new Response(ResponseStatus.OK);
                    response.addData("table", profList);
                    findClientAndSendResponse(id, response);
                }
                break;
            case "courses":
                String name = (String) request.getData("name");
                String code = (String) request.getData("code");
                int unit = Integer.parseInt((String) request.getData("unit"));
                List<Course> courses = new ArrayList<>();
                for (Course course : Data.courses) {
                    if (course.getName().equals(name) && course.getCourseCode().equals(code) &&
                            course.getUnits() == unit) {
                        courses.add(course);
                    }
                }
                if (courses.isEmpty()) {
                    Response response = new Response(ResponseStatus.ERROR);
                    response.setErrorMessage(Config.getConfig().getProperty(String.class, "noMatchError"));
                    findClientAndSendResponse(id, response);
                } else {
                    Response response = new Response(ResponseStatus.OK);
                    response.addData("table", courses);
                    findClientAndSendResponse(id, response);
                }
                break;
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
            case "changePassword":
            case "professorList":
            case "courseList":
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
            case "week":
                break;
            case "status":
                break;
            case "profile":
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

    private void findClientAndSendResponse(int clientId, Response response) {
        ClientHandler clientHandler = getClientHandler(clientId);
        if (clientHandler != null) {
            clientHandler.sendResponse(response);
        }
    }
}
