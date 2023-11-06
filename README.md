
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
## Explore Rest APIS
* User API

| Method |             Endpoint              |                     Description | API Call |
|:-------|:---------------------------------:|--------------------------------:|----------|
| POST   |          /riggs/user/add          |               Register new user |          |
| PUT    |      /riggs/user/update/{id}      |        Updated an existing user |          |
| PUT    |   /riggs/user/update-auth/{id}    |  Updated an existing user login |          |
| GET    |    /riggs/user/find-by-id/{id}    |     Find an existing user by id |          |
| GET    | /riggs/user/find-by-email/{email} |  Find an existing user by email |          |
| GET    |         /riggs/user/list          |  Get list of all existing users |          |
| GET    |    /riggs/user/user-excel-file    |       Download users excel file |          |
| DELETE |      /riggs/user/delete/{id}      | Delete a specified user account |          |


*   Room API

| Method |          Endpoint           |                   Description | API Call |
|:-------|:---------------------------:|------------------------------:|----------|
| POST   |       /riggs/room/add       |               Create new room |          |
| PUT    |   /riggs/room/update/{id}   |      Updated an existing room |          |
| GET    | /riggs/room/find-by-id/{id} |         Find an existing room |          |
| GET    |      /riggs/room/list       | Get list of all existing room |          |
| DELETE |   /riggs/room/delete/{id}   |       Delete an existing room |          |

*   Booking API

| Method |              Endpoint              |                          Description | API Call |
|:-------|:----------------------------------:|-------------------------------------:|----------|
| POST   |    /riggs/booking/add/{userid}     | Create new booking for existing user |          |
| GET    |   /riggs/booking/find-by-d/{id}    |             Find an existing booking |          |
| GET    |    /riggs/room/list-of-booking     |     Get list of all existing booking |          |
| DELETE | /riggs/booking/delete-booking/{id} |           Delete an existing booking |          |

