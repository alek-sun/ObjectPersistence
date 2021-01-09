import translation.SQLToJavaTranslator;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLConnector {
    Connection connection;
    Statement statement;

    public SQLConnector(Connection connection) throws SQLException {
        this.connection = connection;
        statement = connection.createStatement();
        statement.setQueryTimeout(30);
    }

    public void createTableByScript(String script) {
        try {
            statement.executeUpdate(script);
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
/*        finally
        {
            try
            {
                if (connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }*/
    }

    void executeQuery(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select * from " + tableName);
            while (rs.next()) {
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
                System.out.println("age = " + rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void insert(Object object) {
        Class<?> c = object.getClass();
        String tableName = c.getSimpleName();
        StringBuilder script = new StringBuilder("INSERT INTO ");
        script.append(tableName).append(" VALUES (");
        try {
            Arrays.stream(c.getDeclaredFields()).forEach(f -> {
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
            System.out.println(script.toString());
            statement.executeUpdate(script.toString());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    void deleteTable(Class<?> c) {
        try {
            statement.executeUpdate("DROP TABLE " + c.getSimpleName());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    List<Object> getAllFromTable(Class<?> c) {
        List<Object> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("select * from " + c.getSimpleName());
            ResultSetMetaData rsmd = rs.getMetaData();
            int propertiesCount = rsmd.getColumnCount();
            Class<?>[] types = new Class[propertiesCount];
            for (int i = 1; i <= propertiesCount; i++) {
                types[i-1] = SQLToJavaTranslator.toJavaType(rsmd.getColumnType(i));
            }
            while (rs.next()) {
                Object[] values = new Object[propertiesCount];
                for (int i = 1; i <= propertiesCount; i++) {
                    values[i-1] = rs.getObject(i);
                }
                Object o = c.getConstructor(types).newInstance(values);
                result.add(o);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
    //select(Student.class).where(or(new LessThanConstraint(), new EqualConstraint())
}