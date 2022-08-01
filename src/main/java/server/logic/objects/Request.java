package server.logic.objects;


import server.logic.enums.Type;

public class Request {

    String student;
    String professor;
    Type type;
    int result;
    String text;

    public Request(String student, String professor, Type type) {
        this.student = student;
        this.professor = professor;
        this.type = type;
        this.result = 0;
        this.text = "-";
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
