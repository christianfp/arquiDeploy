package controller;

public class ChangePasscode implements IState {

    private Mailbox currentMailbox;
    private String accumulatedKeys="";
    private Connection connection;
    ChangePasscode(Connection connection)
    {
        this.connection=connection;
        this.currentMailbox=connection.getCurrentMailbox();
        showEnterNewPasscodeMessage();

    }
    @Override
    public void dial(String command) {
        if (itIsANumeralCharacter(command))
        {
            setNewPasscodeToCurrentMailbox();
            changeToMailboxMenuState();
        }
        else {
            saveCommand(command);
        }
    }

    private void setNewPasscodeToCurrentMailbox() {
        currentMailbox.setPasscode(accumulatedKeys);
        connection.saveChanges();
    }

    private void saveCommand(String command) {
        accumulatedKeys += command;
    }

    private void changeToMailboxMenuState() {
        connection.setStatus(new MailboxMenu(connection));
    }

    private void showEnterNewPasscodeMessage() {
        connection.updateObservers(ENTER_NEW_PASSCODE_MESSAGE);
    }
    @Override
    public void hangup() {
        connection.resetConnection();
    }

    private boolean itIsANumeralCharacter(String key) {
        return key.equals("#");
    }

    private String ENTER_NEW_PASSCODE_MESSAGE = "Enter new passcode followed by the # key";

}
