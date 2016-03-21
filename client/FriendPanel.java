/*
* FriendPanel.java
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

/* Left panel for the ChatFrame gui. Has a list of friends, and can the buttons to init a chat.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class FriendPanel extends JPanel {


    FriendPanel(){
        setLayout(new GridLayout(3,1));
        add(new JButton("Button 1"));
        add(new JButton("Button 2"));
        add(new JButton("Button 3"));
    }
}
