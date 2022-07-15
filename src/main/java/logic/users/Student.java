package logic.users;

import logic.enums.Department;
import logic.enums.Level;
import logic.enums.Status;
import logic.objects.Course;
import logic.objects.CourseStudent;
import logic.objects.Request;

import java.util.List;

public class Student extends User {

    private String studentNumber;
    private double averageGrade;
    private String supervisor;
    private int entryYear;
    private Level level;
    private Status status;
    List<CourseStudent> courseStudentList;
    private Request requestWithdraw;
    private Request requestDorm;
    private Request requestDefend;
    private Request requestMinor;
    private List<Request> requestRecommends;
    private boolean enrollmentCertificate;

    public Student() {

    }

    public Student(String userName, String passWord, String name, String lastName, String nationalCode,
                   String phoneNumber, String email, Department department, String studentNumber,
                   double averageGrade, String supervisor, int entryYear, String image, Level level,
                   Status status, List<Course> courseList, List<CourseStudent> courseStudentList) {
        super(userName, passWord, name, lastName, nationalCode, phoneNumber, email, department, image, courseList);
        this.studentNumber = studentNumber;
        this.averageGrade = averageGrade;
        this.supervisor = supervisor;
        this.entryYear = entryYear;
        this.level = level;
        this.enrollmentCertificate = false;
        this.status = status;
        this.requestWithdraw = null;
        this.requestDorm = null;
        this.requestDefend = null;
        this.requestMinor = null;
        this.requestRecommends = null;
        this.courseStudentList = courseStudentList;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isEnrollmentCertificate() {
        return enrollmentCertificate;
    }

    public void setEnrollmentCertificate(boolean hasEnrollmentCertificate) {
        this.enrollmentCertificate = hasEnrollmentCertificate;
    }

    public Request getRequestWithdraw() {
        return requestWithdraw;
    }

    public void setRequestWithdraw(Request requestWithdraw) {
        this.requestWithdraw = requestWithdraw;
    }

    public List<CourseStudent> getCourseStudentList() {
        return courseStudentList;
    }

    public void setCourseStudentList(List<CourseStudent> courseStudentList) {
        this.courseStudentList = courseStudentList;
    }

    public Request getRequestDorm() {
        return requestDorm;
    }

    public void setRequestDorm(Request requestDorm) {
        this.requestDorm = requestDorm;
    }

    public Request getRequestDefend() {
        return requestDefend;
    }

    public void setRequestDefend(Request requestDefend) {
        this.requestDefend = requestDefend;
    }

    public Request getRequestMinor() {
        return requestMinor;
    }

    public void setRequestMinor(Request requestMinor) {
        this.requestMinor = requestMinor;
    }

    public List<Request> getRequestRecommends() {
        return requestRecommends;
    }

    public void setRequestRecommends(List<Request> requestRecommends) {
        this.requestRecommends = requestRecommends;
    }
}

