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

class Server
{
	private final static int PORT = 5432;
	public static void main(String args[]) throws Exception
	{
		//create database handler and manager
		DatabaseHandlerServer db = new DatabaseHandlerServer();
		ManagerServer manager = new ManagerServer("localhost",PORT);
		manager.setDatabaseHandler(db);
		manager.run();
	}

	private static void usage()
	{
		System.err.println("Usage: java Server <host_address> <port>");
		throw new IllegalArgumentException();
	}
}