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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

/* Left panel for the ChatFrame gui. Has a list of friends, and can the buttons to init a chat.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class FriendPanel extends JPanel {

    private JScrollPane scrollPane;
    private JPanel friendsPanel;
    private JPanel options;
    private ChatFrame chatFrame;
    private ArrayList<String> friends;
    Dimension buttonSize;
    FriendPanel(ChatFrame cf, Dimension dim, ArrayList<String> friends){
        this.friends = friends;
        for(String f: friends)
            addFriend(f);

        chatFrame = cf;
        setPreferredSize(dim);
        setLayout(new BorderLayout());
        Dimension friendDim = new Dimension(dim.width, (int) (dim.height * 0.7));
        Dimension optionsDim= new Dimension(dim.width, (int) (dim.height * 0.2));
        buttonSize = new Dimension(friendDim.width,friendDim.height/10);

        friendsPanel = new JPanel();
        friendsPanel.setLayout(new WrapLayout());

        scrollPane = new JScrollPane(friendsPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(friendDim);
        add(scrollPane, BorderLayout.NORTH);

        options = new JPanel();
        options.setLayout(new WrapLayout());
        JButton addFriend = new JButton("Add Friend");
        addFriend.setPreferredSize(buttonSize);
        addFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String)JOptionPane.showInputDialog(null,
                        "What friend to add?",
                        "Add Friend",
                        JOptionPane.PLAIN_MESSAGE);
                if ((s!=null)){
                    chatFrame.friendRequest(s);
                }
            }
        });
        options.add(addFriend);

        JButton remove = new JButton("Remove Friend");
        remove.setPreferredSize(buttonSize);
        options.add(remove);
        options.setPreferredSize(optionsDim);
        add(options, BorderLayout.SOUTH);
    }

    public void addFriend(String friend){
        friendsPanel.add(new FriendButton(friend, buttonSize));
    }

    class FriendButton extends JButton{
        private String name;
        FriendButton(String name, Dimension dim){
            super(name);
            this.name = name;
            setPreferredSize(dim);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chatFrame.setMessagePanelUser(name);
                }
            });
        }
    }

}
