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

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/

import java.net.Inet4Address;
import java.net.UnknownHostException;

class Server
{
	public static void main(String args[]) throws Exception
	{
		if(args.length != 2) useage();
		String host = "";
		int port = 5432; //port is 5432

		//get host address
		try
		{
			Inet4Address address = (Inet4Address) Inet4Address.getByName("localhost");
			host = address.getHostName();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}

		//create database handler and manager
		DatabaseHandlerServer db = new DatabaseHandlerServer();
		ManagerServer manager = new ManagerServer(host, port);
		manager.setDatabaseHandler(db);
		manager.run();
	}

	private static void useage()
	{
		System.err.println("Usage: java Server <host_address> <port>");
		throw new IllegalArgumentException();
	}
}