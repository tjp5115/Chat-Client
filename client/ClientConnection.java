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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javax.net.ssl.*;

/* Client connection between two peers. Used for chat communication.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public class ClientConnection implements PeerListener{

    private SSLSocket sok;
    private SSLServerSocket serverSocket;
    private PeerListener peerListener;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean debugMode = true;

    /**
     * constructor for the server connection
     * @param sok - SSL socket
     * @param peerListener - database reference.
     * @throws IOException
     */
    public ClientConnection(SSLSocket sok, PeerListener peerListener) throws IOException
    {
        this.sok = sok;
        out = new DataOutputStream (sok.getOutputStream());
        in = new DataInputStream (sok.getInputStream());
        this.peerListener = peerListener;
        new ReaderThread().start();
    }

    public ClientConnection(SSLServerSocket serverSocket, PeerListener peerListener, String to) throws IOException{
        this.peerListener = peerListener;
        this.serverSocket = serverSocket;
        sok = (SSLSocket)serverSocket.accept();
        serverSocket.close();
        peerListener.start(to);
        out = new DataOutputStream(sok.getOutputStream());
        in = new DataInputStream(sok.getInputStream());
        new ReaderThread().start();
    }

    public ClientConnection(SSLSocket sok, PeerListener peerListener, boolean debug) throws IOException
    {
        this.sok = sok;
        out = new DataOutputStream (sok.getOutputStream());
        in = new DataInputStream (sok.getInputStream());
        this.peerListener = peerListener;
        debugMode = debug;
        new ReaderThread().start();
    }

    public ClientConnection(SSLServerSocket serverSocket, PeerListener peerListener, String to, boolean debug) throws IOException{
        this.peerListener = peerListener;
        this.serverSocket = serverSocket;
        sok = (SSLSocket) serverSocket.accept();
        serverSocket.close();
        peerListener.start(to);
        debugMode = debug;
        out = new DataOutputStream(sok.getOutputStream());
        in = new DataInputStream(sok.getInputStream());
        new ReaderThread().start();
    }

    public boolean isClosed(){
        return sok.isClosed();
    }

    /**
     * set the peer client to the client connection
     * @param _peerListener - peer listener
     */
    public void setPeerListener(PeerListener _peerListener)
    {
        peerListener = _peerListener;
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
        debugPrint("--> M " + from + " " + to + " " + msg);
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
        debugPrint("--> S " + user);
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
        if(serverSocket != null){
            serverSocket.close();
        }
        if(sok != null){
            sok.close();
        }

    }

    private void debugPrint(String message)
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
                    String to,from,username,message;
                    byte b = in.readByte();
                    switch (b)
                    {
                        case 'M':
                            from = in.readUTF();
                            to = in.readUTF();
                            message = in.readUTF();
                            debugPrint("<-- M " + from + " " + to + " " + message);
                            peerListener.message(from, to, message);
                            break;
                        case 'S':
                            username = in.readUTF();
                            debugPrint("<-- S " + username);
                            peerListener.stop(username);
                            break;
                        case 'Q':
                            username = in.readUTF();
                            debugPrint("<-- Q " + username);
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
                    if(serverSocket != null)
                        serverSocket.close();
                }
                catch (IOException exc)
                {
                }
            }
        }
    }

}
