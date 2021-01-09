package constraints;

public class EqualConstraint extends Constraint {

    public EqualConstraint(String propertyName, Object value) {
        if (value instanceof String) {
            value = "'" + value + "'";
        }
        lastResult = propertyName + "=" + value.toString();
    }
}
