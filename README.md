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
- **Save announcements**: Users can save sales announcements for future reference or to review later when making purchasing decisions.
- **Edit and Delete Announcements**: Users can edit and delete their own sale announcements.
- **View the user's other listings**: Users can view other listings posted by a specific seller.
- **Check user reviews**: Users can view reviews left by other users to assess the credibility and reputation of sellers before making a purchase.
- **Real-time chat with sellers**: Buyers can communicate directly with sellers in real time using the built-in chat feature, enabling seamless communication and negotiation.
- **Order a sold game disk**: Buyers can place orders for sold game discs to be shipped to their address.
- **Order Cancellation**: Allows users to cancel an order for a sold game disc within 1 day of placing the order, provided it has not yet been shipped.


## Running the application

This repository provides two main configurations for running the application: locally and within Docker, or deploying the backend microservices to Kubernetes using a Helm chart.

## Running Locally

The repository includes a `docker-compose.yml` file that sets up several supporting services: Redis, ZooKeeper, Kafka, Kafka UI, and an NGINX reverse proxy. To start these services, simply run:

``` bash
docker-compose up
```

Next, MySQL needs to be installed locally. Once MySQL is installed, the user should create the necessary databases for the microservices by executing the following SQL commands:

``` SQL
CREATE DATABASE cgs_announcements;
CREATE DATABASE cgs_messages;
CREATE DATABASE cgs_orders;
CREATE DATABASE cgs_users;
```

It is important to ensure that the MySQL user and password match the configuration in the `application-jpa.yml` files located in each microservice within the backend folder at `\src\main\resources\application-jpa.yml`.

Once the databases are created, each microservice can be launched.

Finally, to start the client application, the user should navigate to the client folder. First, the necessary dependencies must be installed, then the application can be launched:
``` bash
npm install
npm start
```

## Deploying microservices to Kubernetes with Helm


To deploy the application to a Kubernetes cluster using Helm, the repository includes a Helm chart located in the `deploy\helm\game-sale-app` folder. 

### Prerequisites

Before deploying the application using the provided Helm chart on Kubernetes, ensure the following prerequisites are met:

1. Access to a Kubernetes Cluster:

Access to a Kubernetes cluster is required where the application will be deployed.

2. Ingress Controller Enabled:

Ingress must be enabled on your Kubernetes cluster to route external traffic to the deployed services. If you are using Azure Kubernetes Service (AKS), you can enable application routing with the following command:
```
az aks approuting enable -g <resourceGroupName> -n <clusterName>
```

3. Access to Private Docker Registry:

The Helm chart uses images stored in a private Docker registry. Each microservice in the repository has a `Dockerfile` that builds the Docker image for that microservice. These Docker images need to be accessible from the private registry using a Kubernetes Secret named `registry-secret`.

To create the registry-secret in Azure AKS, the following kubectl command can be used:
```
kubectl create secret docker-registry registry-secret --docker-server=<registryName> --docker-username=<username> --docker-password=<password>
```

To push Docker images to an Azure Container Registry (ACR) and make them available for deployment on Azure AKS, the following steps are required:

Login to Azure Container Registry
```
az acr login --name <registryName>
```

Tag and Push Docker Images:
``` 
docker tag announcementservice:v2 <registryName>.azurecr.io/announcementservice:v2
docker push <registryName>.azurecr.io/announcementservice:v2

docker tag userservice:v2 <registryName>.azurecr.io/userservice:v2
docker push <registryName>.azurecr.io/userservice:v2

docker tag messageservice:v2 <registryName>.azurecr.io/messageservice:v2
docker push <registryName>.azurecr.io/messageservice:v2

docker tag orderservice:v2 <registryName>.azurecr.io/orderservice:v2
docker push <registryName>.azurecr.io/orderservice:v2

docker tag orchestrator:v2 <registryName>.azurecr.io/orchestrator:v2
docker push <registryName>.azurecr.io/orchestrator:v2

docker tag websocketservice:v2 <registryName>.azurecr.io/websocketservice:v2
docker push <registryName>.azurecr.io/websocketservice:v2
```

The deployments utilize these names, although they are configurable within the deployment files.

### Deploying the microservices

The Helm chart is configured with dependencies for Redis, MySQL, and Kafka, specified in the `Chart.yml` file, using Bitnami Helm charts.

The `templates` folder within the Helm chart contains several YAML files that are essential for deploying the services. 

These include configurations for **Secrets** to securely manage application configurations. There is also an **Ingress** resource to make the backend services accessible and route requests to the appropriate microservices. For the Kafka UI, a **deployment** and **load balancer** service is configured to ensure that the topic visualization is accessible from outside the cluster. In addition, each microservice has its own **deployment, service, and Horizontal Pod Autoscaler (HPA)** configurations to manage scaling and service availability.

