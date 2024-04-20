# Gaming Console Game Sales

A web application tailored for the gaming community that simplifies the process 
of buying and selling used console video game discs. The platform allows users to 
list their physical copies of games from various gaming consoles such as 
PlayStation, Xbox, Nintendo Switch, fostering a vibrant marketplace. 
Buyers can browse through a variety of titles and communicate directly with
sellers through an integrated chat feature. In addition, buyers have the
option to directly order the desired game disk. Their primary goal is to 
facilitate seamless transactions within the gaming community while 
prioritizing user convenience and security.

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