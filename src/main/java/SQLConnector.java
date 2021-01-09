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
/*
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
    }*/

    public List<Object> execute(String script) {
        try {
            statement.executeUpdate(script);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    void deleteTable(Class<?> c) {
        try {
            statement.executeUpdate("DROP TABLE " + c.getSimpleName());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    List<Object> executeQuery(Class<?> c, String script) {
        try {
            ResultSet rs = statement.executeQuery(script);
            return instantiateResults(c, rs);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    void delete(Class<?> c) {
        try {
            statement.executeUpdate("DELETE FROM " + c.getSimpleName() + " WHERE id = 67");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private List<Object> instantiateResults(Class<?> c, ResultSet rs) {
        List<Object> result = new ArrayList<>();
        try {
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
}