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

    /**
     * creates an account for a user
     * @param ip - the ip of this user
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
     * @throws IOException
     */
    public void initConversation(String from, String from_hash, String to) throws IOException;

    /**
     * get the ip of a user
     * @param from - who the request is from
     * @param from_hash - hash to verify user.
     * @param to - what user to get the IP from
     * @throws IOException
     */
    public void getIP(String from, String from_hash, String to) throws IOException;

}
