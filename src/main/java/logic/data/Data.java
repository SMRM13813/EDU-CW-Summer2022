package logic.data;

import logic.objects.Course;
import logic.objects.GradeRequest;
import logic.objects.Request;
import logic.users.Professor;
import logic.users.Student;
import logic.users.User;

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
}
