package controller;

public class MailboxMenu implements IState {


    private Connection connection;
    MailboxMenu(Connection connection){
        this.connection=connection;
        showMailboxMenuOptions();
    }
    @Override
    public void dial(String command) {
        switch (command) {
            case "1":
                changeToMessageMenuState();
                break;
            case "2":
                changeToChangePasscodeState();

                break;
            case "3":
                changeToChangeGreetingState();
                break;
        }
    }

    private void changeToChangeGreetingState() {
        connection.setStatus(new ChangeGreating(connection));
    }

    private void changeToChangePasscodeState() {
        connection.setStatus(new ChangePasscode(connection));
    }

    private void changeToMessageMenuState() {
        connection.setStatus(new MessageMenu(connection));
    }


    @Override
    public void hangup() {
        connection.resetConnection();
    }
    private void showMailboxMenuOptions() {
        connection.updateObservers(MAILBOX_MENU_TEXT);
    }
    private static final String MAILBOX_MENU_TEXT =
            "Enter 1 to listen to your messages\n"
                    + "Enter 2 to change your passcode\n"
                    + "Enter 3 to change your greeting";


}
