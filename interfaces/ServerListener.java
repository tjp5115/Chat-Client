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
 * Interface ServerListener specifies the interface for an object that is
 * triggered by events from the client chat program.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public interface ServerListener {
    /**
     * request for a friend from a given user.
     * @param from - requester user.
     * @param to - requested user.
     * @param status - status of the friend request.
     * @throws IOException
     */
    public void userFriendStatus(String from, String to, int status) throws IOException;

    /**
     * return the IP of a user
     * @param user - user to the IP
     * @param IP - IP address of the user.
     * @throws IOException
     */
    public void IP(String user, String IP) throws IOException;

    /**
     * return an error.
     * @param error - String noting the error that took place.
     * @throws IOException
     */
    public void error(String error) throws IOException;

    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to,String port) throws IOException;

    /**
     * response from server on either or not username was taken
     * @param user username given to the server.
     * @param status
     *   1 - you now have the username
     *   0 - that username is already taken
     * @throws IOException
     */
    public void createAccountResponse(String user, int status) throws IOException;


    /**
     * a client has reject your conversation
     * @param user - 'friend' who rejected.
     */
    public void rejectedConverstation(String user) throws IOException;
    /**
     * status of login
     */
    void loginSuccess()throws IOException;

    /**
     * Message that requests a user remove the friend
     * @param requester - the user who requested the remove
     * @param friend - the friend that was removed.
     */
    void removeFriend(String requester, String friend) throws IOException;

    String getIP();
}
