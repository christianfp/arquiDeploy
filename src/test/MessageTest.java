package test;

import org.junit.Before;
import org.junit.Test;
import controller.Message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class MessageTest {
	private String HI_MESSAGE="Hello, How are you?";
	private Message message;
	@Before
	public void Init(){
		message=new Message(HI_MESSAGE);
	}
	@Test
	public void WriteMessageAndIRequestTextShouldReturnTheSameIWrote() {
		assertEquals(message.getText(), HI_MESSAGE);
	}
	
	@Test
	public void WriteMessageAndIRequestTextShouldNotReturnEmpty() {
		assertNotSame(message.getText(), "");
	}
}
