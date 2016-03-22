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
     * @param to -  requestee
     * @param status - status of request
     *              0 - pending
     *              1 - accept
     *              2 - reject
     * @throws IOException
     */
    public void friendRequest( String from, String to, int status) throws IOException;

    /**
     * creates an account for a user
     * @parm: String - the ip of this user
     * @param username
     * @throws IOException
     */
    public void createAccount(String ip, String username) throws IOException;

    /**
     * initiates a connection between two nodes
     * @throws IOException
     */
    public void initConnection() throws IOException;

    /**
     * log on trigger
     * @param user - user to log on
     * @throws IOException
     */
    public void logon(String user) throws IOException;

    /**
     * log off trigger
     * @param user - user to log off
     * @throws IOException
     */
    public void logoff(String user) throws IOException;

    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to) throws IOException;

    /**
     * get the ip of a user
     * @param from - who the request is from
     * @param to - what user to get the IP from
     * @throws IOException
     */
    public void getIP(String from, String to) throws IOException;

}
