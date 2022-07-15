package logic.data;

import logic.enums.Degree;
import logic.enums.Department;
import logic.objects.Course;
import logic.objects.CourseStudent;
import logic.users.Professor;
import logic.users.Student;
import logic.users.User;

import static logic.data.Data.*;

public class Manage {

    public static boolean userIsValid(String name, String pass) {
        for (Professor professor : professors) {
            if (professor.getUserName().equals(name) && professor.getPassWord().equals(pass)) {
                return true;
            }
        }
        for (Student student : students) {
            if (student.getUserName().equals(name) && student.getPassWord().equals(pass)) {
                return true;
            }
        }
        return false;
    }


    public static User getUserByUserName(String name) {

        User user;
        for (Student student : students) {
            if (student.getUserName().equals(name)) {
                user = student;
                return user;
            }
        }
        for (Professor professor : professors) {
            if (professor.getUserName().equals(name)) {
                user = professor;
                return user;
            }
        }
        return null;
    }

    public static Course getCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    public static Course getCourseByName(String name) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null;
    }

    public static Student getStudentByCode(String s) {
        for (Student student : students) {
            if (student.getStudentNumber().equals(s)) {
                return student;
            }
        }
        return null;
    }

    public static Student getStudentByName(String s) {
        for (Student student : students) {
            if (student.getLastName().equals(s)) {
                return student;
            }
        }
        return null;
    }

    public static Professor getProfessorByCode(String s) {
        for (Professor professor : professors) {
            if (professor.getProfessorCode().equals(s)) {
                return professor;
            }
        }
        return null;
    }

    public static Professor getProfessorByName(String s) {
        for (Professor professor : professors) {
            if (professor.getLastName().equals(s)) {
                return professor;
            }
        }
        return null;
    }

    public static Degree StringToDeg(String s) {
        return switch (s) {
            case "Assistant professor" -> Degree.ASSISTANT_PROFESSOR;
            case "Associate professor" -> Degree.ASSOCIATE_PROFESSOR;
            case "Full professor" -> Degree.FULL_PROFESSOR;
            default -> null;
        };
    }

    public static Department StringToDep(String s) {
        return switch (s) {
            case "Mathematical Science" -> Department.MATHEMATICS;
            case "Mechanical engineering" -> Department.MECHANICAL_ENGINEERING;
            case "Physics" -> Department.PHYSICS;
            case "Industrial engineering" -> Department.INDUSTRIAL_ENGINEERING;
            case "Electrical engineering" -> Department.ELECTRICAL_ENGINEERING;
            default -> null;
        };
    }

    public static boolean addProf(Professor professor) {
        for (Professor professor1 : professors) {
            if (professor1.getUserName().equals(professor.getUserName()) ||
                    professor1.getNationalCode().equals(professor.getNationalCode()) ||
                    professor1.getProfessorCode().equals(professor.getProfessorCode())) {
                return false;
            }
        }
        for(Student student : students){
            if(student.getUserName().equals(professor.getUserName())){
                return false;
            }
        }
        professors.add(professor);
        return true;
    }

    public static boolean addStud(Student student) {
        for (Student student1 : students) {
            if (student1.getUserName().equals(student.getUserName()) ||
                    student1.getNationalCode().equals(student.getNationalCode()) ||
                    student1.getStudentNumber().equals(student.getStudentNumber())) {
                return false;
            }
        }
        for(Professor professor : professors){
            if(professor.getUserName().equals(student.getUserName())){
                return false;
            }
        }
        students.add(student);
        return true;
    }

    public static boolean addCor(Course course) {
        for (Course course1 : courses) {
            if (course1.getCourseCode().equals(course.getCourseCode())) {
                return false;
            }
        }
        courses.add(course);
        return true;
    }

    public static Double computeAverageDegree(Student student) {
        double average = 0;
        double units = 0;
        if(student.getCourseStudentList() != null) {
            for (CourseStudent courseStudent : student.getCourseStudentList()) {
                if (courseStudent.getGrade() != -1) {
                    units += Manage.getCourseByCode(courseStudent.getCourse()).getUnits();
                }
            }
            for (CourseStudent courseStudent : student.getCourseStudentList()) {
                if (courseStudent.getGrade() != -1) {
                    average += ((Manage.getCourseByCode(courseStudent.getCourse()).getUnits()) / units) * courseStudent.getGrade();
                }
            }
        }
        return average;
    }

}
