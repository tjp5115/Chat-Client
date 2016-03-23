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
		init();
	}//end constructor


	/*this method creates the tables in the database

	*/
	private void init(){
		try{
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE users(USER VARCHAR(255) PRIMARY KEY, IP VARCHAR(25), ONLINE BOOLEAN);");
			stmt.execute("CREATE TABLE messages(MESSAGE VARCHAR(255), FOREIGN KEY(USER) REFERENCES USERS);");
		}//end try
		catch(SQLException e){
			System.out.println("error creating tables");
			e.printStackTrace();
		}//end catch
	}//end init

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
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("INSERT INTO messages VALUES(\"" + from + " " + to + " " + status + "\"," + to+";");
			}//end try
			catch(SQLException e){
				System.out.println("error inserting into messages");
				e.printStackTrace();
			}//end catch
		}//end if

		//if client is online
		else{
			t.userFriendStatus(from, to, status);
		}//end
	}//end friend

    /**
     * creates an account for a user
     * @parm: String - the ip of this user
     * @param username
     * @throws IOException
     */
    public void createAccount(String ip, String username) throws IOException{
		ToClient t = cons.get(ip);
		ToClient n = cons.get(username);
		if(username == null){
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("INSERT INTO users VALUES(\"" + username + "\",\"" + ip + "\",TRUE;");
			}//end try
			catch(SQLException e){
				System.out.println("error inserting into messages");
				e.printStackTrace();
			}//end catch
			cons.remove(ip);
			cons.put(username,t);
			t.createAccountResponse(username,1);
		}//end if
		else{
			t.createAccountResponse(username,0);
		}//end else
	}//end create

    /**
     * initiates a connection between two nodes
     * @throws IOException
     */
    public void initConnection() throws IOException{
		//does nothing
	}//end init

    /**
     * log on trigger
     * @param user - user to log on
     * @throws IOException
     */
    public void logon(String user) throws IOException{
		ToClient t = cons.get(user);
		try{
			Statement stmt = conn.createStatement();
			stmt.execute("UPDATE users " +
				"SET IP=" + t.getIP() +", ONLINE=TRUE"
				+ " WHERE USER=" + user + ";");
		}//end try
		catch(SQLException e){
			System.out.println("error inserting into messages");
			e.printStackTrace();
		}//end catch
	}//endlogon

    /**
     * log off trigger
     * @param user - user to log off
     * @throws IOException
     */
    public void logoff(String user) throws IOException{
		try{
			Statement stmt = conn.createStatement();
			stmt.execute("UPDATE users " +
				"SET IP=\"0.0.0.0\", ONLINE=FALSE"
				+ " WHERE USER=" + user + ";");
		}//end try
		catch(SQLException e){
			System.out.println("error inserting into messages");
			e.printStackTrace();
		}//end catch
	}//end logoff

    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to) throws IOException{
		ToClient t = cons.get(to);
		if(t == null){
			ToClient n = cons.get(from);
			n.error(to + " is not online");
		}//end if
		else{
			t.initConversation(from,to);
		}//end

	}//end init

    /**
     * get the ip of a user
     * @param from - who the request is from
     * @param to - what user to get the IP from
     * @throws IOException
     */
    public void getIP(String from, String to) throws IOException{
		ToClient t = cons.get(to);
		if(t != null){
			try{
				Statement stmt = conn.createStatement();
				ResultSet s = stmt.executeQuery("SELECT IP FROM USERS WHERE USER=" + to + ";");
				t.IP(to,s.getString(1));
			}//end try
			catch(SQLException e){
				System.out.println("error inserting into messages");
				e.printStackTrace();
			}//end catch
		}//end if
		else{
			ToClient n = cons.get(from);
			n.error(to + " is not online");
		}
	}//end getIP
}//end class