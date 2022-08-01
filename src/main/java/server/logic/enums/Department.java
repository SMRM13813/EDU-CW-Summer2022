package server.logic.enums;

public enum Department {
    MATHEMATICS,
    PHYSICS,
    MECHANICAL_ENGINEERING,
    INDUSTRIAL_ENGINEERING,
    ELECTRICAL_ENGINEERING;

    @Override
    public String toString() {
        return switch (this) {
            case MATHEMATICS -> "Mathematical Science";
            case MECHANICAL_ENGINEERING -> "Mechanical engineering";
            case PHYSICS -> "Physics";
            case INDUSTRIAL_ENGINEERING -> "Industrial engineering";
            case ELECTRICAL_ENGINEERING -> "Electrical engineering";
        };
    }
}
