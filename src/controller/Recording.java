package controller;

public class Recording implements IState{
    String message;
    private Connection connection;
    private Mailbox currentMailbox;
    public Recording(Connection connection)
    {
        this.connection=connection;
        this.currentMailbox=connection.getCurrentMailbox();
    }
    @Override
    public void dial(String command) {
        if (isAMessage(command))
        {
            addMessage(command);
        }
        if (isNumericalCommand(command))
        {
            changeStateToLogin();
            executeCommand(command);
        }
    }

    private void executeCommand(String command) {
        connection.executeCommand(command);
    }

    private void addMessage(String command) {
        message= command;
    }

    private boolean isAMessage(String key) {
        return key.length()>1;
    }

    private void changeStateToLogin() {
        connection.setStatus(new Login(connection));
    }


    @Override
    public void hangup() {
        if (isNotTheMessageEmpty())
        {
            saveMessage();
        }
        connection.resetConnection();
    }

    private boolean isNotTheMessageEmpty() {
        return message!=null;
    }

    private void saveMessage() {
        currentMailbox.addMessage(new Message(message));
        connection.saveChanges();
    }

    private boolean isNumericalCommand(String input) {
        return input.length() == 1
                && "1234567890#".contains(input);
    }

}
