# Web Browser Game REST API

This is the REST API documentation for the Web Browser Game, an exciting online game project. The API allows you to manage user accounts, villages, buildings, troops, and attacks. Feel free to explore the available endpoints below to interact with the game.

## Postman Endpoints

### Account Management

- **Create an Account**:
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

- **Retrieve All Accounts**:
  - Endpoint: `GET http://localhost:8080/accounts/`

- **Get an Account by Username**:
  - Endpoint: `GET http://localhost:8080/accounts/example_user`

- **Get an Account by User ID**:
  - Endpoint: `GET http://localhost:8080/accounts/id/1`

### Village Management

- **Retrieve All Villages**:
  - Endpoint: `GET http://localhost:8080/villages`

- **Get a List of Villages by Account Username**:
  - Endpoint: `GET http://localhost:8080/villages/byAccountUsername/example_user`

### Building Management

- **Retrieve All Resource-Producing Buildings by Village ID**:
  - Endpoint: `GET http://localhost:8080/buildings/{villageID}/resource`

- **Retrieve All Non-Resource Producing Buildings by Village ID**:
  - Endpoint: `GET http://localhost:8080/buildings/{villageID}/non-resource`

- **Upgrade Buildings**:
  - Endpoint: `POST http://localhost:8080/{villageID}/buildings/{buildingID}/upgrade`

### Troop Management

- **Train Troops**:
  - Endpoint: `POST http://localhost:8080/villages/{villageID}/troops/train`
  - Request Body:
    ```json
    {
        "troopType": "example_troop",
        "quantity": 10
    }
    ```

- **Get Troop Training Queue**:
  - Endpoint: `GET http://localhost:8080/villages/{villageID}/troops/training-queue`

- **Get All Troop Types**:
  - Endpoint: `GET http://localhost:8080/villages/{villageID}/troops/list`

### Village Actions

- **Create a Village**:
  - Endpoint: `POST http://localhost:8080/villages`
  - Request Body: (Village DTO)

- **Get Village Details by Username**:
  - Endpoint: `GET http://localhost:8080/villages/{username}/`

- **Update Village Name**:
  - Endpoint: `PUT http://localhost:8080/villages/update-name/{username}`
  - Request Body: (New Village Name)

- **Get All Villages**:
  - Endpoint: `GET http://localhost:8080/villages`

- **Get Village by Account Username**:
  - Endpoint: `GET http://localhost:8080/villages/byAccountUsername/{username}`

- **Declare Attack**:
  - Endpoint: `POST http://localhost:8080/villages/{villageID}/declare-attack`
  - Request Body: (Attack DTO)

- **Get Village by ID**:
  - Endpoint: `GET http://localhost:8080/villages/{id}`

- **Get Surrounding Villages**:
  - Endpoint: `GET http://localhost:8080/villages/surrounding?x={x}&y={y}`

## Status and Collaboration

Looking forward to building the ultimate Web Browser Game together! 🚀

## License 

This project is released under the MIT License.

## Acknowledgments

Thanks for exploring our Web Browser Game API!
