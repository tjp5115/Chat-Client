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

class ManagerServer
{
	private DatabaseHandlerServer dbHandler;
	private int SERVER_HOST;
	private String SERVER_PORT;
	private String jksFileName = "keystore.jks";
	private final char KEY_STORE_PS[] = "Chat1234".toCharArray();
	private final char KEY_PS[] = "Chat5678".toCharArray();

	public ManagerServer(String host, int port)
	{
		this.SERVER_PORT = port;
		this.SERVER_HOST = host;
	}

	public void setDatabaseHandler(DatabaseHandlerServer dh)
	{
		dbHandler = dh;
	}

	// Listening for the initial connection
	public void run()
	{
		//create secure server socket
		//prepare private key and public key
		try
		{
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(jksFileName), KEY_STORE_PS);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, KEY_PS);
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
			System.out.println(serverSocket.getInetAddress().toString());

			for(;;)
			{
				//receive any SSLsocket
				SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
				System.out.println("Accepted.");
				//create ToClient object and gives it the SSL socket
				new ToClient(clientSocket, dbHandler);
			}
		} catch (IOException e) {
			System.err.println("IOException in Server manager");
		}
	}
}