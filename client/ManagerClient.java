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
import java.security.*;
import javax.net.ssl.*;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/


class ManagerClient
{
	private ChatFrame GUI;
	private String SERVER_HOST;
	private int SERVER_PORT = 5432;
	private String jksFileName = "keystore.jks";
	private final char KEY_STORE_PS[] = "Chat1234".toCharArray();
	private ServerConnection serverConnection;
	private ClientConnection clientConnection;
	SSLSocket  socket;

	public ManagerClient(ChatFrame inGUI)
	{
		this.GUI = inGUI;
		this.SERVER_HOST = "localhost";
	}

	/**
	 * get ip of the user
	 * @return ip;
	 */
	public String getServerIP()
	{
		return SERVER_HOST;
	}

	public String getUserIP()
	{
		byte []addr = socket.getInetAddress().getAddress();
		StringBuffer out = new StringBuffer();
		for(int i = 0; i < addr.length; ++i)
		out.append(addr[i]+".");
		return out.substring(0,out.length()-1);
	}

	/**
	 * set up the peer connection between two clients. Assumes the establishment process has been completed.
	 * @return
	 */
	//todo
	public PeerListener createClientConnection(String ip, int port)
	{
		//create SSL Socket to other ip
		//SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		PeerListener peer = null;

		try
		{
			//SSLSocket c = (SSLSocket) sf.createSocket(_ip, Integer.parseInt(_port));
			Socket c = new Socket(ip,port);
			peer = new ClientConnection(c, GUI);
		}catch (IOException ioe)
		{
			System.err.println("IOException caught. Exiting");
			ioe.printStackTrace();
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
	public ServerSocket createClientServerConnection()
	{
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream(jksFileName), KEY_STORE_PS);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, KEY_PS);
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(kmf.getKeyManagers(), null, null);
		SSLServerSocketFactory ssf = sc.getServerSocketFactory();

		SSLServerSocket serverSocket = null;
		try
		{
			SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(0);
		}
		catch(Exception e)
		{
			System.err.println("Exception caught while creating Client Server connection");
		}

		return serverSocket;
	}

	public PeerListener createClientConnection(ServerSocket serverSocket, String to)
	{
		try {
			return new ClientConnection(serverSocket, GUI, to);
		} catch (IOException e) {
			System.err.println("IOException while creating a clientConnection in the manager.");
		}
		return null;
	}

	//send initial message to Server when the program start
	public void run()
	{
		try
		{
			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(new FileInputStream(jksFileName), KEY_STORE_PS);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(keystore);
			SSLContext context = SSLContext.getInstance("TLS");
			TrustManager[] trustManagers = tmf.getTrustManagers();

			context.init(null, trustManagers, null);
			SSLSocketFactory sf = context.getSocketFactory();
			socket = (SSLSocket) sf.createSocket(SERVER_HOST, SERVER_PORT);
			serverConnection = new ServerConnection(socket, GUI);
			GUI.setClientListener(serverConnection);
		}catch (IOException ioe){
			System.err.println("IOException caught while creating connection to server. Exiting");
			System.exit(1);
		}
	}
}