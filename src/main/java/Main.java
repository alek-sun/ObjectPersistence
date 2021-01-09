import constraints.EqualConstraint;
import persistence.Student;
import queries.BaseQuery;
import queries.SQLConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            SQLConnector connector = new SQLConnector(connection);

            ProjectAnalyzer p = new ProjectAnalyzer("persistence", connector);
            p.analyzeProject();
            new BaseQuery(connector).delete(Student.class).execute();
            new BaseQuery(connector).insert(new Student(23, "Anna", 21)).execute();
            new BaseQuery(connector).insert(new Student(24, "Aya", 21)).execute();
            new BaseQuery(connector).insert(new Student(26, "Alla", 21)).execute();
            new BaseQuery(connector).insert(new Student(25, "Alice", 23)).execute();
            new BaseQuery(connector).insert(new Student(27, "Anna", 23)).execute();
            List<Object> all = new BaseQuery(connector).select(Student.class).execute();
            all.forEach(el -> {
                Student student = (Student) el;
                System.out.println(student.id + student.name + student.age);
            });
            //new BaseQuery(connector).delete(Student.class).where(new EqualConstraint("age", 23).not()).execute();

            List<Object> all2 = new BaseQuery(connector).select(Student.class).execute();
            all2.forEach(el -> {
                Student student = (Student) el;
                System.out.println(student.id + student.name + student.age);
            });

            List<Object> l = new BaseQuery(connector).select(Student.class).where(new EqualConstraint("age", 21)).select()
                    .where(new EqualConstraint("name", "Anna").or(new EqualConstraint("name", "Alla"))).execute();
            l.forEach(el -> {
                Student student = (Student) el;
                System.out.println(student.id + student.name + student.age);
            });
        } catch (Exception e) {

        }
    }
}