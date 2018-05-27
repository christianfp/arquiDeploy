package test;

import controller.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.Console;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
public class ConnectionTest {

    @Before
	public void init() {
	    mockedMailsystem = mock(MailSystem.class);
	    mockedTelephone = mock(Console.class);
	    Observers observers=new Observers();
	    observers.addObservable(mockedTelephone);
	    connection = new Connection(mockedMailsystem, observers);
	    //connection.addObservable(mockedTelephone);
		connection.resetConnection();
        Mailbox chosenMailbox = new Mailbox(idMailBox, HI_MESSAGE);
        when(mockedMailsystem.findMailbox(idMailBox)).thenReturn(chosenMailbox);
	}

	@Test
	public void theConnectionWasStarted() {
		connection.resetConnection();
	    assertTrue(connection.isConnected());
	}

	
	@Test
	public void shouldChooseValidMailBoxWhenSelectTheMailBoxAndSelectNumeral() {
		dialMailBox(idMailBox);
		assertTrue(connection.isRecording());
		verify(mockedTelephone).update(HI_MESSAGE);
	}
	@Test
	public void shouldGetIntoMailBoxMenuWhenSelectTheMailBox() {

		dialMailBox(idMailBox);
		dialMailBoxMenu();
		assertTrue(connection.isMailBoxMenu());
	}


	@Test
	public void shouldGetIntoMessageMenuWhenSelectTheMailBox() {

		String mailBoxMenuOption = "1";
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		listenMessages();

		assertTrue(connection.isMessageMenu());
	}
	@Test
	public void shouldGetIntoChangePassCodeMenuWhenSelectMenuOption2() {

		String changePasscode = "2";
		dialMailBox(idMailBox);
		selectOptionOfMailBoxMenu(changePasscode);
		assertTrue(connection.isChangePassCode());
	}
	@Test
	public void shouldGetIntoChangeGreetingMenuAfterSelectMenuOption3() {

		String changeGreeting = "3";
		dialMailBox(idMailBox);
		selectOptionOfMailBoxMenu(changeGreeting);

		assertTrue(connection.isChangeGreeting());
	}

	@Test
	public void shouldChangePassCodeFrom1To2AfterSelectTheMailBox() {
		String newKeyMailBox = "2";
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		changePasscode(newKeyMailBox);
		dialMailBox(idMailBox);
		dialMailBox(newKeyMailBox);

		assertTrue(connection.isMailBoxMenu());
	}


	@Test
	public void whenISendAMessageToAccountOneAndSeeTheLastMessageIShouldReturnTheMessageISend() {

		String mailBoxMenuOption = "1";
		dialMailBox(idMailBox);
		selectOptionOfMailBoxMenu(mailBoxMenuOption);
		verify(mockedTelephone).update(ENTER_MAILBOX_MESSAGE);

	}



	@Test
	public void whenIDeleteTheLastMessageIShouldReturnTheStartMessage() {

		String deleteCurrentMessage = "3";
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		selectOptionOfListenMessagesMenu(deleteCurrentMessage);
		verify(mockedTelephone).update(ENTER_MAILBOX_MESSAGE);
	}
	@Test
	public void whenIEnterTheMessageMenuAndSelectTheOptionExitShouldReturnTheStartMessage() {

		String returnMainMenu = "4";
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		selectOptionOfListenMessagesMenu(returnMainMenu);
		verify(mockedTelephone).update(ENTER_MAILBOX_MESSAGE);
	}

	private void selectOptionOfListenMessagesMenu(String option) {
		connection.executeCommand("1");
		connection.executeCommand(option);
	}


	
	@Test
	public void shouldChangeGreetingFromStockOneToNewOne() {
		String optionForChangeGreeting = "3";
		String newGreeting="Hola, como estas?";
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		changeGreeting(optionForChangeGreeting, newGreeting);
		dialMailBox(idMailBox);
		verify(mockedTelephone, times(2)).update(newGreeting);
	}
	@Test
	public void IfIEnterTheMailboxMenuOf1AndPressHShouldTheStateGoToConnect()
	{
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		dialMailBox("h");
		Assert.assertEquals(true, connection.isConnected());
	}
	@Test
	public void IfIEnterTheMessageMenuOf1AndPressHShouldTheStateGoToConnect()
	{
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		dialMessageMenu();
		dialHangup();
		Assert.assertEquals(true, connection.isConnected());
	}
	@Test
	public void IfIEnterToLoginStateAndPutTheIncorrectPasscodeShouldBeShowIncorrectPasscodeMessage()
	{
		dialMailBox(idMailBox);
		dialIncorrectPasscode();
		verify(mockedTelephone).update(INCORRECT_PASSCODE_MESSAGE);
	}

	private void dialIncorrectPasscode() {
    	connection.executeCommand("3");
		connection.executeCommand("#");
	}

