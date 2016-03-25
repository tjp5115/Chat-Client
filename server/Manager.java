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
import java.util.ArrayList;
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
	private DatabaseHandler dbHandler;
	private ArrayList<ToClient> clientList;
	private String host;
	private int port;
	private final String KEY_FILE_NAME = "";

	//the password used to check the integrity of the keystore, the password used to unlock the keystore
	private final char[] UNLOCK_KEYSTORE_PASSWORD = "";

	//the password for recovering keys in the keyStore
	private final char[] GET_PRIVATE_KEY_PASSWORD = "";

	public Manager(String host, int port)
	{
		clientList = new ArrayList<ToClient>();
		this.host = host;
		this.port = port;
	}

	public void setDatabaseHandler(DatabaseHandler dh)
	{
		dbHandler = dh;
	}

	public ToClient createConnection()
	{

	}

	// Listening for the initial connection
	public void run()
	{
		//create secure server socket
		//prepare private key and public key
		try
		{
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(new FileInputStream(KEY_FILE_NAME), UNLOCK_KEYSTORE_PASSWORD);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, GET_PRIVATE_KEY_PASSWORD);
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(kmf.getKeyManagers(), null, null);
			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);

			for(;;)
			{
				//receive any SSLsocket
				SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
				//create ToClient object and gives it the SSL socket
				ToClient tc = new ToClient(clientSocket, (ClientListener)dbHandler);
				clientList.add(tc);
			}	
		}
	}
}