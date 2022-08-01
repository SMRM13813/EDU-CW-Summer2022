package server.logic.objects;

public class CourseStudent {
    private String course;
    private Double grade;

    public CourseStudent(String course) {
        this.course = course;
        this.grade = (1.0) * (-1);
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
