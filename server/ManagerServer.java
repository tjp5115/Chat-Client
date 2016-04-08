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
import java.util.ArrayList;
import java.io.*;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/

class ManagerServer
{
	private DatabaseHandlerServer dbHandler;
	private ArrayList<ToClient> clientList;
	private String host;
	private int port;

	public ManagerServer(String host, int port)
	{
		clientList = new ArrayList<ToClient>();
		this.host = host;
		this.port = port;
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
			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);

			for(;;)
			{
				//receive any SSLsocket
				SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
				//create ToClient object and gives it the SSL socket
				ToClient tc = new ToClient(clientSocket, (ClientListener)dbHandler);
				clientList.add(tc);
			}	
		} catch (IOException e) {
			System.err.println("IOException in Server manager");
		}
	}
}