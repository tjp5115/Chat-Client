/*
* ChatFrame.java
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

/* Chat frame for the GUI. Components are added to this class for each section of the frame.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ChatFrame {
    private FriendPanel friendPanel;
    private MessagePanel messagePanel;
    private JFrame frame;
    ChatFrame(){
        friendPanel = new FriendPanel();
        messagePanel = new MessagePanel();
        frame = new JFrame("Chat Client");
        frame.setSize(300, 700);
        frame.setLayout(new GridLayout(1,3));
        frame.add(friendPanel);
        frame.add(messagePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
