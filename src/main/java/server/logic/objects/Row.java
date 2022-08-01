package server.logic.objects;

public class Row {
    private String firstRow;
    private String secondRow;
    private String thirdRow;

    public Row(String firstRow, String secondRow) {
        this.firstRow = firstRow;
        this.secondRow = secondRow;
    }

    public Row(String firstRow, String secondRow, String thirdRow) {
        this.firstRow = firstRow;
        this.secondRow = secondRow;
        this.thirdRow = thirdRow;
    }

    public String getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(String firstRow) {
        this.firstRow = firstRow;
    }

    public String getSecondRow() {
        return secondRow;
    }

    public void setSecondRow(String secondRow) {
        this.secondRow = secondRow;
    }

    public String getThirdRow() {
        return thirdRow;
    }

    public void setThirdRow(String thirdRow) {
        this.thirdRow = thirdRow;
    }
}

