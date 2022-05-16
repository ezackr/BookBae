# Bookbae Backend

## Introduction

This is the backend software that drives the BookBae app. 
It is a 'Jakarta RESTful Web Services' application that can be deployed to any standards-compliant Java EE web server. 
It exposes a REST API that clients will use to read and modify data.

## Building

The application is built with 'gradle build' (or possibly './gradlew build' or '.\gradlew build')
The above command builds a file 'build/libs/api.war' which can be deployed to a Jakarta EE server like glassfish

## Running

To run this application, you will need a Jakarta EE server. The server will come with instructions that will tell you how to deploy .war files like the api.war file built by this application

The application can often be deployed by uploading api.war to the server's builtin web management interface - both TomEE and Glassfish support this. 

## User Authentication

As written, the application uses JWT - the client gets a JWT token from the /login endpoint and places it in the Authorization header for all subsequent API calls.

## Client API

The server runs the application from a specific path, often called a 'context'. This is usually the name of the .war file (so in this case, api) and if you have the option to set it, you should set it to 'api'. The application then has it's own path (determined by the value of the ApplicationPath annotation on the Application subclass). This is "v1" for the current version of the aplication. If we are ever in a situation where we rewrite the API presented to the client after a full release to a wider userbase, we could then use "v2" and continue to provide the older API to older clients at "v1". Therefore, the full root path for all requests to this server is `http://ip:port_number/api/v1`. 

The application offers a number of API endpoints accessible from the root path. (API endpoints are found in classes annotated with @Path. The value of the Path annotation is appended to the root path given above to get the path that you would direct your requests to.) Current endpoints include: 

- /user - for the authenticated user (the client)
    - GET
        - Gets the following user data for the authenticated user
        - Produces: `{“email”: “<email>”, “name”: “<name>”, “preferredGender”: “<preferredGender>”, “gender”: “<gender>”, “favGenre”: “<favGenre>”, “birthday”: “<birthday>”, “bio”: “<bio>”, “zipcode”: “<zipcode>”}`
        - Returns 403 response code if user does not exist
    - PUT
        - Saves the passed data for the authenticated user
        - Consumes: `{“email”: “<email>”, “name”: “<name>”, “preferredGender”: “<preferredGender>”, “gender”: “gender”, “favGenre”: “favGenre”, “birthday”: “<birthday>”, “bio”: “<bio>”, “zipcode”: “<zipcode>”}`
        - Produces: `{“email”: “<email>”, “name”: “<name>”, “preferredGender”: “<preferredGender>”, “gender”: “<gender>”, “favGenre”: “<favGenre>”, “birthday”: “<birthday>”, “bio”: “<bio>”, “zipcode”: “<zipcode>”}`
    - Does Not Return USERID!
    - Birthday must be of the form "yyy-mm-dd"
- FUTURE OPTION: /user/{userid} : gets the above object for a specific userid
- /email?email=<email@email.com>
    - GET
      - Returns true if email is in the database, false otherwise
      - Produces: `{“emailexists”: “<boolean>}`
    - Returns 404 response code if email is null
- /create
    - POST
        - Creates a user account with the given email and password
        - Consumes: `{“email”: “<email>”, “password”: “<password>”}`
        - Returns 403 response code if email already in use
- /login
    - POST
        - Attempts to login with the given email and password, upon successful login returns an auth token to use in future calls
        - Consumes: `{“email”: “<email>”, “password”: “<password>”}`
        - Produces: `{“authToken”: “<token>”}`
        - Returns 403 response code if email or password are wrong
- /recommends
    - GET
        - Returns a list of recommended users
        - Produces: `[{“userid”: “<userid>”, “name”: “<name>”, “preferredGender”: “<preferredGender>”, “gender”: “<gender>”, “favGenre”: “<favGenre>”, “birthday”: “<birthday>”, “bio”: “<bio>”}, ...]`
        - Returns 403 response code if there are no users to recommend (to be changed to return empty list in future version)
    - Will not return email or zipcode to protect privacy!
- /like
    - PUT
        - Saves that the authenticated user likes the given user
        - Consumes: `{"userid": "<userid>"}`
        - Returns 403 response code if client user has already liked the other user
- /chats
    - GET
        - Gets basic information on all chats the authenticated user is involved in
        - Produces: `[{"displayName": "<name>", "photoUrl": "<url>", "lastMessage": "<msg>", "likeId": "<uuid>"}, ...]`
- /chats/{likeId}
    - GET
        - Gets the chat lines from the chat specified by likeId, userid is of the sender
        - Produces: `[{"userid": "<userid>", "timestamp": "<timestamp>", "text": "<text>", "nthMessage": "<nthMessage>"}, ...]`
    - POST
        - Sends a chat from the authenticated user to the chat specified by likeId
        - Consumes: `{"text": "<words>"}`
- /book/get
    - GET
        - Gets a list of book ids for the authenticated user
        - Produces: `[{"bookid": "<bookid>"},...]`
- /book/add
    - PUT
        - Takes in a list of book ids to add to the authenticated user's book list, returns the updated book list
        - Consumes: `[{"bookid": "<bookid>"},...]`
        - Produces: `[{"bookid": "<bookid>"},...]`
- /book/remove
    - PUT
        - Takes in a list of book ids to remove from the authenticated user's book list, returns the updated book list
        - Consumes: `[{"bookid": "<bookid>"},...]`
        - Produces: `[{"bookid": "<bookid>"},...]`

Endpoints return a 500 response code in the event of a SQL error   
The `authToken` returned from the login endpoint is a JWT token that should be kept by the client and must be used to access authenticated endpoints (for now, just /user, but will include photo upload and accessing matches and chat).

Attempting to access a secured resource with an expired JWT will return a response with an UNAUTHORIZED status code, and attempting to access a secured resource with an invalid JWT will return UNAUTHORIZED. If a client recieves an unauthorized response, it should attempt to get a fresh JWT by hitting the /login endpoint again. 

To access a secured resource, the JWT must be passed in the "Authorization" HTTP header. The header must have a value of `Bearer <insert token here>`, where the token is the value of the `authToken` field recieved from the /login endpoint.

## Glassfish Caveat

When running on Glassfish (and likely other servers too) you need to configure some system properties. Specifically, you need to set the following four properties appropriately:

-Dbookbae.server_url=
-Dbookbae.database_name=
-Dbookbae.username=
-Dbookbae.password=

Additionally, you need to unset the following properties (so Glassfish uses the system defaults, which are appropriate for now):

- javax.net.ssl.keyStore
- javax.net.ssl.trustStore

These changes can be made under the options tab found by following Configurations > server-config > JVM Settings
