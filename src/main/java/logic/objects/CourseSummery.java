package logic.objects;

public class CourseSummery {

    String courseName;
    double average;
    double pureAverage;
    int passed;
    int fell;

    public CourseSummery(String courseName, double average, double pureAverage, int passed, int fell) {
        this.courseName = courseName;
        this.average = average;
        this.pureAverage = pureAverage;
        this.passed = passed;
        this.fell = fell;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getPureAverage() {
        return pureAverage;
    }

    public void setPureAverage(double pureAverage) {
        this.pureAverage = pureAverage;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getFell() {
        return fell;
    }

    public void setFell(int fell) {
        this.fell = fell;
    }
}
