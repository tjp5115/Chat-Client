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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/* Message panel for the ChatFrame gui. Has the conversation for

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class MessagePanel extends JPanel{
    private JScrollPane messagesScrollPane;
    private JPanel messages, send;
    private JTextArea messageText;
    private JButton sendMessage;
    private ChatFrame chatFrame;
    private final String user, friend;
    int messageWidth;
    MessagePanel(ChatFrame cf, final String user, final String friend, Dimension dim){
        this.user = user;
        this.friend = friend;
        chatFrame = cf;
        setLayout(new BorderLayout());

        messageText = new JTextArea(6,27);
        messageText.setBorder(BorderFactory.createLineBorder(Color.black));
        messageText.setLineWrap(true);
        sendMessage = new JButton("Send");
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chatFrame.message(user, friend, messageText.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        int w = dim.width;
        int h = (int)(dim.height * .2);
        send = new JPanel();
        send.setLayout(new FlowLayout());
        send.setPreferredSize(new Dimension(w, h));
        send.add(messageText);
        send.add(sendMessage);
        add(send, BorderLayout.SOUTH);


        Dimension messagesDim = new Dimension(dim.width, (int) (dim.height * 0.7));
        messageWidth = (int)(messagesDim.width * 0.9);
        messages = new JPanel(new WrapLayout());
        messagesScrollPane = new JScrollPane(messages,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        messagesScrollPane.setPreferredSize(messagesDim);
        JScrollBar vertical = messagesScrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        add(messagesScrollPane, BorderLayout.NORTH);



    }

    public void addMessage(String sender, String message){
        JPanel messagePanel = new JPanel(new BorderLayout());
        JTextArea messageText = new JTextArea();
        messageText.setLineWrap(true);
        messageText.setColumns(26);
        messageText.setText(message);
        messageText.setEditable(false);
        messageText.setSize(messageWidth-20,Short.MAX_VALUE);
        messagePanel.add(messageText);
        messagePanel.setPreferredSize(new Dimension(messageWidth, messageText.getPreferredSize().height));

        if(sender.equals(user)){
            messageText.setAlignmentX(JTextPane.RIGHT_ALIGNMENT);
            messagePanel.add(messageText,BorderLayout.EAST);
        }else{
            messageText.setAlignmentX(JTextPane.LEFT_ALIGNMENT);
            messagePanel.add(messageText,BorderLayout.WEST);
        }

        messages.add(messagePanel);
        messages.revalidate();
        messages.repaint();
        revalidate();
        repaint();


    }
}
