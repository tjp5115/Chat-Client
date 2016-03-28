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
     * @param usr1 - requestee
     * @param usr2 - requested.
     * @param status - status of the friend request.
     * @throws IOException
     */
    public void userFriendStatus(String usr1, String usr2, int status) throws IOException;

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
     * returns the response to made by a user for a friend request
     * @param user - user name who responded
     * @param status - status given user selected.
     *  1 - accepted friend request
     *  0 - reject friend request
     * @throws IOException
     */
    public void friendRequestResponse(String user, int status) throws IOException;



    /**
     * initiate a conversation between two clients
     * @param from - initiator
     * @param to - responder
     * @throws IOException
     */
    public void initConversation(String from, String to) throws IOException;

    /**
     * response from server on either or not username was taken
     * @param user username given to the server.
     * @param status
     *   1 - you now have the username
     *   0 - that username is already taken
     * @throws IOException
     */
    public void createAccountResponse(String user, int status) throws IOException;

}
