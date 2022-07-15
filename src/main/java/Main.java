import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.enums.Department;
import logic.enums.Level;
import logic.enums.Status;
import logic.users.Student;
import org.apache.logging.log4j.*;
import java.io.*;

import static logic.data.Data.students;

public class Main extends Application {

    private final static Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args){

        //Making users:

        Student student = new Student("salad", "1234", "asf", "asdf", "02131", "0912323",
                "dasf@adsf", Department.MATHEMATICS, "400101231", 18.35, "Azizi", 2019,
                "", Level.BACHELOR, Status.STUDYING, null, null);
        students.add(student);

        logger.info("App started");
        launch(args);
        logger.info("App closed");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Data/Fxmls/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("EDU");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(720);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
