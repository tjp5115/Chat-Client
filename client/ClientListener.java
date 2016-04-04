
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

import java.io.IOException;

/* class Description

@authors: Samuel Launt, Tyler Paulsen, LAI CHUNG Lau
@emails: stl7199@rit.edu, tjp5115@rit.edu, lxl3375@rit.edu 

*/

interface ClientListener
{
	void friendRequest(String from, String from_hash, String to, int status) throws IOException;
	void createAccount(String ip, String username, String username_hash) throws IOException ;
	void initConnection() throws IOException;
	void logon(String user, String user_hash) throws IOException;
	void logoff(String user, String user_hash) throws IOException;

}