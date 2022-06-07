# helpdesk
This is my final project for my Computer Science major.

Install: 

Make sure to install both java fx and derby libraries 

I use derby server. In order to start the server, you need to set the derby home and java home local variables in the terminal before you call startNetworkServer in the bin file of the derby filepath

Project Specifications: 

To start you need to create a fake account through the code then the rest can be through the GUI

Every time a user creates an organization, three queues get created: Assigned, Closed, Name of Organization. All of these queues are tied to that organization

Every time a user creates another user, the project assigns the user to the Name of Organization queue

Users can be assigned to as many queues as they want and new queues can be created.

Then an organization can create tickets with another organization and place them in specific queues and edit who is assigned to that ticket