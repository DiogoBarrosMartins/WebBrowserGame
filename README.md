# WebBrowserGame Backend

Welcome to the Single Greatest Web Browser Game in the history of the interwebs! Try to stop me nerds!

## Postman Endpoints

### Game Map Generation

- Generate the game map (5 by 5 grid):
   - Endpoint: `POST http://localhost:8080/grid/generate`

### Account Management

- Create an account with the following JSON object as the request body:
   - Endpoint: `POST http://localhost:8080/accounts`
   - Request Body:
     ```json
     {
         "username": "example_user",
         "email": "example@example.com",
         "password": "yourpassword",
         "tribe": "orc"
     }
     ```

- Retrieve all accounts:
   - Endpoint: `GET http://localhost:8080/accounts/`

- Get an account by username:
   - Endpoint: `GET http://localhost:8080/accounts/example_user`

- Get an account by user ID:
   - Endpoint: `GET http://localhost:8080/accounts/id/1`

### Village Management

- Retrieve all villages:
   - Endpoint: `GET http://localhost:8080/villages`

- Get a list of villages by account username:
   - Endpoint: `GET http://localhost:8080/villages/byAccountUsername/example_user`

### Building Management

- Retrieve all resource-producing buildings by village ID:
   - Endpoint: `GET http://localhost:8080/buildings/{villageID}/resource`

- Retrieve all non-resource producing buildings by village ID:
   - Endpoint: `GET http://localhost:8080/buildings/{villageID}/non-resource`

- Upgrade buildings (stats change, but the timer is yet to be refined):
   - Endpoint: `POST http://localhost:8080/{villageID}/buildings/{buildingID}/upgrade`

### Messages, Troops, and Attacks

Messages, Troops, and Attacks are implemented and being tested.

## Status and Collaboration

This application is almost ready to go to the front-end phase! Feel free to contact me if you want to collaborate. 😄

Looking forward to building the ultimate WebBrowserGame together! 🚀

## License 

This project is released under the MIT License.

## Acknowledgments

Thanks! 
