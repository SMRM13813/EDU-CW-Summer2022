import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.logic.data.FileWork;
import server.logic.users.Professor;
import shared.model.enums.Degree;
import shared.model.enums.Department;
import shared.model.enums.Level;
import shared.model.enums.Status;
import server.logic.users.Student;
import shared.model.objects.Course;
import shared.model.objects.CourseStudent;
import shared.util.Config;

import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static server.logic.data.Data.*;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws FileNotFoundException {

        FileWork.readFile();
        logger.info("App started");
        Server server = new Server(Config.getConfig().getProperty(Integer.class, "serverPort"));
        server.start();
        logger.info("App closed");
        FileWork.writeFile();
    }

}
