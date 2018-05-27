package controller;

import persistence.IPersistence;

import java.util.ArrayList;

public class MailSystem
{
   private ArrayList<Mailbox> mailboxes;
   private int idCurrentMailbox;
   private IPersistence dbContext;

   public MailSystem(int mailboxCount, IPersistence dbContext)
   {
      this.dbContext = dbContext;
      mailboxes = new ArrayList();
      setupMailSystem(mailboxCount);
   }

   private void setupMailSystem(int mailboxCount) {
      ArrayList<Mailbox> mailboxOfDB=dbContext.getAlMailbox();
      int Quantity=mailboxOfDB.size();
      if (Quantity==0)
      {
         setInitial(mailboxCount);
      }
      else
      {
         loadMailboxesOfPersistence(mailboxOfDB);
      }
   }

   private void loadMailboxesOfPersistence(ArrayList<Mailbox> mailboxOfDB) {
      for (Mailbox mailbox:mailboxOfDB) {
            mailboxes.add(mailbox);
      }
   }

   private void setInitial(int mailboxCount) {
      for (int i = 0; i < mailboxCount; i++)
      {
         String passcode = "" + (i + 1);
         String greeting = "You have reached mailbox " + (i + 1)
               + ". \nPlease leave a message now.";
         mailboxes.add(new Mailbox(passcode, greeting));
      }
      for (Mailbox mailbox:mailboxes) {
         dbContext.addMailbox(mailbox);
      }
   }


   public Mailbox findMailbox(String ext)
   {
      int i = Integer.parseInt(ext);
      if (1 <= i && i <= mailboxes.size()) {
         idCurrentMailbox = i ;
         return mailboxes.get(idCurrentMailbox-1);
      }
      else return null;
   }
   public void saveChanges(Mailbox mailbox){
      dbContext.saveChanges(mailbox, idCurrentMailbox);
   }

}
