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
        ManagerServer manager = null;
        if(args.length == 1){
		    manager = new ManagerServer(args[0],PORT,false);
        }//end if 
        else if(args.length == 2){
            if(args[1].equals("true")){
                manager = new ManagerServer(args[0],PORT,true);   
            }
            else if(args[1].equals("false")){
                manager = new ManagerServer(args[0],PORT,false);
            }
            else{
                usage();
            }
        }
        else{
            usage();
        }
		manager.setDatabaseHandler(db);
		manager.run();
	}

	private static void usage()
	{
		System.err.println("Usage: java Server <Host ip> [<debug(true/false)>]");
		throw new IllegalArgumentException();
	}
}
