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
public class ChatFrame implements ServerListener, PeerListener
{
    LoginPanel loginPanel;
    RegisterPanel registerPanel;
    ClientListener clientListner;

    private JFrame frame;
    ChatFrame()
    {
        createLoginFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setClientListener(ClientListener listener)
    {
        this.clientListner = listener;
    }

    public void createLoginFrame(){
        frame = new JFrame("Chat Client Login");
        frame.setSize(400, 200);
        loginPanel = new LoginPanel(this);
        frame.add(loginPanel);

    }
    public void createRegisterFrame(){
        frame.remove(loginPanel);
        frame.setSize(400, 210);
        registerPanel = new RegisterPanel(this);
        frame.add(registerPanel);
        frame.revalidate();
        frame.repaint();

    }
    public void createChatFrame(){
        if (registerPanel == null){
            frame.remove(registerPanel);
        }else{
            frame.remove(loginPanel);
        }
        frame.setTitle("Chat Client");
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());
        Dimension friendDim = new Dimension(200,700);

        FriendPanel friendPanel = new FriendPanel(this, friendDim);
        frame.add(friendPanel,BorderLayout.WEST);

        MessagePanel messagePanel = new MessagePanel(this);
        messagePanel.setPreferredSize(new Dimension(380, 700));
        frame.add(messagePanel,BorderLayout.EAST);

        frame.revalidate();
        frame.repaint();
    }
}
