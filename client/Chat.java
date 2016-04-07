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

class Chat
{
	public static void main(String args[])
	{
		
		//create GUI
		ChatFrame cf = new ChatFrame();

		//create Manager, and pass the database and GUI to manager
		Manager manager = new Manager(cf);
		manager.run();
	}
}