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

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/

public class ToClient implements ServerListener{
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
     * @param user - user to get the IP of
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
}//end class