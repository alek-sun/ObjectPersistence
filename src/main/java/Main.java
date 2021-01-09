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
            Student s = new Student(3967, "Anna", 19);
            connector.insert(s);
            //connector.executeQuery("Student");
            List<Object> l = connector.getAllFromTable(Student.class);
            l.forEach(el -> System.out.println(((Student) el).name));
        }
        catch (Exception e) {

        }


    }
}

// todo annotation names to SQL
// todo ? create table script connect to DB (vk link)
// todo save built table