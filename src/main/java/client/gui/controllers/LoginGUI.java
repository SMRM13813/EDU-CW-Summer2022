package client.gui.controllers;

import client.gui.ManagePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.logic.enums.Level;
import server.logic.enums.Status;
import server.logic.users.Professor;
import server.logic.users.Student;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.ResourceBundle;

import static server.logic.data.Data.selectedUser;
import static server.logic.data.Manage.getUserByUserName;
import static server.logic.data.Manage.userIsValid;

public class LoginGUI implements Initializable {

    private final static Logger logger = LogManager.getLogger(LoginGUI.class);

    @FXML
    Button loginButton;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField captcha;
    @FXML
    ImageView image;
    @FXML
    ImageView imageCap;
    @FXML
    Label userPassWarn;
    @FXML
    Label captchaWarn;
    Stage stage;
    String capt;
    String path;

    public void login(ActionEvent actionEvent) throws IOException {

        String name = usernameField.getText();
        String pass = passwordField.getText();
        String cap = captcha.getText();

        if (userIsValid(name, pass)) {

            if ((cap).equalsIgnoreCase(capt)) {

                stage = ((Stage) (((Node) actionEvent.getSource())).getScene().getWindow());
                selectedUser = getUserByUserName(name);

                assert selectedUser != null;
                if (selectedUser.getLastEntered() != null && ChronoUnit.HOURS.between(LocalDateTime.parse(selectedUser.getLastEntered()), LocalDateTime.now()) > 2) {
                    ManagePage.changePage(stage, "changePassword.fxml");
                } else {
                    if (selectedUser instanceof Student) {
                        if (((Student) selectedUser).getStatus().equals(Status.WITHDRAW)) {
                            logger.info("Student " + selectedUser.getLastName() + " can not enter(withdrawn)");
                            userPassWarn.setText("You have withdrawn form education!");
                        } else {
                            if (((Student) selectedUser).getLevel().equals(Level.BACHELOR)) {
                                logger.info("User " + selectedUser.getUserName() + " logged in");
                                ManagePage.changePage(stage, "bachelorStudentMainMenu.fxml");
                            } else if (((Student) selectedUser).getLevel().equals(Level.MASTER)) {
                                logger.info("User " + selectedUser.getUserName() + " logged in");
                                ManagePage.changePage(stage, "masterStudentMainMenu.fxml");
                            } else {
                                logger.info("User " + selectedUser.getUserName() + " logged in");
                                ManagePage.changePage(stage, "PhDStudentMainMenu.fxml");
                            }
                        }
                    } else if (selectedUser instanceof Professor) {
                        if (((Professor) selectedUser).isDeanOfFaculty()) {
                            logger.info("User " + selectedUser.getUserName() + " logged in");
                            ManagePage.changePage(stage, "DOFMainMenu.fxml");
                        } else {
                            logger.info("User " + selectedUser.getUserName() + " logged in");
                            ManagePage.changePage(stage, "professorMainMenu.fxml");
                        }
                    }
                }
            } else {
                captchaWarn.setText("captcha incorrect!");
                userPassWarn.setText("");
                resetCaptcha();
            }
        } else {
            userPassWarn.setText("Username/Password is wrong!");
            captchaWarn.setText("");
        }

    }

