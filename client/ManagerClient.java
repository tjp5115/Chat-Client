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
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/


class ManagerClient
{
	private ChatFrame GUI;
	private String SERVER_HOST;
	private ServerConnection serverConnection;
	private ClientConnection clientConnection;

	public ManagerClient(ChatFrame inGUI)
	{
		this.GUI = inGUI;
		this.SERVER_HOST = "";
	}

	/**
	 * get ip of the user
	 * @return ip;
	 */
	public String getServerIP()
	{
		return "";
	}
	
	public String getUserIP()
	{
		return "";
	}

	/**
	 * set up the peer connection between two clients. Assumes the establishment process has been completed.
	 * @return
	 */
	//todo
	public PeerListener createClientConnection(String _ip, String _port)
	{
		//create SSL Socket to other ip
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		PeerListener peer = null;

		try
		{
			SSLSocket c = (SSLSocket) sf.createSocket(_ip, Integer.parseInt(_port));
			peer = new ClientConnection(c, GUI);
		}catch (IOException ioe)
		{
			System.err.println("IOException caught. Exiting");
		}
		catch(NumberFormatException e)
		{
			System.err.println("NumberFormatException caught. Exiting");
		}
		return peer;
	}

	/**
	 * set up the peer connection between two clients. Assumes the establishment process has been completed.
	 * @return
	 */
	public SSLServerSocket createClientServerConnection()
	{
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		SSLServerSocket serverSocket = null;
		try
		{
			serverSocket = (SSLServerSocket) ssf.createServerSocket(0);
		}
		catch(Exception e)
		{
			System.err.println("Exception caught");
		}

		return serverSocket;
	}

	public PeerListener createClientConnection(SSLServerSocket ssk)
	{
		try {
			return new ClientConnection(ssk, GUI);
		} catch (IOException e) {
			System.err.println("IOException while creating a clientConnection in the manager.");
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