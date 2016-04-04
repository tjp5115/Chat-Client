/*
* MessagePanel.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

//imports go here

import javax.swing.*;
import java.awt.*;

/* Message panel for the ChatFrame gui. Has the conversation for

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class MessagePanel extends JPanel{
    private ChatFrame chatFrame;
    private String user;
    MessagePanel(ChatFrame cf, String user){
        this.user = user;
        chatFrame = cf;
        GridLayout grid = new GridLayout(3,1);
        setLayout(grid);
        add(new JButton("Chat Section"));
    }

    public void addMessage(String user, String message){

    }
}
