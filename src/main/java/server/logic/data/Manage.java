package server.logic.data;

import shared.model.enums.Degree;
import shared.model.enums.Department;
import shared.model.objects.Course;
import shared.model.objects.CourseStudent;
import server.logic.users.Professor;
import server.logic.users.Student;
import server.logic.users.User;
import shared.model.users.ProfessorView;
import shared.model.users.StudentView;
import shared.model.users.UserView;
import shared.util.Config;

import java.util.Random;

public class Manage {

    public static boolean userIsValid(String name, String pass) {
        for (Professor professor : Data.professors) {
            if (professor.getUserName().equals(name) && professor.getPassWord().equals(pass)) {
                return true;
            }
        }
        for (Student student : Data.students) {
            if (student.getUserName().equals(name) && student.getPassWord().equals(pass)) {
                return true;
            }
        }
        return false;
    }


    public static String setCaptcha() {
        String path = null;
        Random random = new Random();

        int randInt = random.nextInt();
        randInt %= 7;
        randInt = Math.abs(randInt);
        switch (randInt) {
            case 0 -> {
                path = Config.getConfig().getProperty(String.class, "captcha#1");
            }
            case 1 -> {
                path = Config.getConfig().getProperty(String.class, "captcha#2");
            }
            case 2 -> {
                path = Config.getConfig().getProperty(String.class, "captcha#3");
            }
            case 3 -> {
                path = Config.getConfig().getProperty(String.class, "captcha#4");
            }
            case 4 -> {
                path = Config.getConfig().getProperty(String.class, "captcha#5");
            }
            case 5 -> {
                path = Config.getConfig().getProperty(String.class, "captcha#6");
            }
            case 6 -> {
                path = Config.getConfig().getProperty(String.class, "captcha#7");
            }
        }
        return path;
    }

    public static User getUserByUserName(String name) {

        User user;
        for (Student student : Data.students) {
            if (student.getUserName().equals(name)) {
                user = student;
                return user;
            }
        }
        for (Professor professor : Data.professors) {
            if (professor.getUserName().equals(name)) {
                user = professor;
                return user;
            }
        }
        System.out.println("not found!");
        return null;
    }

