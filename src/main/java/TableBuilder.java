import translation.JavaToSQLTranslator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Generates DB script to create table
class TableBuilder<T> {
    Class<T> javaTableType;
    String tableName;
    List<Field> declaredFields;
    List<String> builtTables;

    public TableBuilder(Class<T> type) {
        //todo check if type is annotated with Annotations.Table
        // todo check if Field Primitive
        this.javaTableType = type;
        tableName = type.getSimpleName();
        declaredFields = new ArrayList<>();
        declaredFields.addAll(Arrays.asList(type.getDeclaredFields()));
    }

    String buildCreateTableScript() {
        StringBuilder script = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS " + tableName + " (\n");
        declaredFields.forEach(
                field -> {
                    script
                            .append(field.getName())
                            .append(" ")
                            .append(JavaToSQLTranslator.typeToSQL(field.getType()));
                    Arrays.stream(field.getDeclaredAnnotations()).forEach(
                            annotation -> script
                                    .append(" ")
                                    .append(JavaToSQLTranslator.constraintsToSQL(
                                            annotation.annotationType())
                                    )
                    );
                    script.append(",\n");
                }
        );
        script.delete(script.length()-2,script.length()-1);
        script.append(");");
        System.out.println(script.toString());
        return script.toString();
    }

}