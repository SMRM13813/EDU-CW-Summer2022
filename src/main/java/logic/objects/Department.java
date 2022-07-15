package logic.objects;

import logic.users.Professor;

import java.util.ArrayList;
import java.util.List;

public class Department {

    private enum Name {
        MATHEMATICS,
        PHYSICS,
        MECHANICAL_ENGINEERING,
        INDUSTRIAL_ENGINEERING,
        ELECTRICAL_ENGINEERING
    }

    private Professor deanOfFaculty;
    private Professor principle;
    private List<Course> courses = new ArrayList<>();

    public Professor getDeanOfFaculty() {
        return deanOfFaculty;
    }

    public void setDeanOfFaculty(Professor deanOfFaculty) {
        this.deanOfFaculty = deanOfFaculty;
    }

    public Professor getPrinciple() {
        return principle;
    }

    public void setPrinciple(Professor principle) {
        this.principle = principle;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
