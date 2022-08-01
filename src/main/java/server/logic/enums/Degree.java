package server.logic.enums;

public enum Degree {
    ASSISTANT_PROFESSOR,
    ASSOCIATE_PROFESSOR,
    FULL_PROFESSOR;

    @Override
    public String toString() {
        return switch (this) {
            case ASSISTANT_PROFESSOR -> "Assistant professor";
            case ASSOCIATE_PROFESSOR -> "Associate professor";
            case FULL_PROFESSOR -> "Full professor";
        };
    }
}
