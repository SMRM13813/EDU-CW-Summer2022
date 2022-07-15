package logic.users;

import logic.enums.Department;
import logic.objects.Course;

import java.util.List;


public class User {
    private String userName;
    private String passWord;
    private String name;
    private String lastName;
    private String nationalCode;
    private String phoneNumber;
    private String email;
    private Department department;
    private String image;
    String lastEntered;
    private List<Course> courseList;

    public User() {

    }

    public User(String userName, String passWord, String name, String lastName, String nationalCode,
                String phoneNumber, String email, Department department, String image, List<Course> courseList) {

        this.userName = userName;
        this.passWord = passWord;
        this.name = name;
        this.lastName = lastName;
        this.nationalCode = nationalCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.department = department;
        this.image = image;
        this.lastEntered = null;
        this.courseList = courseList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department.toString();
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastEntered() {
        return lastEntered;
    }

    public void setLastEntered(String lastEntered) {
        this.lastEntered = lastEntered;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

}
