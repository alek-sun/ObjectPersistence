package queries;

import annotations.Column;

import java.util.Arrays;

public class BaseQuery {
    SQLConnector connector;

    public BaseQuery(SQLConnector connector) {
        this.connector = connector;
    }

    public InsertQuery insert(Object object) {
        Class<?> c = object.getClass();
        String tableName = c.getSimpleName();
        StringBuilder script = new StringBuilder("INSERT INTO ");
        script
                .append(tableName)
                .append(" VALUES (");
        Arrays.stream(c.getDeclaredFields())
                .forEach(f -> {
                    if (f.isAnnotationPresent(Column.class)) {
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
                    }
                });
        script.delete(script.length() - 2, script.length() - 1);
        script.append(");");
        return new InsertQuery(script.toString(), connector);
    }

    public SelectQuery select(Class<?> c) {
        return new SelectQuery("SELECT * FROM " + c.getSimpleName(), c, connector);
    }


    public DeleteQuery delete(Class<?> c) {
        return new DeleteQuery("DELETE FROM " + c.getSimpleName(), connector);
    }
}
