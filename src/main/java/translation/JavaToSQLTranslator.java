package translation;

import annotations.Column;
import annotations.NotNull;
import annotations.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

public class JavaToSQLTranslator {
    private static final Map<Class<?>, String> typeTable = new HashMap<>();
    private static final Map<Class<?>, String> constrainsTable = new HashMap<>();
    static {
        typeTable.put(String.class, "TEXT");
        typeTable.put(Integer.class, "INTEGER");
        constrainsTable.put(PrimaryKey.class, "PRIMARY KEY");
        constrainsTable.put(NotNull.class, "NOT NULL");
        constrainsTable.put(Column.class, "");
    }

    public static String typeToSQL(Class<?> javaType) {
        return typeTable.get(javaType);
    }

    public static String constraintsToSQL(Class<?> annotation) {
        return constrainsTable.get(annotation);
    }
}
