/*
 * ServerListener.java
 *
 * Version:
 * 	$Id$
 *
 * Revisions:
 * 	$Log$
 */

//imports go here

import java.io.IOException;

/*
 * Summary:
 * Interface ClientListener specifies the interface for an object that is
 * triggered by events to the client chat program.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public interface ClientListener {

    /**
     * friend request between two users
     * @param from - requester
     * @param from_hash - hash to verify from.
     * @param to -  requestee
     * @param status - status of request
     *              0 - pending
     *              1 - accept
     *              2 - reject
     * @throws IOException
     */
    public void friendRequest( String from, String from_hash, String to, int status) throws IOException;


    /*closes the socket

    */
    public void end() throws IOException;

    /*gets ip of this socket

    */
    public String IP();


	/*this method adds a connection
		@parm: String username
		@parm: String hash
		@parm: Toclient the conection to user

	*/
	public void add(String name, String hash, ServerListener c);



	/*this method adds a connection
		@parm: String ip
		@parm: Toclient the conection to user

	*/
	public void add(String ip, ServerListener c);




    /**
     * creates an account for a user
     * @param ip
     * @param username
     * @param username_hash - hash to verify user.
     * @throws IOException
     */
    public void createAccount(String ip, String username, String username_hash) throws IOException;

    /**
     * log on trigger
     * @param user - user to log on
     * @param user_hash - hash to verify user.
     * @throws IOException
     */
    public void logon(String user, String user_hash) throws IOException;

    /**
     * log off trigger
     * @param user - user to log off
     * @param user_hash - hash to verify user.
     * @throws IOException
     */
    public void logoff(String user, String user_hash) throws IOException;

    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @param from_hash - hash to verify user.
     * @param to - responder
     * @param port - port.
     * @throws IOException
     */
    public void initConversation(String from, String from_hash, String to, String port) throws IOException;

    /**
     * get the ip of a user
     * @param from - who the request is from
     * @param from_hash - hash to verify user.
     * @param to - what user to get the IP from
     * @throws IOException
     */
    public void getIP(String from, String from_hash, String to) throws IOException;

    /**
     * reject message from the user
     * @param from -- who rejected
     * @param to -- to
     */
    public void rejectConversation(String from, String from_hash, String to) throws IOException;

    /**
     * Request to remove the friend
     * @param from user who wants to remove
     * @param from_hash - verify user
     * @param friend - friend to remove.
     * @throws IOException
     */
    void requestRemoveFriend(String from, String from_hash, String friend) throws IOException;

}
