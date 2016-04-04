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

		//create Database
		String path = "";
		String username ="";
		String password = "";
		DatabaseHandler dbHandler = new DatabseHandler(path, username, password);
		//connect databse to GUI

		//create Manager, and pass the database and GUI to manager
		Manager manager = new Manager();
		manager.setGUI(cf);
		manager.run();
	}
}