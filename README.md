# Gaming Console Game Sales

A web application for selling and buying used console video game discs. 
Users could re-sell their physical copies of a game they already played on gaming console, 
and buyers would get a platform to easily connect with sellers and possibility to chat with them.

The application does not cover the payment and transportation of the products, it is only a free announcement posting page, 
that helps to get in touch with others.


## Functionalities 

## API server

The application uses Spring for it's REST API. 
The responses are in JSON format, the endpoints can be accessed using asynchronous requests.

## Application Database

The application uses a MySQL database, configured in the `application-jpa.yml` file under 
`backend/src/main/resources/application-jpa.yml`.

## Client side

The user interface of the application is a React application, which communicates
with the API server using asynchronous requests.