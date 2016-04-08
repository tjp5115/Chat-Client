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
import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* Client connection to the server.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ServerConnection implements ClientListener{

    private SSLSocket sok;
    private ServerListener serverListener;
    private DataOutputStream out;
    private DataInputStream in;

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
        new ReaderThread().start();
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
        out.flush();
    }

    @Override
    public void add(String name, String hash, ToClient c) {

    }

    @Override
    public void add(String ip, ToClient c) {

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
        out.writeByte('R');
        out.writeUTF(ip);
        out.writeUTF(username);
        out.writeUTF(username_hash);
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
        out.flush();
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
        out.flush();
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
        out.writeUTF(from);
        out.writeUTF(from_hash);
        out.writeUTF(to);
        out.flush();
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
                    String to,from,username,ip,error, hash;
                    int status;
                    byte b = in.readByte();
                    switch (b)
                    {
                        case 'F':
                            from = in.readUTF();
                            to = in.readUTF();
                            status = in.readByte();
                            serverListener.userFriendStatus(from, to, status);
                            break;
                        case 'G':
                            username = in.readUTF();
                            ip = in.readUTF();
                            serverListener.IP(username, ip);
                            break;
                        case 'E':
                            error = in.readUTF();
                            serverListener.error(error);
                            break;
                        case 'S':
                            from = in.readUTF();
                            to = in.readUTF();
                            String port = in.readUTF();
                            serverListener.initConversation(from,to, port);
                            break;
                        case 'R':
                            username = in.readUTF();
                            status = in.readByte();
                            serverListener.createAccountResponse(username, status);
                            break;
                        case 'Z':
                            from = in.readUTF();
                            serverListener.rejectedConverstaion(from);
                            break;
                        default:
                            System.err.println ("Bad message");
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

