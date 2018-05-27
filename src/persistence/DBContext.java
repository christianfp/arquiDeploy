package persistence;

import controller.Mailbox;
import controller.Message;
import controller.MessageQueue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBContext implements IPersistence {
    private DBConfiguration currentDBConfiguration;
    private String query;

    public DBContext(){
        currentDBConfiguration = new DBConfiguration();
        currentDBConfiguration.connect();
        createTables();
    }

    private void createTables(){
        createMessageTableIfNotExist();
        createMailBoxTableIfNotExist();
    }

    private void createMailBoxTableIfNotExist(){
        query = "CREATE TABLE IF NOT EXISTS `MailBox` (\n" +
                "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`passcode`\tTEXT,\n" +
                "\t`greeting`\tTEXT\n" +
                ");";
        currentDBConfiguration.create(query);
    }
    private void createMessageTableIfNotExist(){
        query = "CREATE TABLE IF NOT EXISTS `Message` (\n" +
                "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`idMailBox`\tINTEGER,\n" +
                "\t`message`\tTEXT,\n" +
                "\t`state`\tINTEGER,\n" +
                "\tFOREIGN KEY(`idMailBox`) REFERENCES `MailBox`(`id`)\n" +
                ");";
        currentDBConfiguration.create(query);
    }

    public void saveChanges(Mailbox mailbox, int idCurrentMailbox) {
        updateMailbox(mailbox, idCurrentMailbox);
        updateMessages(mailbox, idCurrentMailbox);
    }

    private void updateMessages(Mailbox mailbox, int idCurrentMailbox) {
        MessageQueue kept=mailbox.getKeptMessages();
        MessageQueue news=mailbox.getNewMessages();
        updateKeptMessages(idCurrentMailbox, kept);
        updateNewMessages(idCurrentMailbox, news);
    }

    private void updateNewMessages(int idCurrentMailbox, MessageQueue news) {
        int quantityNewsOnMemory=news.size();
        int quantityNewsOnPersistence=getQuantityOfMessagesType(TypeOfMessage.New, idCurrentMailbox);
        if (quantityNewsOnMemory>quantityNewsOnPersistence)
        {
            for (int i=quantityNewsOnPersistence;i<quantityNewsOnMemory;i++)
            {
                Message message=news.getMessageOf(i);
                addMessage(idCurrentMailbox, message, TypeOfMessage.New);
            }
        }
        if (quantityNewsOnMemory<quantityNewsOnPersistence)
        {
            deleteMessageOf(idCurrentMailbox);
        }
    }


    private void updateKeptMessages(int idCurrentMailbox, MessageQueue kept) {
        int quantityKeptOnMemory=kept.size();
        int quantityKetpOnPersistence=getQuantityOfMessagesType(TypeOfMessage.Kept, idCurrentMailbox);
        if (quantityKeptOnMemory>quantityKetpOnPersistence)
        {
            for (int i=quantityKetpOnPersistence;i<quantityKeptOnMemory;i++)
            {
                Message message=kept.getMessageOf(i);
                addMessage(idCurrentMailbox, message, TypeOfMessage.Kept);
            }
        }

    }
    //listo
    private void updateMailbox(Mailbox mailbox, int idMailbox) {
        String passcode=mailbox.getPasscode();
        String greeting=mailbox.getGreeting();
        saveChangesMailbox(idMailbox, passcode, greeting);
    }


    private void addMessage(int idCurrentMailbox, Message message, TypeOfMessage type) {
        if (type==TypeOfMessage.New) {
            insertMessageToMailBox(idCurrentMailbox, message, "New");
        }
        if (type==TypeOfMessage.Kept) {
            insertMessageToMailBox(idCurrentMailbox, message, "Kept");
        }
    }

    private void insertMessageToMailBox(int idCurrentMailbox, Message message,String type) {
        query = "INSERT INTO Message(idMailBox,message,state) VALUES(" + idCurrentMailbox + ",'" + message.getText() + "','" + type + "')";
        currentDBConfiguration.insert(query);
    }


    private int getQuantityOfMessagesType(TypeOfMessage type, int idMailbox){

        if (type.equals(TypeOfMessage.New))
        {
            return getTotalMessagesOf(idMailbox,"New");
        }
        if (type.equals(TypeOfMessage.Kept))
        {
            return getTotalMessagesOf(idMailbox,"Kept");
        }
        return 0;
    }

    private int getTotalMessagesOf(int idMailbox,String state) {
        String total = "0";
        try {
            query = "SELECT COUNT(ME.state) FROM Message ME, MailBox MA  WHERE ME.idMailBox=MA.id AND MA.id=" + idMailbox + " AND ME.state='" + state + "';";
            ResultSet rs = currentDBConfiguration.select(query);
            while (rs.next()) {
                total = rs.getString("COUNT(ME.state)");
            }
            currentDBConfiguration.closeSelect(rs);
            return Integer.parseInt(total);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private void saveChangesMailbox(int idMailbox, String passcode, String greeting)
    {
        query="UPDATE MailBox SET passcode='"+passcode+"', greeting = '"+greeting+"' WHERE id="+idMailbox+";";
        currentDBConfiguration.update(query);
    }



    public void addMailbox(Mailbox mailbox) {

        query="INSERT INTO MailBox(passcode,greeting) VALUES('"+mailbox.getPasscode()+"','"+mailbox.getGreeting()+"');";
        currentDBConfiguration.insert(query);
    }

    public ArrayList<Mailbox> getAlMailbox()
    {

       ArrayList<Mailbox> mailboxes = new ArrayList<>();
        try {
            query="SELECT * FROM MailBox;";
            ResultSet rs = currentDBConfiguration.select(query);
            while (rs.next()) {
                Mailbox mailbox = null;
                int ID = Integer.parseInt(rs.getString("id"));
                String passcode = rs.getString("passcode");
                String greeting = rs.getString("greeting");
                mailbox = new Mailbox(passcode,greeting);
                query="SELECT message, state FROM Message WHERE idMailBox="+ID;
                ResultSet rs2 = currentDBConfiguration.select(query);
                {
                    while (rs2.next()) {
                        Message m = null;
                        String message = rs2.getString("message");
                        String state = rs2.getString("state");
                        m = new Message(message);
                        if(state.equals("New"))
                        {
                            mailbox.addMessage(m);
                        }
                        if(state.equals("Kept"))
                        {
                            mailbox.addKeptMessage(m);
                        }
                    }
                }
                mailboxes.add(mailbox);
            }
            currentDBConfiguration.closeSelect(rs);
            return mailboxes;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    private int getLastNewMessage(int idMailBox){
        String total = "0";
        try {
            query = "SELECT ME.id FROM Message ME, MailBox MA WHERE MA.id=ME.idMailBox AND ME.state='New' AND ME.idMailBox = "+idMailBox;
            ResultSet rs = currentDBConfiguration.select(query);
            while (rs.next()) {
                total = rs.getString("id");
            }
            currentDBConfiguration.closeSelect(rs);
            return Integer.parseInt(total);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private void deleteMessageOf(int idMailbox) {

        int id = getLastNewMessage(idMailbox);
        String query = "DELETE FROM Message WHERE id="+id;
        currentDBConfiguration.delete(query);
    }
}
