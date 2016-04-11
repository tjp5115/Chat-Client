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

	/**
	 *  Constructor for the ToClient connection
	 * @param sok - SSL socket
	 * @param clientListener - database reference;
	 * @throws IOException
	 */
	ToClient(Socket sok, ClientListener clientListener) throws IOException{
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
		//out.writeUTF(user_hash);
		out.writeUTF(IP);
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
		out.flush();
	}//end error

    /**
     * successful login.
     * @throws IOException
     */
    public void loginSuccess() throws IOException {
		out.writeByte('R');
		out.flush();
	}//end

    /**
     * initiate a conversation between two clients
     * @param from - initiator
	 * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to, String port) throws IOException{
		out.writeByte ('S');
		out.writeUTF(from);
		//out.writeUTF(from_hash);
		out.writeUTF(to);
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
		out.flush();
	}//end

    /**
     * a client has reject your conversation
     * @param user - 'friend' who rejected.
     */
    public void rejectedConverstaion(String user) throws IOException{
		out.writeByte('Z');
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
							clientListener.friendRequest(from,to,hash,status);
							break;
						case 'R':
							ip = in.readUTF();
							username = in.readUTF();
							hash = in.readUTF();
							System.out.println(ip + " " + username + " " + hash );
							clientListener.add(ip, toc);
							clientListener.createAccount(ip, username, hash);
							break;
						case 'J':
							username = in.readUTF();
							hash = in.readUTF();
							System.out.println(username + " " + hash );
							clientListener.add(username, hash, toc);
							clientListener.logon(username, hash);
							break;
						case 'Q':
							username = in.readUTF();
							hash = in.readUTF();
							clientListener.logoff(username, hash);
							break;
						case 'S':
							from = in.readUTF();
							hash = in.readUTF();
							to = in.readUTF();
							String port = in.readUTF();
							clientListener.initConversation(from, hash, to, port);
							break;
						case 'G':
							from = in.readUTF();
							hash = in.readUTF();
							to = in.readUTF();
							clientListener.getIP(from, hash, to);
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