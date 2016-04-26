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

Before we can chat we need to run the server, the user must be in Ubuntu 14.04,  navigate to 
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

To chat in the program we need to create a user for the service. When the client program
starts hit the register button at the bottom of the GUI. Fill in the desired username,
the password you want for your database, the server IP, and lastly you need to hit the 
browse buttom by the last field to select the directory where you would like the database 
created in. 

If everything was filled in correctly you should now be logged in! Now to add a friend you
hit the add friend button at the bottom and enter their name.

Now that we have friends (assuming that they accepted) click there name to require a conversation
to be started. (assuming they said yes) Click in the text box and text your message and click send.

Restristions
------------

When we implemented this we had planned on making it be able to tranverse NAT
but after problems, this feature could not be implemented.

Also the database on the server side is a in memory database, so if the server 
ever goes down all pending messages are lost. But that should be the only thing 
lost. 

NOTE: The client side database is NOT IN MEMORY. There is a actual file and that is basicly your ID.
Don't lose it...

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

