package server.logic.data;

import shared.model.objects.Course;
import server.logic.objects.GradeRequest;
import server.logic.objects.Request;
import server.logic.users.Professor;
import server.logic.users.Student;
import server.logic.users.User;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<Student> students = new ArrayList<>();
    public static List<Professor> professors = new ArrayList<>();
    public static List<Course> courses = new ArrayList<>();
    public static List<GradeRequest> gradeRequests = new ArrayList<>();
    public static User selectedUser;
    public static Course selectedCourse;
    public static Request selectedRequest;
    public static String captcha;
}
