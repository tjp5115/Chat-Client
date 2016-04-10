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
makes the nessitary SQL calls to the database. Before you can use anymethods the toClient
That is making the calls needs to add its self to this class through the add method!

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class DatabaseHandlerServer implements ClientListener{

	HashMap<String, ToClient> cons;
	Connection conn;


	/*Default constructor
	makes cons and conn

	*/
	public DatabaseHandlerServer(){
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
			stmt.execute("CREATE TABLE users(USER VARCHAR(255) PRIMARY KEY, IP VARCHAR(25), ONLINE BOOLEAN, HASH VARCHAR(255));");
			stmt.execute("CREATE TABLE messages(MESSAGE VARCHAR(255), USER VARCHAR(255), FOREIGN KEY(USER) REFERENCES USERS);");
		}//end try
		catch(SQLException e){
			System.out.println("error creating tables");
			e.printStackTrace();
		}//end catch
	}//end init

	/*this method adds a connection
		@parm: String username
		@parm: String hash
		@parm: Toclient the conection to user

	*/
	public synchronized void add(String name, String hash, ToClient c){
		cons.put(name.concat(hash),c);
	}//end add

	/*this method adds a connection
		@parm: String ip
		@parm: Toclient the conection to user

	*/
	public synchronized void add(String ip, ToClient c){
		cons.put(ip,c);
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
    public synchronized void friendRequest(String from, String from_hash, String to, int status) throws IOException{
		if(check(from, from_hash)){
			ToClient t = cons.get(to.concat(from_hash));
			//client isn't online
			if(t == null){
				try{
					Statement stmt = conn.createStatement();
					stmt.execute("INSERT INTO messages VALUES(\"" + from + " " + to + " " + status + "\"," + to+";");
				}//end try
				catch(SQLException e){
					System.out.println("error friend request");
					e.printStackTrace();
				}//end catch
			}//end if

			//if client is online
			else{
				t.userFriendStatus(from, to, status);
			}//end
		}//end if
		else{
			ToClient t = cons.get(to.concat(from_hash));
			t.error("Your not authehtic!");
		}
	}//end friend

    /**
     * creates an account for a user
     * @parm: String - the ip of this user
     * @param username
     * @throws IOException
     */
    public synchronized void createAccount(String ip, String username, String username_hash) throws IOException{
		ToClient t = cons.get(ip);
		boolean c = false;
		//check to see if username is already used
		try{
			Statement stmt = conn.createStatement();
			ResultSet s = stmt.executeQuery("SELECT USER FROM USERS WHERE UESR=\'" + username + "\';");
			String test = s.getString(1);
			if(test == null){
			 c = true;
			}//end if
		}//end try
		catch(SQLException e){
			System.out.println("error checking if username is taken");
			e.printStackTrace();
		}//end catch
		if(c){
			//adding user
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("INSERT INTO users VALUES(\"" + username + "\",\"" + ip + "\",TRUE, \'" + username_hash + "\');");
			}//end try
			catch(SQLException e){
				System.out.println("error createaccount");
				e.printStackTrace();
			}//end catch
			cons.remove(ip);
			cons.put(username,t);
			t.createAccountResponse(username,1);
		}//end if
		else{//rejected
			t.createAccountResponse(username,0);
		}//end else
	}//end create


    /**
     * log on trigger
     * @param user - user to log on
     * @throws IOException
     */
    public synchronized void logon(String user, String user_hash) throws IOException{
		if(check(user,user_hash)){
			ToClient t = cons.get(user.concat(user_hash));
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("UPDATE users " +
					"SET IP=" + t.getIP() +", ONLINE=TRUE"
					+ " WHERE USER=" + user + ";");

				ResultSet s = stmt.executeQuery("SELECT MESSAGE FROM MESSAGES WHERE UESR=\'" + user + "\';");
				while(s.next()){
					t.userFriendStatus(s.getString(1));
				}//end while
			}//end try
			catch(SQLException e){
				System.out.println("error logon");
				e.printStackTrace();
			}//end catch
		}//end if
		else{
			ToClient t = cons.get(user.concat(user_hash));
			t.error("Your not authehtic!");
		}
	}//endlogon

    /**
     * log off trigger
     * @param user - user to log off
     * @throws IOException
     */
    public synchronized void logoff(String user, String user_hash) throws IOException{
		if(check(user,user_hash)){
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("UPDATE users " +
					"SET IP=\"0.0.0.0\", ONLINE=FALSE"
					+ " WHERE USER=" + user + ";");
			}//end try
			catch(SQLException e){
				System.out.println("error logoff");
				e.printStackTrace();
			}//end catch
			cons.remove(user.concat(user_hash));
		}//end if
		else{
			ToClient t = cons.get(user.concat(user_hash));
			t.error("Your not authehtic!");
		}
	}//end logoff

    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @parm from_hash - the hash from the from
     * @param to - responder
     * @param port - port.
     * @throws IOException
     */
    public synchronized void initConversation(String from, String from_hash, String to, String port) throws IOException{
		if(check(from,from_hash)){
			ToClient t = null;
			try{
				Statement stmt = conn.createStatement();
				ResultSet s = stmt.executeQuery("SELECT HASH FROM USERS WHERE UESR=\'" + to + "\';");
				String test = s.getString(1);
				t = cons.get(to.concat(test));
			}//end try
			catch(SQLException e){
				System.out.println("error checking if username is taken");
				e.printStackTrace();
			}//end catch
			if(t == null){
				ToClient n = cons.get(from);
				n.error(to + " is not online");
			}//end if
			else{
				t.initConversation(from,to,port);
			}//end
		}//end if
		else{
			ToClient t = cons.get(from.concat(from_hash));
			t.error("Your not authehtic!");
		}
	}//end init

    /**
     * get the ip of a user
     * @param from - who the request is from
     * @param to - what user to get the IP from
     * @throws IOException
     */
    public synchronized void getIP(String from, String from_hash, String to) throws IOException{
		if(check(from,from_hash)){
			ToClient t = cons.get(to);
			ToClient n = cons.get(from.concat(from_hash));
			if(t != null){
				try{
					Statement stmt = conn.createStatement();
					ResultSet s = stmt.executeQuery("SELECT IP FROM USERS WHERE USER=\'" + to + "\';");
					n.IP(to,s.getString(1));
				}//end try
				catch(SQLException e){
					System.out.println("error getIP");
					e.printStackTrace();
				}//end catch
			}//end if
			else{
				n.error(to + " is not online");
			}
		}//end if
		else{
			ToClient t = cons.get(from.concat(from_hash));
			t.error("Your not authehtic!");
		}
	}//end getIP


	/*this method checks to see if the person who sent this message is authentic
	@parm: String - username
	@parm: String - hash
	@post: boolean - are they authentic?

	*/
	private synchronized boolean check(String name, String hash){
		boolean ans = false;
		try{
			Statement stmt = conn.createStatement();
			ResultSet s = stmt.executeQuery("SELECT HASH FROM USERS WHERE USER=\'" + name + "\';");
			ans = hash.equals(s.getString(1));
		}//end try
		catch(SQLException e){
			System.out.println("error check");
			e.printStackTrace();
		}//end catch
		return ans;
	}//end

	/**
     * reject message from the user
     * @param from -- who rejected
     * @param to -- to
     */
    public void rejectConversation(String from, String from_hash, String to) throws IOException{
		try{
			if(check(from,from_hash)){
				Statement stmt = conn.createStatement();
				ResultSet s = stmt.executeQuery("SELECT HASH FROM USERS WHERE UESR=\'" + to + "\';");
				String test = s.getString(1);
				ToClient t = cons.get(to.concat(test));
				t.rejectedConverstaion(from);

			}//end if
			else{
				ToClient t = cons.get(from.concat(from_hash));
				t.error("Your not authehtic!");
			}
		}//end try
		catch(SQLException e){
			System.out.println("error reject");
			e.printStackTrace();
		}//end catch

	}//end


}//end class