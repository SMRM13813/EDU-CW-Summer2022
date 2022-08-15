package server.logic.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import shared.model.objects.Course;
import server.logic.objects.GradeRequest;
import server.logic.users.Professor;
import server.logic.users.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static server.logic.data.Data.*;

public class FileWork {

    private final static Logger logger = LogManager.getLogger(FileWork.class);

    public static void readFile() {
        Gson gsodn = new Gson();

        try (Reader reader = new FileReader("src/main/resources/Data/Jsons/students.json")) {

            Student[] staff = gsodn.fromJson(reader, Student[].class);

            students.clear();
            students.addAll(Arrays.asList(staff));

            logger.info("File read successfully(student)");

        } catch (IOException e) {
            logger.error("File not found(student)");
        }

        try (Reader reader = new FileReader("src/main/resources/Data/Jsons/professors.json")) {

            Professor[] profff = gsodn.fromJson(reader, Professor[].class);

            professors.clear();
            professors.addAll(Arrays.asList(profff));

            logger.info("File read successfully(professor)");

        } catch (IOException e) {
            logger.error("File not found(professor)");
        }

        try (Reader reader = new FileReader("src/main/resources/Data/Jsons/courses.json")) {

            Course[] cor = gsodn.fromJson(reader, Course[].class);

            courses.clear();
            courses.addAll(Arrays.asList(cor));

            logger.info("File read successfully(course)");


        } catch (IOException e) {
            logger.error("File not found(course)");
        }

        try (Reader reader = new FileReader("src/main/resources/Data/Jsons/requests.json")) {

            GradeRequest[] rg = gsodn.fromJson(reader, GradeRequest[].class);

            gradeRequests.clear();
            gradeRequests.addAll(Arrays.asList(rg));

            logger.info("File read successfully(grade)");

        } catch (IOException e) {
            logger.error("File not found(grade)");
        }
    }

    public static void writeFile() {

        String fileName = "src/main/resources/Data/Jsons/students.json";

        Path path = Paths.get(fileName);

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            Gson gsond = new GsonBuilder().create();

            JsonElement tree = gsond.toJsonTree(students);
            gsond.toJson(tree, writer);

            logger.info("File saved(student)");

        } catch (IOException e) {
            logger.error("Cant write in file(student)");
        }
        fileName = "src/main/resources/Data/Jsons/professors.json";
        path = Paths.get(fileName);

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            Gson gsond = new GsonBuilder().create();

            JsonElement tree = gsond.toJsonTree(professors);
            gsond.toJson(tree, writer);

            logger.info("File saved(professor)");

        } catch (IOException e) {
            logger.error("Cant write in file(professor)");
        }
        fileName = "src/main/resources/Data/Jsons/courses.json";
        path = Paths.get(fileName);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            Gson gsond = new GsonBuilder().create();

            JsonElement tree = gsond.toJsonTree(courses);
            gsond.toJson(tree, writer);

            logger.info("File saved(course)");

        } catch (IOException e) {
            logger.error("Cant write in file(course)");
        }

        fileName = "src/main/resources/Data/Jsons/requests.json";
        path = Paths.get(fileName);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            Gson gsond = new GsonBuilder().create();

            JsonElement tree = gsond.toJsonTree(gradeRequests);
            gsond.toJson(tree, writer);

            logger.info("File saved(grade)");

        } catch (IOException e) {
            logger.error("Cant write in file(grade)");
        }
    }

}
