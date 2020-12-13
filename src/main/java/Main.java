import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //Student s = new Student("t", "name");
/*        TableBuilder<Student> b = new TableBuilder<>(Student.class);
        b.translation();*/

    }
}

@Table
class Student {
    @PrimaryKey
    @NotNull
    Integer id;

    @NotNull
    String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}

@Table
class Subject {
    @PrimaryKey
    Integer id;

    @NotNull
    String name;
}

// Generate DB script to create table
class TableBuilder<T> {
    Class<T> javaTableType;
    String tableName;
    List<Field> declaredFields;
    List<String> builtTables;

    public TableBuilder(Class<T> type) {
        //todo check if type is annotated with Table
        this.javaTableType = type;
        tableName = type.getName();
        declaredFields = new ArrayList<>();
        declaredFields.addAll(Arrays.asList(type.getDeclaredFields()));
    }

    void translation() {
        StringBuilder script = new StringBuilder("CREATE TABLE " + tableName + " (\n");
        declaredFields.forEach(
                field -> {
                    script
                            .append(field.getName())
                            .append(" ")
                            .append(JavaToSQLTranslator.toSQL(field.getType()))
                            .append(" ");
                    Arrays.stream(field.getDeclaredAnnotations()).forEach(
                            annotation -> script
                                    .append(annotation.annotationType().getName())
                                    .append(" ")
                    ); // add toSQL
                    script.append(",\n");
                }
        );
        script.append(");");
        System.out.println(script.toString());
    }

}
// todo annotation names to SQL
// todo ? create table script connect to DB (vk link)
// todo save built table