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
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.net.ssl.*;

/* Chat frame for the GUI. Components are added to this class for each section of the frame.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ChatFrame implements ServerListener, PeerListener
{
    private DatabaseHandlerClient dbHandler;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ClientListener clientListener;
    private HashMap<String,PeerListener> peerListener;
    private FriendPanel friendPanel;
    private HashMap<String,MessagePanel> messagePanel;
    private MessagePanel currentMessagePanel;
    private JFrame frame;
    private ManagerClient managerClient;
    private Dimension messageDim;
    ChatFrame(ManagerClient managerClient)
    {
        messagePanel = new HashMap<>();
        peerListener = new HashMap<>();
        this.managerClient = managerClient;
        frame = new JFrame("Chat Client Login");
        createLoginFrame();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try {
                    if(clientListener != null)
                        clientListener.logoff(dbHandler.getName(), dbHandler.getHash());
                    for(PeerListener peer: peerListener.values())
                        peer.stop(dbHandler.getName());
                    System.exit(0);
                }catch (Exception E){
                }
                e.getWindow().dispose();
            }
        });
        messageDim = new Dimension(380, 700);
    }
    ChatFrame(){
        this(null);
    }



    //setter
    public void setClientListener(ClientListener clientListener){
        this.clientListener = clientListener;
    }
    public void setManagerClient(ManagerClient managerClient){this.managerClient = managerClient;}
    /**
     * creates and sets the current frame to the login frame of the gui
     */
    public void createLoginFrame(){
        if(registerPanel != null)
            frame.remove(registerPanel);
        frame.setSize(400, 200);
        loginPanel = new LoginPanel(this);
        frame.add(loginPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * creates and sets the current frame to the register new user frame of the gui
     */
    public void createRegisterFrame(){
        if(loginPanel != null)
            frame.remove(loginPanel);
        frame.setSize(400, 230);
        registerPanel = new RegisterPanel(this);
        frame.add(registerPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * creates and sets the current frame to the chat frame for the gui.
     */
    public void createDefaultChatFrame(){
        if (registerPanel != null){
            frame.remove(registerPanel);
        }
        if(loginPanel != null){
            frame.remove(loginPanel);
        }

        frame.setTitle("Chat Client: " + dbHandler.getName());
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());
        Dimension friendDim = new Dimension(200,700);

        //friendPanel = new FriendPanel(this, friendDim, dbHandler.getFriends());
        friendPanel = new FriendPanel(this, friendDim, dbHandler.getFriends());
        frame.add(friendPanel,BorderLayout.WEST);


        frame.revalidate();
        frame.repaint();
    }
    public void createChatSession(String user){
        if(currentMessagePanel != null)
            currentMessagePanel.setVisible(false);

        currentMessagePanel = new MessagePanel(this, dbHandler.getName(), user, messageDim);
        messagePanel.put(user, currentMessagePanel) ;
        currentMessagePanel.setPreferredSize(messageDim);
        frame.add(currentMessagePanel, BorderLayout.EAST);

        frame.revalidate();
        frame.repaint();
    }

    /**
     * When a user click the username to either bring up the chat window, or create a chat connection with another user.
     * @param user
     */
    public void setMessagePanelUser(String user){
        if(messagePanel.containsKey(user)){
            currentMessagePanel.setVisible(false);
            currentMessagePanel = messagePanel.get(user);
            currentMessagePanel.setVisible(true);
            frame.revalidate();
            frame.repaint();
        }else{
            try {
                clientListener.getIP(dbHandler.getName(), dbHandler.getHash(), user);
            }catch( IOException ioe ){
                System.err.println("Error while setting up a chat connection to "+user);
            }
        }
    }

    /**
     * the logon button was pressed.
     */
    public void logon(){
        if(dbHandler == null){
            try {
                dbHandler = new DatabaseHandlerClient(loginPanel.getPath(), loginPanel.getUsername(), loginPanel.getPassword());
            } catch (SQLException e) {
                dbHandler = null;
                JOptionPane.showMessageDialog(null,
                        "Error connecting to database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            managerClient.setServerIP(dbHandler.getServerIP());
            managerClient.run();
            clientListener.logon(dbHandler.getName(), dbHandler.getHash());
        }catch(IOException ioe){
            JOptionPane.showMessageDialog(null,
                    "Error logging into server.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * the register user button was pressed.
     */
    public void registerUsername() {
        try {
            managerClient.setServerIP(registerPanel.getServerIP());
            managerClient.run();
            String hash = registerPanel.getPassword();
            hash = dbHandler.getPasswordHash(hash);
            String ip = getIP();
            String username = registerPanel.getUsername();
            clientListener.createAccount(ip,
                    username,
                    hash
                    );
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error logging into server.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * request a user to be a pal
     * @param username
     */
    public void friendRequest(String username){
        try {
            clientListener.friendRequest(dbHandler.getName(),dbHandler.getHash(),username,0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Server error while making friend request.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //begin of the PeerListener Interface
    /**
     * Send a message to a user.
     *
     * @param from - who the message is from.
     * @param to   - who the message is too.
     * @param msg  - the message.
     */
    @Override
    public void message(String from, String to, String msg) throws IOException {
        if(to.equals(dbHandler.getName())){

            messagePanel.get(from).addMessage(from, msg);
        }else if (peerListener.get(to).isClosed()){
            JOptionPane.showConfirmDialog(null, to + " has left the conversation.",
                    "friend request",
                    JOptionPane.PLAIN_MESSAGE);
            frame.remove(messagePanel.get(to));
        }else{
            messagePanel.get(to).addMessage(from, msg);
            peerListener.get(to).message(from,to,msg);
        }

        frame.revalidate();
        frame.repaint();
    }

    /**
     * Requests a chat session to begin between two users.
     *
     * @param user - user requesting a chat session.
     */
    @Override
    public void start(String user) throws IOException {
        createChatSession(user);
    }

    /**
     * Stops a chat session between two users.
     *
     * @param user - user to stop the session from.
     */
    @Override
    public void stop(String user) throws IOException {
        JOptionPane.showConfirmDialog(null, user + " has left the conversation.",
                "friend request",
                JOptionPane.PLAIN_MESSAGE);
        if(currentMessagePanel.equals(messagePanel.get(user))){
            currentMessagePanel = null;
        }
        frame.remove(messagePanel.get(user));
        messagePanel.remove(user);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public boolean isClosed() {
        return false;
    }


    //begin of the ClientListener Interface

    /**
     * request for a friend from a given user.
     *
     * @param from   - requester user.
     * @param to     - requested user.
     * @param status - status of the friend request.
     * @throws IOException
     */
    @Override
    public void userFriendStatus(String from, String to, int status) throws IOException {
        if(status == 0 ){
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you accept " + from + " as a friend?",
                    "friend request",
                    dialogButton);
            if(dialogResult == 0) {
                //yes
                clientListener.friendRequest(to, dbHandler.getHash(), from, 1);
                dbHandler.addFriend(from);
                friendPanel.addFriend(from);
                frame.revalidate();
                frame.repaint();
            } else {
                //no
                clientListener.friendRequest(to, dbHandler.getHash(), from, 2);
            }
        }else if(status == 1){
            JOptionPane.showConfirmDialog(null, from + " accepted you as a friend.",
                    "friend request",
                    JOptionPane.PLAIN_MESSAGE);
            dbHandler.addFriend(from);
            friendPanel.addFriend(from);
            frame.revalidate();
            frame.repaint();
        }else if(status == 2){
            JOptionPane.showConfirmDialog(null, from + " declined you as a friend.",
                    "friend request",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * return the IP of a user
     *
     * @param user - user to the IP
     * @param IP   - IP address of the user.
     * @throws IOException
     */
    @Override
    public void IP(String user, String IP) throws IOException {
        // we have the IP, now it is time to initialize the connection.
        dbHandler.addFriendIP(user, IP);
        if(dbHandler.getFriendPort(user) == null ) {
            SSLServerSocket serverSocket = managerClient.createClientServerConnection();
            clientListener.initConversation(dbHandler.getName(), dbHandler.getHash(), user, serverSocket.getLocalPort() + "");
            peerListener.put(user, managerClient.createClientConnection(serverSocket,user));
        }else{
            peerListener.put(user,
                    managerClient.createClientConnection(dbHandler.getFriendIP(user),
                            Integer.parseInt(dbHandler.getFriendPort(user))));
            start(user);
        }
    }

    /**
     * return an error.
     *
     * @param error - String noting the error that took place.
     * @throws IOException
     */
    @Override
    public void error(String error) throws IOException {
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * initiate a conversation between two clients
     *
     * @param from - initiator
     * @param to   - responder
     * @throws IOException
     */
    @Override
    public void initConversation(String from, String to, String port) throws IOException {
        if(!dbHandler.isFriend(from)) return;
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to start a conversation with " + from,
                "Init chat conversation",
                dialogButton);
        if(dialogResult == 0) {
            //yes
            dbHandler.addFriendPort(from, port);
            clientListener.getIP(to, dbHandler.getHash(), from);
        }else{
            clientListener.rejectConversation(to, dbHandler.getHash(), from);
        }
    }

    /**
     * response from server on either or not username was taken
     *
     * @param user   username given to the server.
     * @param status 1 - you now have the username
     *               0 - that username is already taken
     * @throws IOException
     */
    @Override
    public void createAccountResponse(String user, int status) throws IOException {
        if(status == 1) {
            try {
                dbHandler = new DatabaseHandlerClient(registerPanel.getPath()+"/",
                        registerPanel.getUsername(),
                        registerPanel.getPassword());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Error connecting to database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            dbHandler.init(user, getIP());
            loginSuccess();
        } else
            JOptionPane.showMessageDialog(null, "Username is taken, select a new one.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
    }

    /**
     * a client has reject your conversation
     *
     * @param user - 'friend' who rejected.
     */
    @Override
    public void rejectedConverstation(String user) throws IOException {
        JOptionPane.showConfirmDialog(null, user + " declined the conversation.",
                "Conversation Response.",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * status of login
     */
    @Override
    public void loginSuccess() throws IOException {
        createDefaultChatFrame();
    }

    /**
     * Message that requests a user remove the friend
     *
     * @param requester - the user who requested the remove
     * @param friend    - the friend that was removed.
     */
    @Override
    public void removeFriend(String requester, String friend) {
        if(requester.equals(dbHandler.getName())){
            friendPanel.removeFriend(friend);
            dbHandler.updateFriend(friend, false);
        }else{
            JOptionPane.showConfirmDialog(null, requester + " has removed you as a friend.",
                    "Friend Removal.",
                    JOptionPane.PLAIN_MESSAGE);
            friendPanel.removeFriend(requester);
            dbHandler.updateFriend(requester, false);
        }
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public String getIP() {
        return managerClient.getUserIP();
    }

    public void requestRemoveFriend(String friend){
       if(dbHandler.isFriend(friend))
           try {
               clientListener.requestRemoveFriend(dbHandler.getName(),dbHandler.getHash(),friend);
           } catch (IOException e) {
               JOptionPane.showMessageDialog(null, "Error connecting to server.",
                       "Error",
                       JOptionPane.ERROR_MESSAGE);
           }
       else{
           JOptionPane.showMessageDialog(null, friend + " is not your friend.",
                   "Error",
                   JOptionPane.ERROR_MESSAGE);
       }
    }
}