    public void resetCaptcha() {
        Random random = new Random();

        int randInt = random.nextInt();
        randInt %= 7;
        randInt = Math.abs(randInt);
        switch (randInt) {
            case 0 -> {
                path = "src/main/resources/Data/Images/Captcha/4nv3a.jpg";
                capt = "4nv3a";
            }
            case 1 -> {
                path = "src/main/resources/Data/Images/Captcha/9m4bp.jpg";
                capt = "9m4bp";
            }
            case 2 -> {
                path = "src/main/resources/Data/Images/Captcha/b4t9s.jpg";
                capt = "b4t9s";
            }
            case 3 -> {
                path = "src/main/resources/Data/Images/Captcha/cept6.jpg";
                capt = "cept6";
            }
            case 4 -> {
                path = "src/main/resources/Data/Images/Captcha/d35ua.jpg";
                capt = "d35ua";
            }
            case 5 -> {
                path = "src/main/resources/Data/Images/Captcha/mux2s.jpg";
                capt = "mux2s";
            }
            case 6 -> {
                path = "src/main/resources/Data/Images/Captcha/tk58p.jpg";
                capt = "tk58p";
            }
        }
        try {
            Image imagec = new Image(new FileInputStream(path));
            imageCap.setImage(imagec);
        } catch (FileNotFoundException e) {
            logger.error("Captcha can not be loaded");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        Random random = new Random();

        int randInt = random.nextInt();
        randInt %= 7;
        randInt = Math.abs(randInt);
        switch (randInt) {
            case 0 -> {
                path = "src/main/resources/Data/Image/Captcha/4nv3a.jpg";
                capt = "4nv3a";
            }
            case 1 -> {
                path = "src/main/resources/Data/Image/Captcha/9m4bp.jpg";
                capt = "9m4bp";
            }
            case 2 -> {
                path = "src/main/resources/Data/Image/Captcha/b4t9s.jpg";
                capt = "b4t9s";
            }
            case 3 -> {
                path = "src/main/resources/Data/Image/Captcha/cept6.jpg";
                capt = "cept6";
            }
            case 4 -> {
                path = "src/main/resources/Data/Image/Captcha/d35ua.jpg";
                capt = "d35ua";
            }
            case 5 -> {
                path = "src/main/resources/Data/Image/Captcha/mux2s.jpg";
                capt = "mux2s";
            }
            case 6 -> {
                path = "src/main/resources/Data/Image/Captcha/tk58p.jpg";
                capt = "tk58p";
            }
        }
        try {
            Image imagec = new Image(new FileInputStream(path));
            imageCap.setImage(imagec);
        } catch (FileNotFoundException e) {
            logger.error("Captcha can not be loaded");
        }

    }

    public void nextFieldP(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            passwordField.requestFocus();
        }
    }

    public void nextFieldC(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            captcha.requestFocus();
        }
    }

    public void loginC(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String name = usernameField.getText();
            String pass = passwordField.getText();
            String cap = captcha.getText();

            if (userIsValid(name, pass)) {
                if ((cap).equalsIgnoreCase(capt)) {

                    stage = ((Stage) (((Node) keyEvent.getSource())).getScene().getWindow());
                    selectedUser = getUserByUserName(name);

                    assert selectedUser != null;
                    if (selectedUser.getLastEntered() != null && ChronoUnit.HOURS.between(LocalDateTime.parse(selectedUser.getLastEntered()), LocalDateTime.now()) > 2) {
                        ManagePage.changePage(stage, "changePassword.fxml");
                    } else {
                        if (selectedUser instanceof Student) {
                            if (((Student) selectedUser).getStatus().equals(Status.WITHDRAW)) {
                                logger.info("Student with username" + selectedUser.getUserName() + " can not enter(withdrawn)");
                                userPassWarn.setText("You have withdrawn form education!");
                            } else {
                                if (((Student) selectedUser).getLevel().equals(Level.BACHELOR)) {
                                    logger.info("User " + selectedUser.getUserName() + " logged in");
                                    ManagePage.changePage(stage, "Data/bachelorStudentMainMenu.fxml");
                                } else if (((Student) selectedUser).getLevel().equals(Level.MASTER)) {
                                    logger.info("User " + selectedUser.getUserName() + " logged in");
                                    ManagePage.changePage(stage, "Data/masterStudentMainMenu.fxml");
                                } else {
                                    logger.info("User " + selectedUser.getUserName() + " logged in");
                                    ManagePage.changePage(stage, "Data/PhDStudentMainMenu.fxml");
                                }
                            }
                        } else if (selectedUser instanceof Professor) {
                            if (((Professor) selectedUser).isDeanOfFaculty()) {
                                logger.info("User " + selectedUser.getUserName() + " logged in");
                                ManagePage.changePage(stage, "Data/DOFMainMenu.fxml");
                            } else {
                                logger.info("User " + selectedUser.getUserName() + " logged in");
                                ManagePage.changePage(stage, "Data/professorMainMenu.fxml");
                            }
                        }
                    }
                } else {
                    captchaWarn.setText("captcha incorrect!");
                    userPassWarn.setText("");
                    resetCaptcha();
                }
            } else {
                userPassWarn.setText("Username/Password is wrong!");
                captchaWarn.setText("");
            }
        }
    }

}