	@Test
	public void IfIEnterTheMessageMenuOf1AndPressSaveMessageTheSizeOfKeptMessagesShouldBeIncreasedByOne()
	{
		dialMailBox(idMailBox);
		sendDefaultMessage();
		dialHangup();
		dialMailBox(idMailBox);
		Mailbox mailbox=connection.getCurrentMailbox();
		MessageQueue kept=mailbox.getKeptMessages();
		int quantity=kept.size();
		dialMailBoxMenu();
		dialMessageMenu();
		dialSecondOption();
		Mailbox lastMailbox=connection.getCurrentMailbox();
		MessageQueue lastKept=lastMailbox.getKeptMessages();
		Assert.assertEquals(quantity+1,lastKept.size());
	}
	@Test
	public void IfIEnterTheMessageMenuOf1AndPressShowMessageShouldBeShowEmptyMessages()
	{
		dialMailBox(idMailBox);
		dialMailBox(idMailBox);
		dialMessageMenu();
		dialFirstOption();
		String message = setMessageEmpty();
		verify(mockedTelephone, times(2)).update(message);
	}

	private String setMessageEmpty() {
		return EMPTY_MAILBOX_MESSAGE + "\n"+MESSAGE_MENU_TEXT;
	}

	private void dialFirstOption() {
    	connection.executeCommand("1");
	}

	private void dialSecondOption() {
    	connection.executeCommand("2");
	}


	@Test
	public void IfIEnterTheChangeGreetingOptionOf1AndPressHShouldTheStateGoToConnect()
	{
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		dialThirdOption();
		dialHangup();
		Assert.assertEquals(true, connection.isConnected());
	}
	@Test
	public void IfIAmInConnectSateAndPressHShouldTheStateGoToConnect()
	{
		dialHangup();
		Assert.assertEquals(true, connection.isConnected());
	}
	@Test
	public void IfIDialToMailbox1AndPressHShouldTheStateGoToConnect()
	{
		dialMailBox(idMailBox);
		dialHangup();
		Assert.assertEquals(true, connection.isConnected());
	}
	@Test
	public void IfIDialToMailbox1SendAMessageAndPressHShouldTheStateGoToConnect()
	{
		dialMailBox(idMailBox);
		sendDefaultMessage();
		dialHangup();
		Assert.assertEquals(true, connection.isConnected());
	}

	private void sendDefaultMessage() {
    	connection.executeCommand("Hola como estas");
	}

	@Test
	public void IfIAmInLoginSatePressTheKey1AndPressHShouldTheStateGoToConnect()
	{
		dialMailBox(idMailBox);
		dialKeyOne();
		dialHangup();
		Assert.assertEquals(true, connection.isConnected());
	}



	@Test
	public void IfIEnterTheChangePasscodeOptionOf1AndPressHShouldTheStateGoToConnect()
	{
		dialMailBox(idMailBox);
		dialMailBoxMenu();
		dialChangePasscodeOption();
		dialHangup();
		Assert.assertEquals(true, connection.isConnected());
	}




	@Test
	public void whenIEnterANumberOfMailThatDoesNotExistIShouldReturnTheIncorrectLoginMessage()
	{
		when(mockedMailsystem.findMailbox(idMailBox)).thenReturn(null);
		dialMailBox(idMailBox);
        String INCORRECT_MAILBOX_NUMBER_MESSAGE = "Incorrect mailbox number. Try again!";
        verify(mockedTelephone).update(INCORRECT_MAILBOX_NUMBER_MESSAGE);
	}
	private void dialKeyOne() {
		connection.executeCommand("1");
	}
	private void dialChangePasscodeOption() {
		connection.executeCommand("2");
	}
	private void dialThirdOption() {
		connection.executeCommand("3");
	}
	private void selectOptionOfMailBoxMenu(String mailBoxMenuOption) {
		dialMailBoxMenu();
		connection.executeCommand(mailBoxMenuOption);
	}

	private void dialMessageMenu() {
		connection.executeCommand(idMailBox);
		connection.executeCommand("#");
		dialFirstOption();
	}
	private void dialHangup() {
		connection.executeCommand("h");

	}
	private void changePasscode(String newKeyMailBox) {

		String mailBoxMenuOption = "2";
		String hangDown = "h";
		connection.executeCommand(mailBoxMenuOption);
		connection.executeCommand(newKeyMailBox);
		connection.executeCommand("#");
		connection.executeCommand(hangDown);
	}
	private void changeGreeting(String optionForChangeGreeting, String newGreeting) {
		connection.executeCommand(optionForChangeGreeting);
		connection.executeCommand(newGreeting);
		connection.executeCommand("#");
		connection.executeCommand("h");
	}

	private void dialMailBox(String idMailBox) {
		connection.executeCommand(idMailBox);
		connection.executeCommand("#");
	}

	private void listenMessages() {
		connection.executeCommand("1");
		connection.executeCommand("#");
	}

	private void dialMailBoxMenu() {
		connection.executeCommand(keyMailBox);
		connection.executeCommand("#");
	}
    private MailSystem mockedMailsystem;
    private Console mockedTelephone;
    private Connection connection;
    private String ENTER_MAILBOX_MESSAGE = "Enter mailbox number followed by #";
    private String idMailBox = "1";
    private String keyMailBox = "1";
    private String HI_MESSAGE="Hola, como estas?";
	private String EMPTY_MAILBOX_MESSAGE = "No messages.";
	private static final String MESSAGE_MENU_TEXT =
			"Enter 1 to listen to the current message\n"
					+ "Enter 2 to save the current message\n"
					+ "Enter 3 to delete the current message\n"
					+ "Enter 4 to return to the main menu";
	private String INCORRECT_PASSCODE_MESSAGE = "Incorrect passcode. Try again!";

}