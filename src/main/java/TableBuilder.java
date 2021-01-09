import annotations.Column;
import annotations.ManyToOne;
import annotations.OneToOne;
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
                    if (field.isAnnotationPresent(OneToOne.class)) {
                        script
                                .append("FOREIGN KEY(")
                                .append(field.getAnnotation(OneToOne.class).referenced())
                                .append(") REFERENCES ")
                                .append(field.getType().getSimpleName())
                                .append("(")
                                .append(field.getAnnotation(OneToOne.class).value())
                                .append(")")
                                .append(",\n");
                    }
                    if (field.isAnnotationPresent(ManyToOne.class)) {
                        script
                                .append(field.getAnnotation(ManyToOne.class).name())
                                .append(" INTEGER,\n")
                                .append("FOREIGN KEY(")
                                .append(field.getAnnotation(ManyToOne.class).name())
                                .append(") REFERENCES ")
                                .append(field.getType().getSimpleName())
                                .append("(")
                                .append(field.getAnnotation(ManyToOne.class).referencesField())
                                .append(")")
                                .append(",\n");
                    }
                    if (field.isAnnotationPresent(Column.class)) {
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
                }
        );
        script.delete(script.length()-2,script.length()-1);
        script.append(");");
        System.out.println(script.toString() + "\n\n");
        return script.toString();
    }
}