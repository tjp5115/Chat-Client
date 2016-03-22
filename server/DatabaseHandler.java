/*
* DataBaseHandler.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

import java.util.HashMap;
import java.io.IOException;
import java.sql.*;

/* This class handles all requests from the clients, it also
makes the nessitary SQL calls to the database.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class DatabaseHandler implements ClientListener{

	HashMap<String, ToClient> cons;
	Connection conn;


	/*Default constructor
	makes cons and conn

	*/
	public DatabaseHandler(){
		cons = new HashMap<String, ToClient>();
		try {
			//This tells it to use the h2 driver
			Class.forName("org.h2.Driver");

			//creates the connection
			conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "");
		}//end try
		catch (SQLException | ClassNotFoundException e) {
			System.out.println("error creating the conn for database");
			e.printStackTrace();
		}//end catch

	}//end constructor

	/*this method adds a connection

	*/
	public void add(String name, ToClient c){
		cons.put(name,c);
	}//end add


	/**
     * friend request between two users
     * @param from - requester
     * @param to -  requestee
     * @param status - status of request
     *              0 - pending
     *              1 - accept
     *              2 - reject
     * @throws IOException
     */
    public void friendRequest( String from, String to, int status) throws IOException{
		ToClient t = cons.get(to);
		//client isn't online
		if(t == null){

		}//end if

		//if client is online
		else{

		}//end
	}//end friend

    /**
     * creates an account for a user
     * @param username
     * @throws IOException
     */
    public void createAccount(String username) throws IOException{

	}//end create

    /**
     * initiates a connection between two nodes
     * @throws IOException
     */
    public void initConnection() throws IOException{

	}//end init

    /**
     * log on trigger
     * @param user - user to log on
     * @throws IOException
     */
    public void logon(String user) throws IOException{

	}//endlogon

    /**
     * log off trigger
     * @param user - user to log off
     * @throws IOException
     */
    public void logoff(String user) throws IOException{

	}//end logoff

    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to) throws IOException{

	}//end init

    /**
     * get the ip of a user
     * @param from - who the request is from
     * @param to - what user to get the IP from
     * @throws IOException
     */
    public void getIP(String from, String to) throws IOException{

	}//end getIP
}//end class