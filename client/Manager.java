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
import java.io.*;
import java.net.*;
import java.net.ssl.*;
import java.security.*;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/


class Manager
{
	private static final ChatFrame GUI;
	private static final String SERVER_HOST;
	private ServerConnection serverConnection;
	private ArrayList<ClientConnection> clientConnectionList;
	//port 0 for peer to peer

	public Manager(ChatFrame inGUI)
	{
		this.GUI = inGUI;
		this.SERVER_HOST = "";
	}

	public void createClientConnection()
	{

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
			//send initial connection to server
			serverConnection.initConnection();
		}
	}
}