package translation;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

public class SQLToJavaTranslator {
    private static final Map<JDBCType, Class<?>> typeTable = new HashMap<>();

    static {
        typeTable.put(JDBCType.VARCHAR, String.class);
        typeTable.put(JDBCType.INTEGER, Integer.class); // todo
    }

    public static Class<?> toJavaType(Integer i) {
        return typeTable.get(JDBCType.valueOf(i));
    }
}
