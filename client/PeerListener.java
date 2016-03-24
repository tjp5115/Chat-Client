/*
* PeerListener.java
*
* Version:
*  $Id$
*
* Revisions:
*  $Log$
*/

//imports go here

/*
 * Interface PeerListener specifies the interface for an object that is
 * triggered by events between two clients in the chat program.

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu

*/
public interface PeerListener {
    /**
     * Send a message to a user.
     * @param from - who the message is from.
     * @param to - who the message is too.
     * @param msg - the message.
     */
    void message(String from, String to, String msg);

    /**
     * Requests a chat session to begin between two users.
     * @param user - user requesting a chat session.
     */
    void start(String user);

    /**
     * Stops a chat session between two users.
     * @param user - user to stop the session from.
     */
    void stop(String user);
}
