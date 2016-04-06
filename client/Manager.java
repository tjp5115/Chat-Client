/* 
 * filename.java 
 * 
 * Version: 
 * 	$Id$ 
 * 
 * Revisions: 
 * 	$Log$ 
 */

//imports go here
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.ArrayList;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/


class Manager
{
	private ChatFrame GUI;
	private String SERVER_HOST;
	private ServerConnection serverConnection;
	private ClientConnection clientConnection;
	private ArrayList<ClientConnection> clientConnectionList;
	//port 0 for peer to peer

	public Manager(ChatFrame inGUI)
	{
		this.GUI = inGUI;
		this.SERVER_HOST = "";
	}

	/**
	 * get ip of the user
	 * @return ip;
	 */
	public String getIP(){
		return "";
	}
	/**
	 * set up the peer connection between two clients. Assumes the establishment process has been completed.
	 * @param ip
	 * @return
	 */
	//todo
	public PeerListener createClientConnection(String _ip, String _port)
	{
		//create SSL Socket to other ip
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		ClientConnection peer = null;

		try
		{
			SSLSocket c = (SSLSocket) sf.createSocket(_ip, Integer.parseInt(_port));
			clientConnection = new ClientConnection(c);
			peer = new ClientConnection(c);
			peer.setPeerListener(c);
			setPeerListener
		}catch (IOException ioe)
		{
			System.err.println("IOException caught. Exiting");
			System.exit(1);
		}
		catch(NumberFormatException e)
		{
			System.err.println("NumberFormatException caught. Exiting");
		}
		return null;
	}

	//send initial message to Server when the program start
	public void run()
	{
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		try
		{
			SSLSocket c = (SSLSocket) sf.createSocket(SERVER_HOST, 5432);
			serverConnection = new ServerConnection(c, GUI);
			GUI.setClientListener(serverConnection);
		}catch (IOException ioe){
			System.err.println("IOException caught. Exiting");
			System.exit(1);
		}
	}
}