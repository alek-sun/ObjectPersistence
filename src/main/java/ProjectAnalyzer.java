import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import queries.SQLConnector;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectAnalyzer {
    String packageName;
    SQLConnector connector;

    public ProjectAnalyzer(String packageName, SQLConnector connector) {
        this.packageName = packageName;
        this.connector = connector;
    }

    public void analyzeProject() {
        List<Class<?>> allClasses =
                new Reflections(packageName, new SubTypesScanner(false))
                        .getAllTypes()
                        .stream()
                        .map(name -> {
                            try {
                                return Class.forName(name);
                            } catch (ClassNotFoundException e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        allClasses.forEach(this::buildTableInDB);
    }

    private void buildTableInDB(Class<?> cls) {
        connector.createTableByScript(new TableBuilder<>(cls).buildCreateTableScript());
    }

}
