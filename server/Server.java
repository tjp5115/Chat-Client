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
	public static void main(String args[]) throws Exception
	{
		if(args.length != 2) usgae();
		String host = args[0];
		int port = Integer.parseInt(args[1]);

		//create database handler and manager
		DatabaseHandler db = new DatabaseHandler();
		Manager manager = new Manager(host, port);
		manager.setDatabaseHandler(db);
		db.setManager(manager);
		manager.run();
	}
}