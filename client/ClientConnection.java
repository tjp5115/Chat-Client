/*
* ClientConnection.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

//imports go here

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* Client connection between two peers. Used for chat communication.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ClientConnection implements PeerListener{

    private SSLSocket sok;
    private SSLServerSocket ssok;
    private PeerListener peerListener;
    private DataOutputStream out;
    private DataInputStream in;

    /**
     * constructor for the server connection
     * @param _sok - SSL socket
     * @param _peerListener - database reference.
     * @throws IOException
     */
    public ClientConnection(SSLSocket _sok, PeerListener _peerListener) throws IOException{
        sok = _sok;
        out = new DataOutputStream (sok.getOutputStream());
        in = new DataInputStream (sok.getInputStream());
        peerListener = _peerListener;
        new ReaderThread().start();
    }

    public ClientConnection(SSLServerSocket ssok, PeerListener peerListener) throws IOException{
        SSLSocket sok  = (SSLSocket)(ssok.accept());
        out = new DataOutputStream (sok.getOutputStream());
        in = new DataInputStream (sok.getInputStream());
        this.peerListener = peerListener;
        new ReaderThread().start();
    }

    /**
     * Send a message to a user.
     *
     * @param from - who the message is from.
     * @param to   - who the message is too.
     * @param msg  - the message.
     */
    @Override
    public void message(String from, String to, String msg) throws IOException {
        out.writeByte('M');
        out.writeUTF(from);
        out.writeUTF(to);
        out.writeUTF(msg);
        out.flush();
    }

    /**
     * Requests a chat session to begin between two users.
     *
     * @param user - user requesting a chat session.
     */
    @Override
    public void start(String user) throws IOException {
        out.writeByte('S');
        out.writeUTF(user);
        out.flush();
    }

    /**
     * Stops a chat session between two users.
     *
     * @param user - user to stop the session from.
     */
    @Override
    public void stop(String user) throws IOException {
        out.writeByte('Q');
        out.writeUTF(user);
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
                    String to,from,username,message;
                    byte b = in.readByte();
                    switch (b)
                    {
                        case 'M':
                            from = in.readUTF();
                            to = in.readUTF();
                            message = in.readUTF();
                            peerListener.message(from, to, message);
                            break;
                        case 'S':
                            username = in.readUTF();
                            peerListener.stop(username);
                            break;
                        case 'Q':
                            username = in.readUTF();
                            peerListener.stop(username);
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
