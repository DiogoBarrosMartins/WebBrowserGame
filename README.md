# WebBrowserGame
Backend
I AM CREATING THE SINGLE GREATEST WEB BROWSER GAME IN THE HISTORY OF THE INTERWEBS TRY TO STOP ME NERDS. 



ACCOUNT 

post account 
get accounts
get account by id 
get account by username



VILLAGE 

NAN

GRID 

get grid 
grid is not initialized





GRID 
The Grid entity should represent the grid where the game happens. 
Considering the context of a browser game, in which players have villages, the game grid is where said villages are placed. 
It's also one of the most important endpoints on the api. 
By asking our api for the grid object, we should be able to represent the whole game map, and see where the villages are, and where they aren't. 
That will allow us to draw the game map in the future. It will also allow us to see where the villages are located, and where they aren't! 
A player needs a village to play the game, and when they create a new account, or later in the game, when they decide to colonize a new village, we will need to check on the grid where we have an open space available. 