    public static Course getCourseByCode(String code) {
        for (Course course : Data.courses) {
            if (course.getCourseCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    public static Course getCourseByName(String name) {
        for (Course course : Data.courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null;
    }

    public static Student getStudentByCode(String s) {
        for (Student student : Data.students) {
            if (student.getStudentNumber().equals(s)) {
                return student;
            }
        }
        return null;
    }

    public static Student getStudentByName(String s) {
        for (Student student : Data.students) {
            if (student.getLastName().equals(s)) {
                return student;
            }
        }
        return null;
    }

    public static Professor getProfessorByCode(String s) {
        for (Professor professor : Data.professors) {
            if (professor.getProfessorCode().equals(s)) {
                return professor;
            }
        }
        return null;
    }

    public static Professor getProfessorByName(String s) {
        for (Professor professor : Data.professors) {
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
        for (Professor professor1 : Data.professors) {
            if (professor1.getUserName().equals(professor.getUserName()) ||
                    professor1.getNationalCode().equals(professor.getNationalCode()) ||
                    professor1.getProfessorCode().equals(professor.getProfessorCode())) {
                return false;
            }
        }
        for (Student student : Data.students) {
            if (student.getUserName().equals(professor.getUserName())) {
                return false;
            }
        }
        Data.professors.add(professor);
        return true;
    }

    public static boolean addStud(Student student) {
        for (Student student1 : Data.students) {
            if (student1.getUserName().equals(student.getUserName()) ||
                    student1.getNationalCode().equals(student.getNationalCode()) ||
                    student1.getStudentNumber().equals(student.getStudentNumber())) {
                return false;
            }
        }
        for (Professor professor : Data.professors) {
            if (professor.getUserName().equals(student.getUserName())) {
                return false;
            }
        }
        Data.students.add(student);
        return true;
    }

    public static boolean addCor(Course course) {
        for (Course course1 : Data.courses) {
            if (course1.getCourseCode().equals(course.getCourseCode())) {
                return false;
            }
        }
        Data.courses.add(course);
        return true;
    }

    public static Double computeAverageDegree(Student student) {
        double average = 0;
        double units = 0;
        if (student.getCourseStudentList() != null) {
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

    public static User getUserByUserView(UserView userView, String password) {
        if (userView instanceof StudentView) {
            Student student = new Student();
            StudentView studentView = (StudentView) userView;
            student.setUserName(studentView.getUserName());
            student.setPassWord(password);
            student.setImage(Config.getConfig().getProperty(String.class, "defaultImage"));
            student.setName(studentView.getName());
            student.setLastName(studentView.getLastName());
            student.setNationalCode(studentView.getNationalCode());
            student.setPhoneNumber(studentView.getPhoneNumber());
            student.setEmail(studentView.getEmail());
            student.setStudentNumber(studentView.getStudentNumber());
            student.setStatus(studentView.getStatus());
            student.setSupervisor(studentView.getSupervisor());
            student.setEntryYear(studentView.getEntryYear());
            student.setAverageGrade(studentView.getAverageGrade());
            student.setLevel(studentView.getLevel());
            student.setLastEntered(studentView.getLastEntered());
            student.setCourseList(studentView.getCourseList());
            student.setCourseStudentList(studentView.getCourseStudentList());
            student.setEnrollmentCertificate(studentView.isEnrollmentCertificate());
            student.setDepartment(Manage.StringToDep(studentView.getDepartment()));
            student.setRequestDefend(studentView.getRequestDefend());
            student.setRequestDorm(studentView.getRequestDorm());
            student.setRequestMinor(studentView.getRequestMinor());
            student.setRequestRecommends(studentView.getRequestRecommends());
            student.setRequestWithdraw(studentView.getRequestWithdraw());
            return student;
        } else {
            Professor professor = new Professor();
            ProfessorView professorView = (ProfessorView) userView;
            professor.setUserName(professorView.getUserName());
            professor.setPassWord(password);
            professor.setImage(Config.getConfig().getProperty(String.class, "defaultImage"));
            professor.setName(professorView.getName());
            professor.setLastName(professorView.getLastName());
            professor.setNationalCode(professorView.getNationalCode());
            professor.setPhoneNumber(professorView.getPhoneNumber());
            professor.setEmail(professorView.getEmail());
            professor.setProfessorCode(professorView.getProfessorCode());
            professor.setDegree(Manage.StringToDeg(professorView.getDegree()));
            professor.setLastEntered(professorView.getLastEntered());
            professor.setCourseList(professorView.getCourseList());
            professor.setDepartment(Manage.StringToDep(professorView.getDepartment()));
            professor.setDeanOfFaculty(professorView.isDeanOfFaculty());
            professor.setPrinciple(professorView.isPrinciple());
            professor.setRoomNumber(professorView.getRoomNumber());
            professor.setRequestList(professorView.getRequestList());
            return professor;
        }
    }

    public static UserView getUserViewByUserName(String username) {
        User user = getUserByUserName(username);
        if (user instanceof Student) {
            StudentView studentv = new StudentView();
            Student student = (Student) user;
            studentv.setUserName(student.getUserName());
            studentv.setName(student.getName());
            studentv.setLastName(student.getLastName());
            studentv.setNationalCode(student.getNationalCode());
            studentv.setPhoneNumber(student.getPhoneNumber());
            studentv.setEmail(student.getEmail());
            studentv.setStudentNumber(student.getStudentNumber());
            studentv.setStatus(student.getStatus());
            studentv.setSupervisor(student.getSupervisor());
            studentv.setEntryYear(student.getEntryYear());
            studentv.setAverageGrade(student.getAverageGrade());
            studentv.setLevel(student.getLevel());
            studentv.setLastEntered(student.getLastEntered());
            studentv.setCourseList(student.getCourseList());
            studentv.setCourseStudentList(student.getCourseStudentList());
            studentv.setEnrollmentCertificate(student.isEnrollmentCertificate());
            studentv.setDepartment(student.getDepartment());
            studentv.setRequestDefend(student.getRequestDefend());
            studentv.setRequestDorm(student.getRequestDorm());
            studentv.setRequestMinor(student.getRequestMinor());
            studentv.setRequestRecommends(student.getRequestRecommends());
            studentv.setRequestWithdraw(student.getRequestWithdraw());
            return studentv;
        } else {
            ProfessorView professorv = new ProfessorView();
            Professor professor = (Professor) user;
            professorv.setUserName(professor.getUserName());
            professorv.setName(professor.getName());
            professorv.setLastName(professor.getLastName());
            professorv.setNationalCode(professor.getNationalCode());
            professorv.setPhoneNumber(professor.getPhoneNumber());
            professorv.setEmail(professor.getEmail());
            professorv.setProfessorCode(professor.getProfessorCode());
            professorv.setDegree(professor.getDegree());
            professorv.setLastEntered(professor.getLastEntered());
            professorv.setCourseList(professor.getCourseList());
            professorv.setDepartment(professor.getDepartment());
            professorv.setDeanOfFaculty(professor.isDeanOfFaculty());
            professorv.setPrinciple(professor.isPrinciple());
            professorv.setRoomNumber(professor.getRoomNumber());
            professorv.setRequestList(professor.getRequestList());
            return professorv;
        }
    }

}