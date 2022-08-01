package server.logic.objects;

import server.logic.data.Manage;

public class GradeRequest {
    private String courseName;
    private String courseCode;
    private String requestText;
    private String response;
    private Double grade;
    private Double lastGrade;
    private String student;

    public GradeRequest(String courseName, String courseCode, String requestText, Double grade, Double lastGrade, String student, String response) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.requestText = requestText;
        this.grade = grade;
        this.student = student;
        this.response = response;
        this.lastGrade = lastGrade;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Double getLastGrade() {
        return lastGrade;
    }

    public void setLastGrade(Double lastGrade) {
        this.lastGrade = lastGrade;
    }

    public String getStudent() {
        return Manage.getStudentByCode(student).getName() + " " + Manage.getStudentByCode(student).getLastName();
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
