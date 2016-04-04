/*
* DataBaseHandler.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

import java.util.ArrayList;
import java.sql.*;

/* This class handles all requests from the clients, it also
makes the nessitary SQL calls to the database.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class DatabaseHandler{

    Connection conn;
    private boolean isConnected = false;

    /*Default constructor
    @parm: String - path to database file
    @parm: String - username
    @parm: String - user password
    */
    public DatabaseHandler(String path, String user, String password){
        try {
            //This tells it to use the h2 driver
            Class.forName("org.h2.Driver");

            //creates the connection
            conn = DriverManager.getConnection("jdbc:h2:" + path, user, password);
            isConnected = true;
        }//end try
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("error creating the conn for database");
            e.printStackTrace();
        }//end catch
    }//end constructor


    /*this method creates the tables in the database. THis is for when the user creates a
    new username for the service
    @parm: String - username
    @parm: String - server ip in form X.X.X.X

    */
    public void init(String us, String ip){
        try{
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE username(USER VARCHAR(255) PRIMARY KEY, serverIP VARCHAR(25));");
            stmt.execute("CREATE TABLE friends(name VARCHAR(255), status int);");
            stmt.execute("INSERT INTO username VALUES( \'" + us + "\' , \'" + ip + "\');");
        }//end try
        catch(SQLException e){
            System.out.println("error creating tables");
            e.printStackTrace();
        }//end catch
    }//end init


    /*this method return a boolean if this user is a given users friend
    @parm: String - username
    @post: boolean - is friend?
    */
    public boolean isFriend(String username){
		boolean t = false;
		try{
			Statement stmt = conn.createStatement();
			ResultSet s = stmt.executeQuery("SELECT user FROM username WHERE USER=" + username + ";");
			if (s.getString(1) == null ){
				t= false;
			}
			else{
				t= true;
			}
        }//end try
        catch(SQLException e){
            System.out.println("error isFriend");
            e.printStackTrace();
        }//end catch
		return t;
	}//end

    /*this method adds a friend to pending
    @parm: String username
    */
    public void addFriend(String name){
        try{
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO username VALUES(\'" + name + "\', 0);");
        }//end try
        catch(SQLException e){
            System.out.println("error adding friend");
            e.printStackTrace();
        }//end catch
	}//end

    /*this method updates a frind to actual friend or removes from database.
    This could also be used to remove a frind from the database.
    @parm: String - name
    @parm: boolean - did they accept?
    */
    public void updateFriend(String name, boolean t){
		if(t){
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("UPDATE username SET status = 1 WHERE user=\'" + name+ "\';");
			}//end try
			catch(SQLException e){
				System.out.println("error updating friend");
				e.printStackTrace();
			}//end catch
		}
		else{
			try{
				Statement stmt = conn.createStatement();
				stmt.execute("DELETE * FROM username WHERE user = \'" + name + "\';");
			}//end try
			catch(SQLException e){
				System.out.println("error updating friend");
				e.printStackTrace();
			}//end catch
		}
	}//end

    /*this method retrieves a list of all friend names
    @post: ArrayList<String> - list of friends

    */
	public ArrayList<String> getFriends(){
		ArrayList<String> f = new ArrayList<String>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet s = stmt.executeQuery("SELECT user FROM username");
			while(s.next()){
				f.add(s.getString(1));
			}//end
		}//end try
        catch(SQLException e){
            System.out.println("error getFriend");
            e.printStackTrace();
        }//end catch
        return f;
	}//end

	/*this method returns the server IP
	@post: String server IP in for X.X.X.X

	*/
	public String getServerIP(){
		return new String();
	}//end


	/*this method returns the username of the person whose database
	this is
	@post: String - username

	*/
	public String getName(){
		return new String();
	}//end

    /**
     * get the hash of the user
     * @return hash
     */
    //todo
    public String getHash(){return "";}

    /**
     * update friend up
     * @param username - username of friend
     * @param ip - ip of friend;
     */
    //todo
    public void updateFriendIP(String username, String ip){

    }

    /**
     * Gets the IP of a user
     * @param username
     * @return
     */
    //todo
    public String getFriendIP(String username){
        return "";
    }

    /**
     * Need to know if the database was successfully found and read in. if not return false.
     * @return
     */
    public boolean isConnected(){
        return isConnected;
    }

}//end class