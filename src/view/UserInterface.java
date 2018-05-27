package view;

import controller.Connection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class UserInterface extends JFrame implements View {


    public UserInterface(Connection connection)
    {
        pack();
        setConnection(connection);
        setAttributesToComponentsOfUI();
        setActionsToButtons();
        setVisible(true);
    }

    public void update(String message) {
        Output.setText(message);
    }

    private void Run(String input)
    {
        boolean connectionContinue;
        String textarea=Input.getText();
        if (textarea=="")
        {
            connectionContinue=connection.executeCommand(input);
        }
        else
        {
            connectionContinue=connection.executeCommand(textarea);
            connectionContinue=connection.executeCommand(input);
        }

        if (!connectionContinue)
        {
            this.hide();
        }
    }
    private void setConnection(Connection connection) {
        //connection.addObservable(this);
        this.connection=connection;
    }

    private void setAttributesToComponentsOfUI() {
        Output.setEditable(false);
        setSize(400,700);
        setContentPane(panel);
        pressed.setEditable(false);
        pressed.setSize(10,10);
    }

    private void setActionsToButtons() {
        ActionListener listener = returnActionOfButton();
        button1.addActionListener(listener);
        button3.addActionListener(listener);
        button2.addActionListener(listener);
        button4.addActionListener(listener);
        button5.addActionListener(listener);
        button6.addActionListener(listener);
        button7.addActionListener(listener);
        button8.addActionListener(listener);
        button9.addActionListener(listener);
        button0.addActionListener(listener);
        buttonB.addActionListener(listener);
    }

    private ActionListener returnActionOfButton() {
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton buttonClicked=(JButton) e.getSource();
                Run(buttonClicked.getText());
                pressed.setText(pressed.getText()+" "+buttonClicked.getText());
            }
        };

        buttonH.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=Input.getText();
                Input.setText("");
                pressed.setText("");
                Run(text);
                Run("H");
            }
        });
        return listener;
    }

    private JPanel panel;
    private JTextArea Input;
    private Connection connection;
    private JTextArea Output;
    private JButton button1;
    private JButton button8;
    private JButton buttonH;
    private JButton button3;
    private JButton button2;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button9;
    private JButton button7;
    private JButton button0;
    private JButton buttonB;
    private JTextArea pressed;

}
