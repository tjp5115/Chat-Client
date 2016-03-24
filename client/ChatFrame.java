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
import java.io.IOException;

/* Chat frame for the GUI. Components are added to this class for each section of the frame.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ChatFrame {
    private FriendPanel friendPanel;
    private MessagePanel messagePanel;
    private JFrame frame;
    ChatFrame(){
        frame = new JFrame("Chat Client");
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());
        Dimension friendDim = new Dimension(200,700);

        friendPanel = new FriendPanel(friendDim);
        friendPanel.setPreferredSize(friendDim);
        frame.add(friendPanel,BorderLayout.WEST);

        messagePanel = new MessagePanel();
        messagePanel.setPreferredSize(new Dimension(380, 700));
        frame.add(messagePanel,BorderLayout.EAST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
