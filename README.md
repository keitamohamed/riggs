
## Riggs 
Riggs is a Spring boot and React application that allow you to make Hotel Reservation. 
With Riggs, you can register and make a reservation, it also allows you to edit your information 
and delete your booking.

## Database
 * The database structure contains seven tables, a `User, Address, Authenticate, Room, RoomDetail, Booking` and  `BookingPrice` table.
 * `User` table contains user info such name, phone number `Address, Authenticate` and `booking` tables.
 * `Address` table that contains user address
 * `Authenticate` table that contains user login and user role
 * `Room` table that contains room name, size, price etc
 * `RoomDetail` table that contains room details
 * `Booking` table, contains booking details such as date room etc
 * `BookingPrice` table, contains booking price

## Tech Stack
* Backend API Creation
  * Java
  * Spring Boot [Link](https://spring.io/projects/spring-boot) 
  * MYSQL [Link](https://www.mysql.com)
  * Lombok [Link](https://projectlombok.org)
  * Spring Data JPA [Link](https://spring.io/projects/spring-data-jpa#support)
  * Hibernate [Link](https://hibernate.org)
* Frontend UI Creation
  * Reactjs [Link](https://react.dev)
  * Tailwind CSS [Link](https://tailwindcss.com)
  * SCSS
  * Redux [Link](https://redux.js.org/introduction/getting-started)
  * Axios
  * Swiperjs [Link](https://swiperjs.com)
## Explore USER Rest APIS
* User API

| Method |             Endpoint              |           Description           | API Call                                      |
|:-------|:---------------------------------:|:-------------------------------:|-----------------------------------------------|
| POST   |          /riggs/user/add          |        Register new user        | [Register New User](#register-new-user)       |
| PUT    |      /riggs/user/update/{id}      |    Updated an existing user     | [Update Existing User](#update-existing-user) |
| PUT    |   /riggs/user/update-auth/{id}    | Updated an existing user login  | [Update User Login](#update-user-login)       |
| GET    |    /riggs/user/find-by-id/{id}    |   Find an existing user by id   | [Find user by id](#find-user-by-id)           |
| GET    | /riggs/user/find-by-email/{email} | Find an existing user by email  | [Find user by email](#find-user-by-email)     |
| GET    |         /riggs/user/list          | Get list of all existing users  | [User list](#get-user-list)                   |
| GET    |    /riggs/user/user-excel-file    |    Download users excel file    | [Download User excel file](#user-excel-file)  |
| DELETE |      /riggs/user/delete/{id}      | Delete a specified user account | [Delete user](#delete-user-by-id)             |

#### Register New User 
  * Validation (Throws an invalidRequestException) when:
    + firstNme is `null`
    + lastName is `null`
    + phoneNum is `null`
    + address object is `null` or `missing validation requirement`
    + auth (authenticate object) is `is null` or `missing validation requirement`
  * Request Body
    ```json
        {
          "firstName": "",
          "lastName": "",
          "phoneNum": "",
          "address": {
              "street": "",
              "city": "",
              "state": "",
              "zipcode":  ""
          },
          "auth": {
              "email": "",
              "password": "",
              "role": ""
          }
        }
    ```
#### Update Existing User
* Validation (Throws an invalidRequestException) when:
    + userID is `null`
    + firstNme is `null`
    + lastName is `null`
    + phoneNum is `null`
    + address object is `null` or `missing validation requirement`
* Request Body
  ```json
      {
        "userID": 0,
        "firstName": "",
        "lastName": "",
        "phoneNum": "",
        "address": {}
      }
  ```
#### Update User Login
* Validation (Throws an invalidRequestException) when:
    + authID is `null`
    + password is `null`
* Request Body
    ```json
          {
            "authID": 0,
            "email": ""
          }
    ```
    ***Email address cannot be updated and is Unique***

## Explore Room Rest APIS
*   Room API

| Method |          Endpoint           |          Description          | API Call                             |
|:-------|:---------------------------:|:-----------------------------:|--------------------------------------|
| POST   |       /riggs/room/add       |        Create new room        | [Create Room](#create-new-room)      |
| PUT    |   /riggs/room/update/{id}   |   Updated an existing room    | [Update room](#update-existing-room) |
| GET    | /riggs/room/find-by-id/{id} |     Find an existing room     |                                      |
| GET    |      /riggs/room/list       | Get list of all existing room |                                      |
| DELETE |   /riggs/room/delete/{id}   |    Delete an existing room    |                                      |

#### Create New Room
*   Validation (Throws) when:
    + roomName is `null`
    + description is `null`
    + size is `null`
    + price is `null`
    + detail (RoomDetail) object is `null` or `missing validation requirement` 
  * Request Body
      ````json
          {
            "roomName": "",
            "description": "",
            "size": "",
            "price": 0,
            "detail": {
                "bed": "",
                "numberOfBed": "",
                "view": "",
                "smoking": "",
                "bathroom": "",
                "tv": "",
               "animal": ""
            }
          }
    ````
#### Update Existing Room
*   Validation (Throws) when:
    + roomID is `null`
    + roomName is `null`
    + description is `null`
    + size is `null`
    + price is `null`
    + detail (RoomDetail) object is `null` or `invalidate requirements`
* Request Body
    ````json
        {
          "roomName": "",
          "description": "",
          "size": "",
          "price": 0,
          "detail": {}
        }
  ````

## Explore Booking Rest APIS
*   Booking API

| Method |              Endpoint              |             Description              | API Call                           |
|:-------|:----------------------------------:|:------------------------------------:|------------------------------------|
| POST   |    /riggs/booking/add/{userid}     | Create new booking for existing user | [New Booking](#create-new-booking) |
| GET    |   /riggs/booking/find-by-d/{id}    |       Find an existing booking       |                                    |
| GET    |    /riggs/room/list-of-booking     |   Get list of all existing booking   |                                    |
| DELETE | /riggs/booking/delete-booking/{id} |      Delete an existing booking      |                                    |

#### Create New Booking
*   Validation (Throws) when:
    + arrDate is `null`
    + depDate is `null`
    + numRoom is `0`
    + numAdult is `0`
    + user object is `null` or `missing userID`
    + rooms list is `empty`
    + prices (booking prices) list is `empty`
* Request Body
    ````json
        {
          "arrDate": "YYYY-MM-DD",
          "depDate": "YYYY-MM-DD",
          "numRoom": 0,
          "numAdult": 0,
          "numChildren": 0,
          "user": {
            "userID": 0
          },
          "rooms": [
            {
             "roomID": 0
            }
          ],
          "prices": [
            {
              "price":  0
            }
          ]
      }
  ````