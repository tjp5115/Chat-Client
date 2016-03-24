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

    JScrollPane scrollPane;
    JPanel friends;
    JPanel options;
    Dimension dim;
    FriendPanel(Dimension _dim){
        setLayout(new BorderLayout());
        dim = _dim;
        setPreferredSize(dim);
        Dimension friendDim = new Dimension(dim.width, (int) (dim.height * 0.7));
        Dimension optionsDim= new Dimension(dim.width, (int) (dim.height * 0.2));
        Dimension buttonSize = new Dimension(friendDim.width,friendDim.height/10);

        friends = new JPanel();
        friends.setLayout(new WrapLayout());
        friends.add(new FriendButton("Jimmy", buttonSize));
        friends.add(new FriendButton("Jimmy", buttonSize));
        friends.add(new FriendButton("Jimmy", buttonSize));
        friends.add(new FriendButton("Jimmy", buttonSize));
        friends.add(new FriendButton("Jimmy", buttonSize));
        friends.add(new FriendButton("Jimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        friends.add(new FriendButton("wasdasdJimmy", buttonSize));
        scrollPane = new JScrollPane(friends,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(friendDim);
        add(scrollPane, BorderLayout.NORTH);

        options = new JPanel();
        options.setLayout(new WrapLayout());
        JButton add = new JButton("Add Friend");
        add.setPreferredSize(buttonSize);
        options.add(add);

        JButton remove = new JButton("Remove Friend");
        remove.setPreferredSize(buttonSize);
        options.add(remove);
        options.setPreferredSize(optionsDim);
        add(options, BorderLayout.SOUTH);
    }

    class FriendButton extends JButton{
        FriendButton(String name, Dimension dim){
            super(name);
            setPreferredSize(dim);
        }
    }

}