To deploy the backend services the following command can be used in the `deploy/helm` folder:
``` bash
helm install microservice-app ./game-sale-app
```

### Uninstalling the backend

Uninstalling the application deployed via Helm:
```
helm uninstall microservice-app
```

#### Notes:

Persistent Volume Claims (PVCs): The PVCs created by dependencies (such as Redis, MySQL, and Kafka) are not automatically deleted by Helm. You must delete these PVCs manually if needed.

The **registry-secret** created before deploying the application also needs to be deleted separately when uninstalling the application:
```
kubectl delete secret registry-secret
```

## Application Architecture

The application is designed using a microservices architecture consisting of six different microservices: Announcement Service, User Management Service, Order Service, Messaging Service, WebSocket Service, and Orchestrator Service. Each microservice is responsible for specific functionalities and communicates with others through Kafka topics.

### Microservices Overview

1. **User Management Service**
    - **Responsibilities**:
       - User authentication (login, registration, logout)
       - Managing user information
       - Storing user reviews
    - **Database**: Manages its own database for   user-related data

2. **Announcement Service**:
    - **Responsibilities**:
        - Managing sale announcements (create, find, update, delete)
        - Storing which users have saved specific announcements
    - **Database**: Manages its own database for announcements and related information

3. **Order Service**:
    - **Responsibilities**:
        - Managing orders (create, delete)
    - **Database**: Manages its own database for order-related data

4. **Messaging Service**:
    - **Responsibilities**:
        - Saving and managing messages
    - **Database**: Manages its own database for message-related data

5. **WebSocket Service**:
    - **Responsibilities**:
        - Facilitating real-time communication between server and client
        - Receiving messages sent by users
        - Sending chat histories
        - Handling responses for requests that take longer to process (e.g., order creation/deletion)
    - **Database**: No dedicated database required

6. **Orchestrator Service**:
    - **Responsibilities**:
        - Processing order get requests: If data is available in Redis cache, it sends a response; otherwise, it instructs corresponding services to fetch the data and send it back through WebSocket.
        - Managing the flow of transactions for placing and canceling orders
    - **Database**: No dedicated database required


### Data Management

Each service, except the Orchestrator Service and the WebSocket Service, maintains its own database, ensuring that data is properly isolated and managed by the appropriate service.

### Communication


Inter-service communication is facilitated by Kafka topics, which ensure efficient and reliable message processing. Kafka topics are dynamically created by the first producer service to streamline microservice setup and scaling.

### Cache Management

The application uses a Redis cache to store data, improving performance by providing fast access to frequently requested data, minimizing latency, and improving the overall user experience.

### Transaction management

Requests to create or cancel an order are managed as a transaction using the Saga design pattern. This ensures that each step of the transaction is completed successfully, or that compensating actions are taken to undo any partial changes.


1. **Initiation**: 
    - The transaction begins when the orchestrator receives a request to create or cancel an order.

2. **Order Service**:
    - The orchestrator sends data to the Order Service to create or cancel the order.
    - The Order Service processes the request and sends a response back to the orchestrator indicating success or failure.

3. **Announcement Service**:
    - If the Order Service succeeds, the orchestrator sends a request to the Announcement Service to update the announcement status (sold/not sold).
    - The Announcement Service processes the request and sends a response back to the orchestrator indicating success or failure.
    - If successful, the announcement event is saved.

4. **Compensation**:
    - If the orchestrator receives a failed response from any service, it sends compensation messages to the services that need to revert the changes made during the transaction.

5. **Response to Client**:
    - At the end of the transaction, the orchestrator sends data to the WebSocket Service.
    - The WebSocket Service communicates the final outcome to the client, indicating whether the request succeeded or failed along with the relevant data.

![Transaction Management Diagram](fig/transaction%20diagram.png)


## Used Technologies

The platform utilizes the following technologies:

- **Spring REST API**: Using Spring, the platform provides a robust REST API for efficient communication between the backend and the frontend. The API uses JSON for data exchange, ensuring lightweight and readable communication.

- **MySQL Database**: The platform uses MySQL for efficient data storage and management, configured in the `application-jpa.yml` file under
`backend/src/main/resources/application-jpa.yml`. 

- **React**: The platform's frontend is built with React, creating a dynamic user interface.
  
- **Redis**: Redis is employed for caching data, enhancing performance by storing frequently accessed information in memory.
  
- **WebSocket**: WebSocket technology enables real-time communication between users.
  
- **Kafka**: Apache Kafka is used for communication between microservices, facilitating efficient and reliable message processing and exchange across the platform's distributed systems.
  


### Similar Products

1. GameFlip
2. eBay's video game section
3. Amazon's marketplace for video games
4. OfferUp's gaming category in the US
5. OLX gaming section
