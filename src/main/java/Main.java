import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.logic.users.Professor;
import shared.model.enums.Degree;
import shared.model.enums.Department;
import shared.model.enums.Level;
import shared.model.enums.Status;
import server.logic.users.Student;
import shared.util.Config;

import java.io.FileNotFoundException;

import static server.logic.data.Data.professors;
import static server.logic.data.Data.students;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws FileNotFoundException {

        //Making users:
        //TODO

        Professor professor = new Professor("goosale", "1234", "vaghvagho", "ahmagh", "234243", "4323",
                "goosale@ahmagh", Department.PHYSICS, "src/main/resources/Data/Images/Professors/professor-3.png", "43253", 324, Degree.FULL_PROFESSOR, false, false, null);


        Professor professor1 = new Professor("aleh", "1234", "ablah", "afsh", "23426343", "4323",
                "aleh@ahmagh", Department.PHYSICS, "src/main/resources/Data/Images/Professors/professor-4.png", "13253", 334, Degree.FULL_PROFESSOR, true, false, null);


        Professor professor2 = new Professor("babai", "1234", "maymoon", "afas", "235426343", "4323",
                "baba@ahmagh", Department.MECHANICAL_ENGINEERING, "src/main/resources/Data/Images/Professors/professor-2.png", "12653", 134, Degree.ASSOCIATE_PROFESSOR, false, true, null);


        professors.add(professor);
        professors.add(professor1);
        professors.add(professor2);

        Student student = new Student("salad", "1234", "asf", "asdf", "02131", "0912323",
                "dasf@adsf", Department.MATHEMATICS, "400101231", 18.35, professor.getProfessorCode(), 2019,
                "src/main/resources/Data/Images/Students/student-3.png", Level.BACHELOR, Status.GRADUATE, null, null);
        students.add(student);

        logger.info("App started");
        Server server = new Server(Config.getConfig().getProperty(Integer.class, "serverPort"));
        server.start();
        logger.info("App closed");
    }

}
