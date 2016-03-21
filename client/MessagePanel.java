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
    MessagePanel(){
        GridLayout grid = new GridLayout(3,1);
        setLayout(grid);
        add(new JButton("Chat Section"));
    }
}
