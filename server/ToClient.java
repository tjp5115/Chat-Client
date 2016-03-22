/*
 * ToClient.java
 *
 * Version:
 *  $Id$
 *
 * Revisions:
 *  $Log$
 */

import java.io.IOException;
import java.net.*;
import javax.net.ssl.*;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class ToClient implements ServerListener{

	private SSLSocket sok;

	/*this method returns the ssl socket of the toclient
	@post: sslsocket- the connection
	*/
	public SSLSocket getSok(){
		return sok;
	}//end

    /*this method returns the IP of the computer connected on the other side(client)
    @post: String rep of IP ex: "192.168.254.254"

    */
    public String getIP(){
		return "NEEDS TO BE DONE";
	}//end

	/*overrides equals
	@override

	*/
	public boolean equals(final Object obj) {
		ToClient t = (ToClient)obj;
		return sok.equals(t.getSok());

	}//end


    /**
     * request for a friend from a given user.
     * @param usr1 - requestee
     * @param usr2 - requested.
     * @param status - status of the friend request.
     * @throws IOException
     */
    public void userFriendStatus(String usr1, String usr2, int status) throws IOException{

	}//end user

    /**
     * return the IP of a user
     * @param user - user name of the IP
     * @param IP - IP address of the user.
     * @throws IOException
     */
    public void IP(String user, String IP) throws IOException{

	}//end ip

    /**
     * return an error.
     * @param error - String noting the error that took place.
     * @throws IOException
     */
    public void error(String error) throws IOException{

	}//end error

    /**
     * returns the response to made by a user for a friend request
     * @param user - user name who responded
     * @param status - status given user selected.
     *  1 - accepted friend request
     *  0 - reject friend request
     * @throws IOException
     */
    public void friendRequestResponse(String user, int status) throws IOException{

	}//end



    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to) throws IOException{

	}


    /*responce from server on either or not username was taken
    1 - you now have the username
    0 - that username is already taken

    */
    public void createAccountResponce(String user, int i){

	}//end
}//end class