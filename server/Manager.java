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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/

class Manager
{
	private DatabaseHandler dbHandler;
	private ArrayList<ToCLient> clientList;
	private String host;
	private int port;

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
		//create ToClient object and gives it the SSL socket
	}
}