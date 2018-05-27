package controller;

public class Login implements IState{
    private Mailbox currentMailbox;
    private String accumulatedKeys="";
    Connection connection;

    Login(Connection connection){
        this.connection=connection;
        this.currentMailbox=connection.getCurrentMailbox();
    }
    @Override
    public void dial(String command) {
        if (itIsANumeralCharacter(command))
        {
            openMailboxMenu();
        }
        else {
            saveActualCommand(command);
        }
    }

    private void openMailboxMenu() {
        if (isTheCorrectPasscodeOfCurrentMailbox())
        {
            changeToMailboxMenuState();
        }
        else {
            showIncorrectPasscodeMessage();
            cleanAccumulatedKeys();
        }
    }

    private void saveActualCommand(String command) {
        accumulatedKeys += command;
    }

    private void cleanAccumulatedKeys() {
        accumulatedKeys = "";
    }

    private void showIncorrectPasscodeMessage() {
        connection.updateObservers(INCORRECT_PASSCODE_MESSAGE);
    }



    private void changeToMailboxMenuState() {
        connection.setStatus(new MailboxMenu(connection));
    }

    private boolean isTheCorrectPasscodeOfCurrentMailbox() {
        return currentMailbox.checkPasscode(accumulatedKeys);
    }

    @Override
    public void hangup() {
        connection.resetConnection();
    }

    private boolean itIsANumeralCharacter(String key) {
        return key.equals("#");
    }
    private String INCORRECT_PASSCODE_MESSAGE = "Incorrect passcode. Try again!";

}
