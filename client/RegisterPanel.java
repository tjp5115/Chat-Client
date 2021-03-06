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
import java.util.Arrays;

/* Panel for the register portion of the gui.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class RegisterPanel extends JPanel{
    private JButton createUserButton, filePathButton, back;
    private JLabel userLabel, passwordLabel, IPLabel;
    private JTextField userText, databaseText, IPText;
    private JPasswordField passwordText;
    private ChatFrame chatFrame;

    RegisterPanel(ChatFrame cf){
        chatFrame = cf;
        setLayout(null);

        //copied and altered code from: http://www.edu4java.com/en/swing/swing3.html

        userLabel = new JLabel("Username");
        userLabel.setBounds(10, 10, 80, 25);
        add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 10, 260, 25);
        add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 260, 25);
        add(passwordText);

        IPLabel = new JLabel("Server IP");
        IPLabel.setBounds(10, 70, 80, 25);
        add(IPLabel);

        IPText = new JTextField(12);
        IPText.setBounds(100, 70, 260, 25);
        add(IPText);

        filePathButton = new JButton("Browse");
        filePathButton.setBounds(10, 100, 80, 25);
        filePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int choice = chooser.showOpenDialog(null);
                if (choice != JFileChooser.APPROVE_OPTION) return;
                databaseText.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        add(filePathButton);

        databaseText = new JTextField(20);
        databaseText.setText("Database File Directory");
        databaseText.setBounds(100, 100, 260, 25);
        add(databaseText);

        createUserButton = new JButton("Create User");
        createUserButton.setBounds(100, 140, 80, 25);
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (databaseText.getText().isEmpty() ||
                        IPText.getText().isEmpty() ||
                        passwordText.getPassword().length == 0 ||
                        userText.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Fields are Empty.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    chatFrame.registerUsername();
                }
            }
        });

        back = new JButton("Back");
        back.setBounds(200, 140, 80, 25);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatFrame.createLoginFrame();
            }
        });
        add(back);
        add(createUserButton);
    }

    public String getUsername(){
        return userText.getText();
    }
    public String getPassword(){
        return new String(passwordText.getPassword());
    }
    public String getPath(){
        return databaseText.getText();
    }
    public String getServerIP(){
        return IPText.getText();
    }
}
