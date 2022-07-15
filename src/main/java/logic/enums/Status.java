package logic.enums;

public enum Status {
    STUDYING,
    GRADUATE,
    WITHDRAW;

    @Override
    public String toString() {
        if (this.equals(STUDYING)) {
            return "Studying";
        } else if (this.equals(GRADUATE)) {
            return "Graduate";
        } else if (this.equals(WITHDRAW)) {
            return "Withdraw";
        }
        return null;
    }
}
