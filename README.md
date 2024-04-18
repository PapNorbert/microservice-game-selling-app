# Gaming Console Game Sales

A web application for selling and buying used console video game discs. 
Users could re-sell their physical copies of a game they already played on gaming console, 
and buyers would get a platform to easily connect with sellers and possibility to chat with them.



## Functionalities 

- Login
- Registration
- View sale announcements
- Filter and search sale announcements
- Create sale announcement about selling your game
- Get in contact with the seller with the help of real time chat - WebSocket
- Review users
- Filter announcements based on user review rating
- Save announcements
- See other announcements of the user
- Order a sold game disc
- Verified reviews - review after order
- Check user reviews

## API server

The application uses Spring for it's REST API. 
The responses are in JSON format, the endpoints can be accessed using asynchronous requests.

## Application Database

The application uses a MySQL database, configured in the `application-jpa.yml` file under 
`backend/src/main/resources/application-jpa.yml`.

## Client side

The user interface of the application is a React application, which communicates
with the API server using asynchronous requests.