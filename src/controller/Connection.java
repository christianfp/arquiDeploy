package controller;

import persistence.IPersistence;

public class Connection
{

   public Connection(MailSystem s, IObservers observers)
   {
       system = s;
       this.observers = observers;
       this.persistence = persistence;
   }

   public void resetConnection()
   {
       currentMailbox=null;
       status=new Connect(this);

   }

    public boolean executeCommand(String input)
    {
        if (isInputHangUpCommand(input))
            hangup();
        else if (isQuitCommand(input))
            return false;
        else
            dial(input);
        return true;
    }
    public void saveChanges() {
       system.saveChanges(currentMailbox);
    }

    private void hangup()
    {
        status.hangup();
    }
   public void dial(String key)
   {
          status.dial(key);
   }
    public void updateObservers(String text)
    {
        observers.updateObservers(text);
    }
   public boolean isConnected() {
	   return status instanceof Connect;
   }

   public boolean isRecording() {
	   return status instanceof Recording;
   }

   public boolean isChangePassCode() {
	   return status instanceof ChangePasscode;
   }

   public boolean isChangeGreeting() {
	   return status instanceof ChangeGreating;
   }

   public boolean isMailBoxMenu() {
	   return status instanceof MailboxMenu;
   }

   public boolean isMessageMenu() {
	   return status instanceof MessageMenu;
   }


    private boolean isQuitCommand(String input) {
        return input.equalsIgnoreCase("Q");
    }

    private boolean isInputHangUpCommand(String input) {
        return input.equalsIgnoreCase("H");
    }
    public Mailbox getCurrentMailbox() {
        return currentMailbox;
    }
    public MailSystem getMailboxSystem() {
        return system;
    }
    public void setMailbox(Mailbox mailbox) {
        this.currentMailbox = mailbox;
    }
    public void setStatus(IState state)
    {
        this.status=state;
    }
    private MailSystem system;
    private IObservers observers;
    private IPersistence persistence;
    private Mailbox currentMailbox;
    private IState status;


}











