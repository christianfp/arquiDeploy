package test;

import controller.MailSystem;
import controller.Mailbox;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.IPersistence;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class MailSystemTest {
	IPersistence PersistenceMocked;
	@Before
	public void init()
	{
		PersistenceMocked=mock(IPersistence.class);

	}
	@Test
	public void verifyIfTheMailSystemIsEmpty() {
		MailSystem mailSystem = new MailSystem(0, PersistenceMocked);
		Assert.assertEquals(null, mailSystem.findMailbox("0"));
	}
	@Test
	public void verifyIfTheMailSystemIsNotEmpty(){
		ArrayList<Mailbox> mails = SetupArrayListMailboxes();
		when(PersistenceMocked.getAlMailbox()).thenReturn(mails);
		MailSystem mailSystem = new MailSystem(0, PersistenceMocked);
		Assert.assertNotEquals(null, mailSystem.findMailbox("1"));
	}

	private ArrayList<Mailbox> SetupArrayListMailboxes() {
		ArrayList<Mailbox> mails=new ArrayList<Mailbox>();
		mails.add(new Mailbox("",""));
		mails.add(new Mailbox("",""));
		mails.add(new Mailbox("",""));
		return mails;
	}

	@Test
	public void setInitialMailSystemIfArrayIfDBProviderIsNotEmpty(){
		MailSystem mailSystem = new MailSystem(20, PersistenceMocked);
		Assert.assertNotEquals(null, mailSystem.findMailbox("1"));
	}
	@Test
	public void ifIGetaMailboxAndSaveChangesTheValueOfTheDataBaseShouldBeModified()
	{
		ArrayList<Mailbox> mails = SetupArrayListMailboxes();
		when(PersistenceMocked.getAlMailbox()).thenReturn(mails);
		MailSystem mailSystem = new MailSystem(20, PersistenceMocked);
		Mailbox mailbox=mailSystem.findMailbox("1");
		mailSystem.saveChanges(mailbox);
		verify(PersistenceMocked).saveChanges(mailbox, 1);
		Mailbox mailbox1=mailSystem.findMailbox("1");
		Assert.assertEquals(mailbox, mailbox1);

	}
}
