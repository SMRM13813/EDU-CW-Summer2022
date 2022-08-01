package server.logic.objects;

import server.logic.data.Manage;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;


public class Course {

    private String name;
    private String courseCode;
    private int units;
    private Department department;
    private String teacher;
    private String teacherName;
    private boolean gradeSet;
    private boolean gradeFinalized;
    private String start;
    private String finish;
    private String examTime;
    private Set<DayOfWeek> days;
    private List<String> attendence;

    public Course(String name, String courseCode, int units, Department department, String teacher, String start
            , String finish, Set<DayOfWeek> days, String examTime, List<String> attendence) {
        this.name = name;
        this.courseCode = courseCode;
        this.units = units;
        this.department = department;
        this.teacher = teacher;
        this.start = start;
        this.finish = finish;
        this.days = days;
        this.examTime = examTime;
        this.attendence = attendence;
        this.teacherName = "Dr." + Manage.getProfessorByCode(teacher).getName() + " " + Manage.getProfessorByCode(teacher).getLastName();
        this.gradeSet = false;
        this.gradeFinalized = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDepartment() {
        return department.toString();
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<String> getAttendence() {
        return attendence;
    }

    public void setAttendence(List<String> attendence) {
        this.attendence = attendence;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getDays() {
        return days.toString();
    }

    public void setDays(Set<DayOfWeek> days) {
        this.days = days;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public boolean isGradeSet() {
        return gradeSet;
    }

    public void setGradeSet(boolean gradeSet) {
        this.gradeSet = gradeSet;
    }

    public boolean isGradeFinalized() {
        return gradeFinalized;
    }

    public void setGradeFinalized(boolean gradeFinalized) {
        this.gradeFinalized = gradeFinalized;
    }
}
