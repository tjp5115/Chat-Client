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
import java.util.Enumeration;



/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/


class ManagerClient
{
	private ChatFrame GUI;
	//todo need to be able to specify this.
	private String SERVER_HOST;
	private int SERVER_PORT = 5432;
	private String jksFileName = "keystore.jks";
	private final char KEY_STORE_PS[] = "Chat1234".toCharArray();
    private final char KEY_PS[] = "Chat5678".toCharArray();
	private ServerConnection serverConnection;
	private ClientConnection clientConnection;
	SSLSocket  socket;

	public ManagerClient(ChatFrame inGUI)
	{
		this.GUI = inGUI;
	}

	public void setServerIP(String host){
		SERVER_HOST = host;
	}
	/**
	 * get ip of the user
	 * @return ip;
	 */
	public String getServerIP()
	{
		return SERVER_HOST;
	}

	public String getUserIP() {
 		String addr = null;
        try{
        NetworkInterface ni = NetworkInterface.getByName("eth0");
        Enumeration<InetAddress> net = ni.getInetAddresses();
        while(net.hasMoreElements()){
            InetAddress i = net.nextElement();
            if(!i.isLinkLocalAddress()){
                addr = i.getHostAddress();
            }
        }
        }
        catch(Exception e){
               
        }
 		System.out.println(addr + "!!!");
 		return addr;
 	}

	/**
	 * set up the peer connection between two clients. Assumes the establishment process has been completed.
	 * @return
	 */
	//todo
	public PeerListener createClientConnection(String ip, int port)
	{
    PeerListener peer = null;
	try{
        //create SSL Socket to other ip
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(jksFileName), KEY_STORE_PS);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(keystore);
		SSLContext context = SSLContext.getInstance("TLS");
		TrustManager[] trustManagers = tmf.getTrustManagers();

		context.init(null, trustManagers, null);
		SSLSocketFactory sf = context.getSocketFactory();
		SSLSocket socket = (SSLSocket) sf.createSocket(ip, port);
		peer = new ClientConnection(socket, GUI);
		}
        catch (IOException ioe)
		{
			System.err.println("IOException caught. Exiting");
			ioe.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			System.err.println("NumberFormatException caught. Exiting");
		}
        catch(Exception e){
            System.err.println("Probly error with ssl");
        }
		return peer;
	}

	/**
	 * set up the peer connection between two clients. Assumes the establishment process has been completed.
	 * @return
	 */
	public SSLServerSocket createClientServerConnection(){
	SSLServerSocket serverSocket = null;
    try{
        KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream(jksFileName), KEY_STORE_PS);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, KEY_PS);
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(kmf.getKeyManagers(), null, null);
		SSLServerSocketFactory ssf = sc.getServerSocketFactory();
	    serverSocket = (SSLServerSocket) ssf.createServerSocket(0);
	}
	catch(Exception e)
	{
			System.err.println("Exception caught while creating Client Server connection");
	}

		return serverSocket;
	}

	public PeerListener createClientConnection(SSLServerSocket serverSocket, String to)
	{
		try {
			return new ClientConnection(serverSocket, GUI, to);
		} catch (IOException e) {
			System.err.println("IOException while creating a clientConnection in the manager.");
            e.printStackTrace();
		}
		return null;
	}

	//send initial message to Server when the program start
	public void run() throws IOException
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
            ioe.printStackTrace();
			System.exit(1);
		}
        catch(Exception e){
            System.err.println("SSL error?");
        }
	}
}
