# Gaming Console Game Sales

A web application tailored for the gaming community that simplifies the process 
of buying and selling used console video game discs. The platform allows users to 
list their physical copies of games from various gaming consoles such as 
PlayStation, Xbox, Nintendo Switch, fostering a vibrant marketplace. 
Buyers can browse through a variety of titles and communicate directly with
sellers through an integrated chat feature. In addition, buyers have the
option to directly order the desired game disk.

The primary goal of the application is to facilitate seamless transactions within the gaming community while prioritizing user convenience and security. By providing a central hub for gamers to trade their beloved titles, it simplifies the buying and selling process and eliminates the hassle often associated with traditional methods.

## Target Audience

The application serves a diverse range of individuals within the gaming community, including:

- **Gaming Enthusiasts**: Dedicated gamers who are actively involved in the gaming community and are passionate about expanding their game collection and exploring new titles.
  
- **Casual Gamers**: Individuals who enjoy occasional gaming sessions and are looking for affordable ways to add to their gaming library without making a major investment.
  
- **Bargain Hunters**: Those constantly on the lookout for great deals, eager to discover discounted game discs and save money on their gaming purchases.
  
- **Gaming Collectors**: Enthusiasts looking to expand their collections or complete specific sets, including rare and hard-to-find editions available through the platform.
  
- **Parents and Gift Givers**: Individuals purchasing video game discs for their children or loved ones, benefiting from the platform's user-friendly interface and direct ordering options.


## Functionalities 

- **Login**: Users can securely log in to their accounts to access the platform's features and services.n
- **Registration**: New users can easily register for an account to join the community and start buying or selling game discs.
- **View sale announcements**: Users and non-registered users can browse through a variety of sales announcements that showcase various video game discs available for purchase.
- **Filter and search sale announcements**: Filter and search for sales ads based on specific criteria such as game title, console platform, price range, and more for quick and targeted searches.
- **Create a listing**: Sellers can create sale announcements to list their game discs for sale with detailed information about the title, condition, price, and other relevant details.
- **Review Users**: Users can leave reviews and ratings for other users based on their buying or selling experience, helping to build trust and credibility.
- **Filter listings based on user review ratings**: Filter sales listings based on sellers' review ratings, allowing them to make informed buying decisions.
- **Save announcements**: Users can save sales announcements for future reference or to review later when making purchasing decisions.
- **View the user's other listings**: Users can view other listings posted by a specific seller.
- **View user ratings**: Users can view reviews and ratings left by other users to assess the credibility and reputation of sellers before making a purchase.
- **Real-time chat with sellers (WebSocket)**: Buyers can communicate directly with sellers in real time using the built-in chat feature powered by WebSocket technology, enabling seamless communication and negotiation.
- **Order a sold game disk**: Buyers can place orders for sold game discs to be shipped to their address.



## Used Technologies

The platform utilizes the following technologies:

- **Spring REST API**: Using Spring, the platform provides a robust REST API for efficient communication between the backend and the frontend. The API uses JSON for data exchange, ensuring lightweight and readable communication.

- **MySQL Database**: The platform uses MySQL for efficient data storage and management, configured in the `application-jpa.yml` file under
`backend/src/main/resources/application-jpa.yml`. 

- **React**: The platform's frontend is built with React, creating a dynamic user interface.

### Similar Products

1. GameFlip
2. eBay's video game section
3. Amazon's marketplace for video games
4. OfferUp's gaming category in the US
5. OLX gaming section
