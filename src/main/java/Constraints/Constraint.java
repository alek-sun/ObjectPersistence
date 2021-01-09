package Constraints;

public class Constraint {
    public String lastResult;

    public Constraint or(Constraint c) {
        lastResult = "(" + lastResult + " OR " + c.getResult() + ")";
        return this;
    }

    public Constraint and(Constraint c) {
        lastResult = "(" + lastResult + " AND " + c.getResult() + ")";
        return this;
    }

    public Constraint not() {
        lastResult = "NOT (" + lastResult + ")";
        return this;
    }

    public String getResult() {
        return lastResult;
    }
}
