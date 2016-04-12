/*
* DataBaseHandler.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

import org.h2.command.Prepared;
import org.h2.tools.ChangeFileEncryption;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* This class handles all requests from the clients, it also
makes the nessitary SQL calls to the database.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class DatabaseHandlerClient {

    Connection conn;
    private boolean isConnected = false;
    String hash;
    HashMap<String,String> ip;
    HashMap<String,String> port;
    /*Default constructor
    @parm: String - path to database file
    @parm: String - username
    @parm: String - user password
    */
    public DatabaseHandlerClient(String path, String user, String password) throws SQLException{
        try {
            //This tells it to use the h2 driver
            Class.forName("org.h2.Driver");
            //creates the connection
            if ( !new File(path).exists() ) throw new SQLException();
            if(path.endsWith("/")) {
                conn = DriverManager.getConnection("jdbc:h2:" + path + user + ";CIPHER=AES", user, "filepwd " + password);
            }else{
                path = path.substring(0,path.length()-6);
                conn = DriverManager.getConnection("jdbc:h2:file:" + path + ";CIPHER=AES;IFEXISTS=TRUE;", user,  "filepwd " + password);
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            org.h2.tools.Server.startWebServer(conn);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                */
            }
            isConnected = true;
        	hash = getPasswordHash(password);
        	ip = new HashMap<String,String>();
            port = new HashMap<String,String>();
        }//end try
        catch (ClassNotFoundException e) {
            System.out.println("error creating the conn for database");
            e.printStackTrace();
        }//end catch

        password = "";
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
			//Statement stmt = conn.createStatement();
            //ResultSet s = stmt.executeQuery("SELECT name FROM friends WHERE NAME='" + username + "';");
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM friends WHERE NAME=?;");
            stmt.setString(1,username);
            ResultSet s = stmt.executeQuery();
			if (s.next()){
				t = true;
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
            //Statement stmt = conn.createStatement();
            //stmt.execute("INSERT INTO friends VALUES('" + name + "', 0);");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO friends VALUES(?,0);");
            stmt.setString(1, name);
            stmt.execute();
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
				//Statement stmt = conn.createStatement();
                //stmt.execute("UPDATE friends SET status = 1 WHERE name='" + name+ "';");
                PreparedStatement stmt = conn.prepareStatement("UPDATE friends SET status = 1 WHERE name=?;");
                stmt.setString(1, name);
                stmt.execute();
			}//end try
			catch(SQLException e){
				System.out.println("error updating friend");
				e.printStackTrace();
			}//end catch
		}
		else{
			try{
				//Statement stmt = conn.createStatement();
                //stmt.execute("DELETE * FROM friends WHERE name = '" + name + "';");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM friends WHERE name=?;");
                stmt.setString(1, name);
                stmt.execute();
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
            ResultSet s = stmt.executeQuery("SELECT name FROM friends");
            String me = getName();
			while(s.next()){
                if(s.getString(1) != me)
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
		String ans = "";
		try{
			Statement stmt = conn.createStatement();
			ResultSet s = stmt.executeQuery("SELECT serverIP FROM username");
			while(s.next()){
				ans = s.getString(1);
			}//end
		}//end try
        catch(SQLException e){
            System.out.println("error getServerIP");
            e.printStackTrace();
        }//end catch
        return ans;
	}//end


	/*this method returns the username of the person whose database
	this is
	@post: String - username

	*/
	public String getName(){
		String ans = "";
		try{
			Statement stmt = conn.createStatement();
			ResultSet s = stmt.executeQuery("SELECT user FROM username");
			while(s.next()){
				ans = s.getString(1);
			}//end
		}//end try
        catch(SQLException e){
            System.out.println("error getName");
            e.printStackTrace();
        }//end catch
        return ans;
	}//end

    /**
     * get the hash of the user
     * @return hash
     */
    public String getHash(){
		return hash;
	}//end get

    /**
     * Gets the IP of a user
     * @param username
     * @return
     */
    public String getFriendIP(String username){
        return ip.get(username);
    }

    public void addFriendPort(String username, String port){
        this.port.put(username,port);
    }

    /**
     * get ip of friend
      * @param username
     * @return
     */
    public String getFriendPort(String username){
        return port.get(username);
    }

    /**
     * update friend up
     * @param username - username of friend
     * @param ip - ip of friend;
     */
    public void addFriendIP(String username, String ip){
		this.ip.put(username,ip);
    }

    /**
     * Gets the IP of a user
     * @param username
     *
     */
    public void removeFriendIP(String username){
        ip.remove(username);
    }


    /**
     * Need to know if the database was successfully found and read in. if not return false.
     * @return
     */
    public boolean isConnected(){
        return isConnected;
    }
    public static String getPasswordHash(String pw){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error creating the conn for database: HASH error");
            e.printStackTrace();
        }
        StringBuffer hexString = new StringBuffer();
        byte []hash = md.digest(pw.getBytes());
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0"
                        + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

}//end class