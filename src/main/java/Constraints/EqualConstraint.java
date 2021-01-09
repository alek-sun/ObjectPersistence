package Constraints;

public class EqualConstraint implements Constraint {
    String lastResult;
    String propertyName;
    Object value;


    public EqualConstraint(String propertyName, Object value) {
        if (value instanceof String) {
            value = "'" + value + "'";
        }
        lastResult = propertyName + "=" + value.toString(); //todo
        this.propertyName = propertyName;
        this.value = value;
    }

    @Override
    public Constraint or(Constraint c) {
        lastResult = "(" + lastResult + " OR " + c.getResult() + ")";
        return this;
    }

    @Override
    public Constraint and(Constraint c) {
        lastResult = "(" + lastResult + " AND " + c.getResult() + ")";
        return this;
    }

    @Override
    public Constraint not() {
        lastResult = "NOT (" + lastResult + ")";
        return this;
    }

    @Override
    public String getResult() {
        return lastResult;
    }
}
