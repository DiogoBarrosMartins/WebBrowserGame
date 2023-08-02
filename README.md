# WebBrowserGame
Backend
I AM CREATING THE SINGLE GREATEST WEB BROWSER GAME IN THE HISTORY OF THE INTERWEBS TRY TO STOP ME NERDS. 


![img.png](resources/static/img.png)




HOW TO SEE SOME ... ENDPOINTS 


POSTMAN : 


We create a game map. This will happen only once, and be implemented on our GameServerService. 5 by 5 grid. 
1) http://localhost:8080/grid/generate  `POST` 

You can now see the grid, the gamemap. It will allow us to see where we can colonize
   http://localhost:8080/grid `GET`


We can now create an account using this json object as a body on this endpoint :)
2) http://localhost:8080/accounts  `POST` 
{
"username": "example_user",
"email": "example@example.com",
"password": "yourpassword",
"tribe":"orc"
} 

We can also see all accounts
http://localhost:8080/accounts/ `GET`
Get an account by username
http://localhost:8080/accounts/example_user `GET`
Get an account by user ID
http://localhost:8080/accounts/id/1 `GET`


The cool part is that, as it should be, an account creation created a village for that player!
We can see all Villages
The best part, we can also see the resources of that village! Spying is 100% legal for now. 
3) http://localhost:8080/villages 

We can get a list of villages by account username (neat)
http://localhost:8080/villages/byAccountUsername/example_user


4) We can also see buildings !!

All resource producing buildings by village ID !
http://localhost:8080/buildings/{villageID}/resource

As well as non resource producing buildings. There are a bit lacking for now. 
http://localhost:8080/buildings/{villageID}/non-resource

We can upgrade buildings, the stats change but the timer is yet to be refined. 
5) http://localhost:8080/{villageID}/buildings/{buildingID}/upgrade

Messages, Troops and Attacks are implemented and being tested.

This application is almost ready to go to the front end phase !

Feel free to contact me if you want to collaborate :D
