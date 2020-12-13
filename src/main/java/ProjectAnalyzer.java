import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectAnalyzer implements Runnable {

    String packageName = ""; // todo

    @Override
    public void run() {
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
        allClasses.forEach(this::analyze);
    }

    private void analyze(Class<?> cls) {
        new TableBuilder<>(cls).translation();
    }

}
