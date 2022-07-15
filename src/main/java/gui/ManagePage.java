package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.data.Manage;
import logic.objects.CourseSummery;
import logic.objects.Row;
import logic.objects.Course;
import logic.objects.GradeRequest;
import logic.users.Professor;
import logic.users.Student;

import java.io.IOException;
import java.util.List;

public class ManagePage {

    public static void changePage(Stage stage, String fxml) throws IOException {

        FXMLLoader loader = new FXMLLoader(ManagePage.class.getClassLoader().getResource(fxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static ObservableList<Row> getRows(List<Row> rows) {
        ObservableList<Row> rows1 = FXCollections.observableArrayList();
        rows1.addAll(rows);
        return rows1;
    }

    public static ObservableList<Row> getRowsTwo(Student student) {
        ObservableList<Row> row = FXCollections.observableArrayList();
        row.add(new Row("Academic status:", student.getStatus().toString()));
        row.add(new Row("Supervisor:", Manage.getProfessorByCode(student.getSupervisor()).getName() + " " +
                Manage.getProfessorByCode(student.getSupervisor()).getLastName()));
        row.add(new Row("Enrollment permission:", "Allowed"));
        row.add(new Row("Enrollment time", "10 feb 2023, 12:22:32"));
        return row;
    }

    public static ObservableList<Course> getRowsCourse(List<Course> courseList) {
        ObservableList<Course> cor = FXCollections.observableArrayList();
        cor.addAll(courseList);
        return cor;
    }

    public static ObservableList<Professor> getRowsProfessor(List<Professor> professorList) {
        ObservableList<Professor> por = FXCollections.observableArrayList();
        por.addAll(professorList);
        return por;
    }

    public static ObservableList<GradeRequest> getRowsGradeRequest(List<GradeRequest> gradeRequestList) {
        ObservableList<GradeRequest> gr = FXCollections.observableArrayList();
        gr.addAll(gradeRequestList);
        return gr;
    }

    public static ObservableList<CourseSummery> getRowsCourseSummery(List<CourseSummery> courseSummeries) {
        ObservableList<CourseSummery> cs = FXCollections.observableArrayList();
        cs.addAll(courseSummeries);
        return cs;
    }

}

