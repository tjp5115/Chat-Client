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
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/* Chat frame for the GUI. Components are added to this class for each section of the frame.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ChatFrame implements ServerListener, PeerListener, WindowListener
{
    private DatabaseHandler dbHandler;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ClientListener clientListener;
    private HashMap<String,PeerListener> peerListener;
    private FriendPanel friendPanel;
    private HashMap<String,MessagePanel> messagePanel;
    private MessagePanel currentMessagePanel;
    private JFrame frame;
    private Manager manager;
    private Dimension messageDim;
    MessageDigest md;
    ChatFrame(Manager manager)
    {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messagePanel = new HashMap<>();
        peerListener = new HashMap<>();
        this.manager = manager;
        createLoginFrame();
        //createDefaultChatFrame();
        frame.setVisible(true);
    }
    ChatFrame(){
        this(null);
    }

    //setter
    public void setClientListener(ClientListener clientListener){
        this.clientListener = clientListener;
    }

    /**
     * creates and sets the current frame to the login frame of the gui
     */
    public void createLoginFrame(){
        frame = new JFrame("Chat Client Login");
        frame.setSize(400, 200);
        loginPanel = new LoginPanel(this);
        frame.add(loginPanel);

    }

    /**
     * creates and sets the current frame to the register new user frame of the gui
     */
    public void createRegisterFrame(){
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
        }else if(loginPanel != null){
            frame.remove(loginPanel);
        }else{
            System.err.println("CreateDefaultChatFrame Call with no login or register panel, Hope you are debugging");
            frame = new JFrame();
        }




        frame.setTitle("Chat Client");
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());
        Dimension friendDim = new Dimension(200,700);

        //friendPanel = new FriendPanel(this, friendDim, dbHandler.getFriends());
        friendPanel = new FriendPanel(this, friendDim, new ArrayList() );
        frame.add(friendPanel,BorderLayout.WEST);

        messageDim = new Dimension(380, 700);
        currentMessagePanel = new MessagePanel(this, "Tyler", "Not Tyler",messageDim);
        frame.add(currentMessagePanel, BorderLayout.EAST);
    }
    public void createChatSession(String user){
        if(currentMessagePanel != null)
            currentMessagePanel.setVisible(false);

        new MessagePanel(this, dbHandler.getName(), user, messageDim);
        messagePanel.put(user, currentMessagePanel) ;
        currentMessagePanel.setPreferredSize(messageDim);

        frame.revalidate();
        frame.repaint();
    }
    /**
     * removes a user from the chatframe map and panel
     * @param user
     */
    private void clearChatFrame(String user){
        frame.remove(messagePanel.get(user));
        messagePanel.remove(user);
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
            dbHandler = new DatabaseHandler(loginPanel.getPath(), loginPanel.getUsername(), loginPanel.getPassword());
            if (!dbHandler.isConnected()){
                JOptionPane.showMessageDialog(null,
                        "Error connecting to database, Check filepath.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            clientListener.logon(dbHandler.getName(), dbHandler.getHash());
        }catch(Exception ioe){
            System.err.println("IOException while logging on to the server.");
        }
    }

    /**
     * the register user button was pressed.
     */
    public void registerUsername() {
        try {
            clientListener.createAccount(manager.getUserIP(),
                    registerPanel.getUsername(),
                    //todo the digest needs to be different
                    Arrays.toString(md.digest((registerPanel.getUsername() + registerPanel.getPassword()).getBytes())));
        } catch (IOException e) {
            e.printStackTrace();
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
            System.err.println("Error with friend request. User request: "+ username);
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
        messagePanel.get(from).addMessage(from, msg);
        if(from.equals(dbHandler.getName()))
            peerListener.get(to).message(from,to,msg);
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
        clearChatFrame(user);
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
                clientListener.friendRequest(from, dbHandler.getHash(), to, 1);
            } else {
                //no
                clientListener.friendRequest(from, dbHandler.getHash(), to, 0);
            }
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
        dbHandler.updateFriendIP(user, IP);

        clientListener.initConversation(dbHandler.getName(), dbHandler.getHash(), user);
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
    public void initConversation(String from, String to) throws IOException {
        if(!dbHandler.isFriend(from)) return;
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to start a conversation with " + from,
                "Init chat conversation",
                dialogButton);
        if(dialogResult == 0) {
            //yes
            peerListener.put(from, manager.createClientConnection(dbHandler.getFriendIP(from)));
            start(from);
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
            //todo we need some way to create the database file.
            //dbHandler = new DatabaseHandler();
            //todo Why do we need IP
            dbHandler.init(user,manager.getServerIP());
            createDefaultChatFrame();
        } else
            JOptionPane.showMessageDialog(null, "Username is taken, select a new one.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
    }

    //start of WindowListener interface
    public void windowOpened(WindowEvent e) {}

    public void windowClosing(WindowEvent e) {}

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     *
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {
        try {
            clientListener.logoff(dbHandler.getName(), dbHandler.getHash());
            for(String f: messagePanel.keySet())
                stop(f);

        }catch(IOException ioe){
            System.err.println("i dont think you will see this. Error while closed.");
        }
    }

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}
}
