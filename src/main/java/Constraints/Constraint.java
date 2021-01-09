package Constraints;

public interface Constraint {
    Constraint or(Constraint c);
    Constraint and(Constraint c);
    Constraint not();
    String getResult();
}
