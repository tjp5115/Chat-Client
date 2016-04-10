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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/

class ManagerServer
{
	private DatabaseHandlerServer dbHandler;
	private int port;
	private String host;

	public ManagerServer(String host, int port)
	{
		this.port = port;
		this.host = host;
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
			ServerSocket serverSocket = new ServerSocket();
			serverSocket.bind (new InetSocketAddress (host, port));
			System.out.println(serverSocket.getInetAddress().toString());

			for(;;)
			{
				//receive any SSLsocket
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted.");
				//create ToClient object and gives it the SSL socket
				new ToClient(clientSocket, dbHandler);
			}
		} catch (IOException e) {
			System.err.println("IOException in Server manager");
		}
	}
}