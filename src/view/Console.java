package view;

import controller.Connection;

import java.util.Scanner;

public class Console implements View
{
   public Console(Scanner aScanner, Connection connection)
   {
      scanner = aScanner;
      this.connection=connection;
   }
   public void update(String message)
   {
      System.out.println(message);
   }
   
   public String speakT(String output)
   {
	   return output;
   }
   public void run()
   {
      boolean more = true;
      while (more) {
         String input = scanner.nextLine();
         more = connection.executeCommand(input);
      }
   }

   private Scanner scanner;
   private Connection connection;
}
