import Constraints.Constraint;

import java.util.Arrays;
import java.util.List;

public class QueryBuilder {
    private String currentResult;
    private SQLConnector connector;
    private Class<?> selectedType;

    public QueryBuilder(SQLConnector connector) {
        currentResult = "";
        this.connector = connector;
        selectedType = Object.class;
    }

    public QueryBuilder insert(Object object) {
        Class<?> c = object.getClass();
        String tableName = c.getSimpleName();
        StringBuilder script = new StringBuilder("INSERT INTO ");
        script
                .append(tableName)
                .append(" VALUES (");
        Arrays.stream(c.getDeclaredFields())
                .forEach(f -> {
                    try {
                        boolean isString = f.getType().equals(String.class);
                        if (isString) {
                            script.append("'");
                        }
                        script.append(f.get(object));
                        if (isString) {
                            script.append("'");
                        }
                        script.append(", ");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        script.delete(script.length() - 2, script.length() - 1);
        script.append(");");
        currentResult = script.toString();
        return this;
    }

    public QueryBuilder select(Class<?> c) {
        currentResult = "SELECT * FROM " + c.getSimpleName();
        selectedType = c;
        return this;
    }

    public QueryBuilder select() {
        currentResult = "SELECT * FROM (" + currentResult + ")";
        return this;
    }

    public QueryBuilder delete(Class<?> c) {
        currentResult = "DELETE FROM " + c.getSimpleName();
        return this;
    }

    public QueryBuilder where(Constraint constraint) {
        currentResult += " WHERE " + constraint.getResult();
        return this;
    }

    public List<Object> execute() {
        System.out.println(currentResult);
        if (currentResult.startsWith("SELECT")) {
            return connector.executeQuery(selectedType, currentResult);
        } else {
            return connector.execute(currentResult);
        }
    }
}
