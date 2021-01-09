import Constraints.EqualConstraint;
import persistence.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            SQLConnector connector = new SQLConnector(connection);
            connector.deleteTable(Student.class);

            ProjectAnalyzer p = new ProjectAnalyzer("persistence", connector);
            p.run(); // possible parameter if drop existing table

            connector.delete(Student.class);

            // q.delete(Student.class).where(new EqualConstraint("id", 6).or(new EqualConstrint("name", "Ann")));
            // q.select(Student.class).where(..).select().where();*/
            System.out.println(
                    new EqualConstraint("id", 6).or(new EqualConstraint("name", "Ann")).and(new EqualConstraint("id", 434)).not()
                            .getResult());

            Student s = new Student(75, "Ann", 20);
            Student s2 = new Student(57, "Nina", 20);
            Student s3 = new Student(6, "Nina", 20);
            Student s4 = new Student(699, "Ira", 15);

            QueryBuilder q = new QueryBuilder(connector);
            q.insert(s).execute();
            QueryBuilder q1 = new QueryBuilder(connector);
            q.insert(s2).execute();
            QueryBuilder q2 = new QueryBuilder(connector);
            q.insert(s3).execute();
            QueryBuilder q3 = new QueryBuilder(connector);
            q.insert(s4).execute();

            QueryBuilder q5 = new QueryBuilder(connector);
            List<Object> all = q5.select(Student.class).execute();
            all.forEach(el -> {
                Student student = (Student) el;
                System.out.println(student.id + student.name + student.age);
            });

            QueryBuilder q6 = new QueryBuilder(connector);
            q6.delete(Student.class).where(new EqualConstraint("age", 15).not()).execute();

            List<Object> all2 = q6.select(Student.class).execute();
            all2.forEach(el -> {
                Student student = (Student) el;
                System.out.println(student.id + student.name + student.age);
            });

            QueryBuilder query = new QueryBuilder(connector);
            List<Object> l = query.select(Student.class).where(new EqualConstraint("age", 20)).select()
                    .where(new EqualConstraint("name", "Ann").or(new EqualConstraint("name", "Nina"))).execute();
            l.forEach(el -> {
                Student student = (Student) el;
                System.out.println(student.id + student.name + student.age);
            });
        } catch (Exception e) {

        }


    }
}