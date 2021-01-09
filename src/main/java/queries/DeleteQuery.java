package queries;

import constraints.Constraint;

public class DeleteQuery {
    String script;
    SQLConnector connector;

    public DeleteQuery(String script, SQLConnector connector) {
        this.script = script;
        this.connector = connector;
    }

    public void execute(){
        System.out.println(script);
        connector.execute(script);
    }

    public DeleteQuery where(Constraint constraint) {
        script += " WHERE " + constraint.getResult();
        return this;
    }

}
