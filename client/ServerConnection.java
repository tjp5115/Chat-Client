/*
* ServerConnection.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

//imports go here
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.net.ssl.*;
import java.net.*;

/* Client connection to the server.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ServerConnection implements ClientListener{

    private SSLSocket sok;
    private ServerListener serverListener;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean debugMode;

    /**
     * constructor for the server connection
     * @param _sok - SSL socket
     * @param _serverListener - database reference.
     * @throws IOException
     */
    public ServerConnection(SSLSocket _sok, ServerListener _serverListener)throws IOException{
        sok = _sok;
        out = new DataOutputStream (sok.getOutputStream());
        in = new DataInputStream (sok.getInputStream());
        serverListener = _serverListener;
        debugMode = false;
        new ReaderThread().start();
    }

    public ServerConnection(SSLSocket _sok, ServerListener _serverListener, boolean debug)throws IOException{
        sok = _sok;
        out = new DataOutputStream (sok.getOutputStream());
        in = new DataInputStream (sok.getInputStream());
        serverListener = _serverListener;
        debugMode = debug;
        new ReaderThread().start();
    }

    public void end() throws IOException{
        sok.close();
    }

    /**
     * friend request between two users
     *
     * @param from   - requester
     * @param to     -  requestee
     * @param status - status of request
     *               0 - pending
     *               1 - accept
     *               2 - reject
     * @throws IOException
     */
    @Override
    public void friendRequest(String from, String from_hash, String to, int status) throws IOException {
        out.writeByte ('F');
        out.writeUTF(from);
        out.writeUTF(from_hash);
        out.writeUTF(to);
        out.writeByte(status);
        debugPrint("--> F " + from + " " +from_hash + " " + to  + " " + status);
        out.flush();
    }

    @Override
    public void add(String name, String hash, ServerListener c) {

    }

    @Override
    public void add(String ip, ServerListener c) {

    }

    /**
     * creates an account for a user
     * @param ip
     * @param username @throws IOException
     * @param username_hash - hash to verify from.
     * @parm: String - the ip of this user
     */
    @Override
    public void createAccount(String ip, String username, String username_hash) throws IOException {
        debugPrint(sok.toString());
        out.writeByte('R');
        out.writeUTF(ip);
        out.writeUTF(username);
        out.writeUTF(username_hash);
        debugPrint("--> R " + ip + " " + username + " " + username_hash);
        out.flush();
    }

    /**
     * log on trigger
     *
     * @param user - user to log on
     * @param user_hash - hash to verify from.
     * @throws IOException
     */
    @Override
    public void logon(String user, String user_hash) throws IOException {
        out.writeByte('J');
        out.writeUTF(user);
        out.writeUTF(user_hash);
        debugPrint("--> J " + user + " " + user_hash);
        out.flush();
    }

	/*gets client ip

	*/
	public String IP(){
		 String addr = null;
		 try {
		 addr = sok.getLocalAddress().getLocalHost().getHostAddress();
         }
		 catch (UnknownHostException e) {
		 e.printStackTrace();
		 }
	    System.out.println(addr + "B");
        System.out.println(sok);
		 return addr;

	    }


    /**
     * log off trigger
     *
     * @param user - user to log off
     * @param user_hash - hash to verify from.
     * @throws IOException
     */
    @Override
    public void logoff(String user, String user_hash) throws IOException {
        out.writeByte('Q');
        out.writeUTF(user);
        out.writeUTF(user_hash);
        debugPrint("--> Q " + user + " " + user_hash);
        out.flush();
        sok.close();
    }

    /**
     * initiate a conversation between two clients
     *
     * @param from - initiator
     * @param from_hash - initiator
     * @param to   - responder
     * @throws IOException
     */
    @Override
    public void initConversation(String from, String from_hash, String to, String port) throws IOException {
        out.writeByte('S');
        out.writeUTF(from);
        out.writeUTF(from_hash);
        out.writeUTF(to);
        out.writeUTF(port);
        debugPrint("--> S " + from + " " + from_hash + " " + to + " " + port);
        out.flush();
    }

    /**
     * get the ip of a user
     *
     * @param from - who the request is from
     * @param from_hash - hash to verify from.
     * @param to   - what user to get the IP from
     * @throws IOException
     */
    @Override
    public void getIP(String from, String from_hash, String to) throws IOException {
        out.writeByte('G');
        out.writeUTF(from);
        out.writeUTF(from_hash);
        out.writeUTF(to);
        debugPrint("--> G " + from + " " + from_hash + " " + to);
        out.flush();
    }

    /**
     * reject message from the user
     *
     * @param from      -- who rejected
     * @param from_hash
     * @param to        -- to
     */
    @Override
    public void rejectConversation(String from, String from_hash, String to) throws IOException{
        out.writeByte('Z');
        out.writeUTF(from);
        out.writeUTF(from_hash);
        out.writeUTF(to);
        debugPrint("--> Z " + from + " " + from_hash + " " + to);
        out.flush();
    }

    /**
     * Request to remove the friend
     *
     * @param from      user who wants to remove
     * @param from_hash - verify user
     * @param friend    - friend to remove.
     * @throws IOException
     */
    @Override
    public void requestRemoveFriend(String from, String from_hash, String friend) throws IOException {
        out.writeByte('M');
        out.writeUTF(from);
        out.writeUTF(from_hash);
        out.writeUTF(friend);
        out.flush();
    }

    public void debugPrint(String message)
    {
        if(debugMode)
        {
            System.out.println(message);
        }
    }

    /**
     * Class ReaderThread receives messages from the network, decodes them, and
     * invokes the proper methods to process them.
     *
     * @author:  Alan Kaminsky
     * @contributers: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
     * @version 3-23-2016
     */
    private class ReaderThread
            extends Thread
    {
        public void run()
        {
            try
            {
                for (;;)
                {
                    String to,from,username,ip,error;
                    int status;
                    byte b = in.readByte();
                    switch (b)
                    {
                        case 'F':
                            from = in.readUTF();
                            to = in.readUTF();
                            status = in.readByte();
                            debugPrint("<-- F " + from + " " + to + " " + status);
                            serverListener.userFriendStatus(from, to, status);
                            break;
                        case 'G':
                            username = in.readUTF();
                            ip = in.readUTF();
                            debugPrint("<-- G " + username + " " + ip);
                            serverListener.IP(username, ip);
                            break;
                        case 'E':
                            error = in.readUTF();
                            debugPrint("<-- E " + error);
                            serverListener.error(error);
                            break;
                        case 'S':
                            from = in.readUTF();
                            to = in.readUTF();
                            String port = in.readUTF();
                            debugPrint("<-- S " + from + " " + to + " " + port);
                            serverListener.initConversation(from,to, port);
                            break;
                        case 'C':
                            username = in.readUTF();
                            status = in.readByte();
                            debugPrint("<-- C " + username + " " + status);
                            serverListener.createAccountResponse(username, status);
                            break;
                        case 'Z':
                            from = in.readUTF();
                            debugPrint("<-- Z " + from);
                            serverListener.rejectedConverstation(from);
                            break;
                        case 'R':
                            debugPrint("<-- R ");
                            serverListener.loginSuccess();
                            break;
                        case 'M':
                            from = in.readUTF();
                            to = in.readUTF();
                            serverListener.removeFriend(from, to);
                            break;
                        default:
                            System.err.println ("Bad message: " + b);
                            break;
                    }
                }
            }
            catch (IOException exc)
            {
            }
            finally
            {
                try
                {
                    sok.close();
                }
                catch (IOException exc)
                {
                }
            }
        }
    }

}

