/*
* LoginPanel.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

//imports go here

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Panel for the login portion of the gui.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class LoginPanel extends JPanel {

    private JButton newUser, loginButton, filePathButton;
    private JLabel  passwordLabel;
    private JTextField databaseText;
    private JPasswordField passwordText;
    private ChatFrame chatFrame;

    LoginPanel(ChatFrame cf){
        chatFrame = cf;
        setLayout(null);

        //copied and altered code from: http://www.edu4java.com/en/swing/swing3.html

        filePathButton = new JButton("Browse");
        filePathButton.setBounds(10, 10, 80, 25);
        filePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int choice = chooser.showOpenDialog(null);
                if (choice != JFileChooser.APPROVE_OPTION) return;
                databaseText.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        add(filePathButton);

        databaseText = new JTextField(20);
        databaseText.setBounds(100, 10, 260, 25);
        databaseText.setText("Database File");
        add(databaseText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 260, 25);
        add(passwordText);

        loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo loging verification
            }
        });
        add(loginButton);

        newUser = new JButton("register");
        newUser.setBounds(180, 80, 80, 25);
        newUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               chatFrame.createRegisterFrame();
            }
        });
        add(newUser);
    }

    public void message(String from, String to, String msg) throws IOException
    {

    }

    public void start(String user) throws IOException
    {

    }

    public void stop(String user) throws IOException
    {
        
    }

}
