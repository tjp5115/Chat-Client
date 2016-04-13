/*
 * ToClient.java
 *
 * Version:
 *  $Id$
 *
 * Revisions:
 *  $Log$
 */

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class ToClient implements ServerListener{

	private Socket sok;
	private ClientListener clientListener;
	private DataOutputStream out;
	private DataInputStream in;
	private ToClient toc;
    private boolean debug;

	/**
	 *  Constructor for the ToClient connection
	 * @param sok - SSL socket
	 * @param clientListener - database reference;
	 * @throws IOException
	 */
	ToClient(Socket sok, ClientListener clientListener) throws IOException{
		debug = false;
        this.sok = sok;
		out = new DataOutputStream (sok.getOutputStream());
		in = new DataInputStream (sok.getInputStream());
		this.clientListener = clientListener;
		toc = this;
		new ReaderThread().start();
	}


	/**
	 *  Constructor for the ToClient connection
	 * @param sok - SSL socket
	 * @param clientListener - database reference;
     * @param boolean - turn debug on
	 * @throws IOException
	 */
	ToClient(Socket sok, ClientListener clientListener, boolean b) throws IOException{
		debug = b;
        this.sok = sok;
		out = new DataOutputStream (sok.getOutputStream());
		in = new DataInputStream (sok.getInputStream());
		this.clientListener = clientListener;
		toc = this;
		new ReaderThread().start();
	}

	/*this method returns the ssl socket of the toclient
	@post: sslsocket- the connection
	*/
	public Socket getSok(){
		return sok;
	}//end

    /*this method returns the IP of the computer connected on the other side(client)
    @post: String rep of IP ex: "192.168.254.254"

    */
    public String getIP(){
		byte []addr = sok.getInetAddress().getAddress();
		StringBuffer out = new StringBuffer();
		for(int i = 0; i < addr.length; ++i)
			out.append(addr[i]+".");
		return out.substring(0, out.length() - 1);
	}//end

	/*overrides equals
	@override
	*/
	public boolean equals(final Object obj) {
		ToClient t = (ToClient)obj;
		return sok.equals(t.getSok());

	}//end



    /**
     * request for a friend from a given user.
     * @param from - requestee
     * @param to - requested.
     * @param status - status of the friend request.
     * @throws IOException
     */
    public void userFriendStatus(String from, String to, int status) throws IOException{
		out.writeByte ('F');
		out.writeUTF(from);
		out.writeUTF(to);
		out.writeByte(status);
		print("--> F " + from + " " + to + " " + status);
		out.flush();
	}//end user


	/*over rides userFriendStatus
	@parm: the whole message

	*/
	public void userFriendStatus(String message)throws IOException{
		out.writeByte('F');
		out.writeUTF(message);
		out.flush();
	}

    /**
     * return the IP of a user
     * @param user - user name of the IP
     * @param IP - IP address of the user.
     * @throws IOException
     */
    public void IP(String user, String IP) throws IOException{
		out.writeByte ('G');
		out.writeUTF(user);
		out.writeUTF(IP);
	    print("--> G " + user + " " + IP);
		out.flush();
	}//end ip

    /**
     * return an error.
     * @param error - String noting the error that took place.
     * @throws IOException
     */
    public void error(String error) throws IOException{
		out.writeByte('E');
		out.writeUTF(error);
		print("--> E " + error);
		out.flush();
	}//end error

    /**
     * successful login.
     * @throws IOException
     */
    public void loginSuccess() throws IOException {
		out.writeByte('R');
	    print("--> R " );
		out.flush();
	}//end

	/**
	 * Message that requests a user remove the friend
	 *
	 * @param requester - the user who requested the remove
	 * @param friend    - the friend that was removed.
	 */
	@Override
	public void removeFriend(String requester, String friend) throws IOException{
		out.writeByte ('M');
		out.writeUTF(requester);
		out.writeUTF(friend);
		print("--> M " + requester + " " + friend );
		out.flush();
	}

	/**
     * initiate a conversation between two clients
     * @param from - initiator
	 * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to, String port) throws IOException{
		out.writeByte ('S');
		out.writeUTF(from);
		out.writeUTF(to);
		out.writeUTF(port);
		print("--> S " + from + " " + to + " " + port);
		out.flush();
	}

    /*response from server on either or not username was taken
    1 - you now have the username
    0 - that username is already taken

    */
    public void createAccountResponse(String user, int status) throws IOException {
		out.writeByte ('C');
		out.writeUTF(user);
		out.writeByte(status);
		print("--> C " + user + " " + status );
		out.flush();
	}//end

    /**
     * a client has reject your conversation
     * @param user - 'friend' who rejected.
     */
    public void rejectedConverstation(String user) throws IOException{
		out.writeByte('Z');
		out.writeUTF(user);
		print("--> Z " + user + " " );
		out.flush();
	}

    /*prints if debug is true

    */
    private void print(String s){
        if(debug){
            System.out.println(s);
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
					String to,from,username,ip,hash;
					int status;
					byte b = in.readByte();
					switch (b)
					{
						case 'F':
							from = in.readUTF();
							hash = in.readUTF();
							to = in.readUTF();
							status = in.readByte();
							print("<-- F " + from+ " " + hash+ " " + to + " " + status);
							clientListener.friendRequest(from,hash,to,status);
							break;
						case 'R':
							ip = in.readUTF();
							username = in.readUTF();
							hash = in.readUTF();
							print("<-- R " + ip + " " + username + " " + hash );
							clientListener.add(ip, toc);
							clientListener.createAccount(ip, username, hash);
							break;
						case 'J':
							username = in.readUTF();
							hash = in.readUTF();
							print("<-- J " + username + " " + hash );
							clientListener.add(username, hash, toc);
							clientListener.logon(username, hash);
							break;
						case 'Q':
							username = in.readUTF();
							hash = in.readUTF();
							print("<-- Q " + username + " " + hash );
							clientListener.logoff(username, hash);
							break;
						case 'S':
							from = in.readUTF();
							hash = in.readUTF();
							to = in.readUTF();
							String port = in.readUTF();
							print("<-- S " + from+ " " + hash+ " " + to + " " + port);
							clientListener.initConversation(from, hash, to, port);
							break;
						case 'G':
							from = in.readUTF();
							hash = in.readUTF();
							to = in.readUTF();
							print("<-- G " + from+ " " + hash+ " " + to);
							clientListener.getIP(from, hash, to);
							break;
						case 'Z':
							from = in.readUTF();
							hash = in.readUTF();
							to = in.readUTF();
							print("<-- Z " + from+ " " + hash+ " " + to);
							clientListener.rejectConversation(from, hash, to);
							break;
						case 'M':
							from = in.readUTF();
							hash = in.readUTF();
							to = in.readUTF();
							print("<-- M " + from+ " " + hash+ " " + to);
							clientListener.requestRemoveFriend(from, hash, to);
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


}//end class
