package queries;

import Constraints.Constraint;

import java.util.List;

public class SelectQuery {
    String script;
    Class<?> selectedType;
    SQLConnector connector;

    public SelectQuery(String script, Class<?> selectedType, SQLConnector connector) {
        this.script = script;
        this.selectedType = selectedType;
        this.connector = connector;
    }

    public SelectQuery select() {
        script = "SELECT * FROM (" + script + ")";
        return this;
    }

    public SelectQuery where(Constraint constraint) {
        script += " WHERE " + constraint.getResult();
        return this;
    }

    public List<Object> execute() {
        System.out.println(script);
        return connector.executeQuery(selectedType, script);
    }
}
