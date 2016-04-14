                                    chat

What is it?
-----------


The Latest Version
------------------

The latest version can be found at the following github page:

https://github.com/blstrrifl/chat

NOTE: This page is private and access is restricted

Documentation
-------------

Current documentation for the project can be found in the docs directory of
the github directory. Or by following the link below:

https://github.com/blstrrifl/chat

Running the Program
-------------------

NOTE: This program was made is java 8 and any other java versions are not garenteed 
to work!

NOTE: This program has only been tested in Ubuntu 14.04 any other OS is undefined.
But ubuntu 15.04 and above really doesn't work! Thougth the server should work in any
Unix enviroment.

To run the server, the user must be in Ubuntu 14.04,  navigate to 
the server directory and use the following command:

./run.sh <ip of the server>

The server is using a in memory database so if the server goes down all users must 
regrister. This can be changed to a file database easily, but has not been implimented.

This command will compile the server and run it on the IP specified.

To run the client, again the user must be in Ubuntu 14.04,  navigate to 
the client directory and use the following command:

./run.sh

This command will compile the client and run it. Note: runnng the client this 
way will make the terminal unusable will the client is running.

NOTE: there are still a few debug messages laying around. Those will be removed 
for the next version.

Restristions
------------

When we implemented this we had planned on making it be able to tranverse NAT
but after problems, this feature could not be implemented.

Also the database on the server side is a in memory database, so if the server 
ever goes down all pending messages are lost. But that should be the only thing 
lost.

Additionally, none of the stretch goals where met for this release.

Bad feelings
------------

I would investigate how we hard handling the ssl keystore. This was a large problem
for our group, and could be our weak point.


Contacts
--------

If there is need for any support in running or using the program, please
contact Samuel Launt at stl7199@rit.edu.

If you want to submit a bug report, please contact Samuel Launt at 
stl7199@rit.edu. Please include a brief description of the bug and the
necessary steps to reproduce it.

