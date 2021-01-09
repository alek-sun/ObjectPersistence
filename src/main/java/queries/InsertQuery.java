package queries;

public class InsertQuery {
    String script;
    SQLConnector connector;

    public InsertQuery(String script, SQLConnector connector) {
        this.script = script;
        this.connector = connector;
    }

    public void execute(){
        System.out.println(script);
        connector.execute(script);
    }
}
