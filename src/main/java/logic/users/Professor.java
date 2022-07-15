package logic.users;

import logic.enums.Degree;
import logic.enums.Department;
import logic.objects.Course;
import logic.objects.Request;

import java.io.FileNotFoundException;
import java.util.List;

public class Professor extends User {
    private String professorCode;
    private int roomNumber;
    private Degree degree;
    private boolean deanOfFaculty;
    private boolean principle;
    private List<Request> requestList;

    public Professor() {
    }

    public Professor(String userName, String passWord, String name, String lastName, String nationalCode,
                     String phoneNumber, String email, Department department, String image,
                     String professorCode, int roomNumber, Degree degree, boolean deanOfFaculty,
                     boolean principle, List<Course> courseList) throws FileNotFoundException {
        super(userName, passWord, name, lastName, nationalCode, phoneNumber, email, department, image, courseList);
        this.professorCode = professorCode;
        this.roomNumber = roomNumber;
        this.degree = degree;
        this.deanOfFaculty = deanOfFaculty;
        this.principle = principle;
        this.requestList = null;
    }

    public String getProfessorCode() {
        return professorCode;
    }

    public void setProfessorCode(String professorCode) {
        this.professorCode = professorCode;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDegree() {
        return degree.toString();
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public boolean isDeanOfFaculty() {
        return deanOfFaculty;
    }

    public void setDeanOfFaculty(boolean deanOfFaculty) {
        this.deanOfFaculty = deanOfFaculty;
    }

    public boolean isPrinciple() {
        return principle;
    }

    public void setPrinciple(boolean principle) {
        this.principle = principle;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

}
