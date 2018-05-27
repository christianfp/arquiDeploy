package api;
import controller.Connection;

import static spark.Spark.get;
import static spark.Spark.port;

public class VoiceMailService {
    private Connection connection;
    public VoiceMailService(Connection connection){
        this.connection = connection;
    }
    public VoiceMailService(){
    }
    public void hello(){
        port(getHerokuAssignedPort());
        String currentMessage = "hello";
        get("/currentMessage", (req, res) -> currentMessage);
    }
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public void currentMessage(){
        String currentMessage = connection.getCurrentMailbox().getCurrentMessage().getText();
        get("/currentMessage", (req, res) -> currentMessage);
    }
    public void executeCommand(){
        get("/executeCommand", (req, res) ->{
            String id = req.params(":id");
            return connection.executeCommand(id);
        });
    }
    public void currentGreeting(){
        String greeting = connection.getCurrentMailbox().getGreeting();
        get("/currentGreeting", (req, res) -> greeting);
    }



}
