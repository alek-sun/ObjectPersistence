import java.util.HashMap;
import java.util.Map;

public class JavaToSQLTranslator {
    private static final Map<Class<?>, String> table = new HashMap<>();
    static {
        table.put(String.class, "TEXT");
        table.put(Integer.class, "INTEGER");
    }

    public static String toSQL(Class<?> javaType) {
        return table.get(javaType);
    }
}
