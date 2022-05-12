# BookBae

## Project Overview:
   
Our goal is to create a dating/friendship app that matches people based on their literary preferences, aptly named “BookBae.” A user’s profile on BookBae includes all the normal aspects of a dating profile, in addition to a “bookshelf.” A bookshelf is a collection of books the user selects to represent themselves. This lets users showcase their interests and personality in a unique way.

Our goal is for BookBae to be a well-rounded swipe-based dating app. Users are prompted to swipe on profiles of other users that like similar genres and meet their set parameters. Like most swipe-based dating apps, swiping right conveys interest, while swiping left conveys disinterest. If two users both swipe right on each other, they are "matched" and can begin a conversation in-app. Users are encouraged to draw on their similar literary preferences to start their conversation.
  
<br/>

## Layout:

The repository is primarily organized by functionality, with the main sections being frontend, backend, and database. A short description of each internal folder is provided below:

### Frontend:

The *Frontend* folder stores all code relevant to the frontend of the project. This includes JS files, CSS files, markup, and more. As a general rule, the *Frontend* folder contains the code that the client will interact with (e.g., GUI). The *Frontend* folder should **not** store any of the project's functional Backend, which the user does not see. 

### Backend:

The *Backend* folder stores all code relevant to the backend of the project. This code is more functional than the frontend and is not accessible to the user. Instead, the *Backend* folder contains the code necessary for the application to operate properly (e.g., user-matching algorithms). For this project, backend code is primarily written in Java. The *Backend* folder also contains subfolders separating the Java code from its test suites (see ./backend/README.md for more).

### Database:

The *Database* folder holds SQL to manipulate the database. This includes a SQL file to create the database, files to test the database, and more.

### Prototypes:

The *Prototypes* folder stores semi-relevant code that demonstrates a core project function. For example, the "googleAPI-tutorial.js" file demonstrates how to use the Google Books API to fetch information used in the app. Although the exact implementations are not used directly in the app, they depict how the  necessary tools function. 

### Reports:

The *Reports* folder stores the weekly project report markdown files. These reports provide status updates for the TAs and outlines the next project meeting. Additionally, the reports have weekly goals, progress reports, and future goals for next week.
  
<br/>

## Other:

### Branch Protection:

This project has implemented branch protection. Contributors to the main branch must submit requests for their code to be reviewed before merging into the main branch. 

### Milestones:

The main BookBae project has a "Projects" tab which outlines the milestones within the course (e.g., System Architecture, Testing, etc.). Each project milestone will detail the individual/group tasks necessary, as well as a task's status.

### Set up Instructions

These are the set up instructions to set up an instance of our server. For the final turn in, we will host the server on Azure.

1. Build the backend .war file
    - In command line, navigate to the backend folder
    - run "./gradlew build" this command will build an .war file called api.war within backend/build/libs
    
2. Download Glassfish, our interim server, and start the server (@Joshua what to say besides "start the server"?)
    - Go to https://projects.eclipse.org/projects/ee4j.glassfish/downloads and download Glassfish version 6.2.5 Full Profile
    - Extract the glassfish-6.2.5.zip to a safe place
    - In command line, navigate to the glassfish-6.2.5 folder
    - In command line, run the following command:
      On Unix: glassfish6/glassfish/bin/asadmin start-domain
      On Windows: glassfish6\glassfish\bin\asadmin start-domain
      
3. Upload the .war file
   - In your browser, navigate to localhost:4848. This will pull up the Glassfish configuration portal
   - On the left navigation bar, click "Application" to navigate to the application page
   - Click "deploy". This will prompt you to upload a .war file. Chose the api.war file within backend/build/libs
   - In the "Context Path" blank, write "/api"
   - Click "Ok" to save
    
4. Configure properties
    - On the left navigation bar, click "server config". It is the last option.
    - Click JVM ________
    - Click the JVM Options tab 
    - delete the javax.net.ssl.keyStore and javax.net.ssl.trustStore properties
    - Add the following four properties. Contact BookBae team for password.
      "-Dbookbae.server_url=bookbaeserver.database.windows.net'
      "-Dbookbae.database_name=BookBaeDB'
      "-Dbookbae.username=TeamBookBae@bookbaeserver'
      "-Dbookbae.password=<password>'
    - Click "Ok" to save
    - Press the "Restart Server" button to save changes
      
    At this point your server is set up and ready to take requests!
   
5. (Optional) Test your server connection
    - From the Application page (Click "Application" on left navigation tab)
    - Click "Launch". This will load a page with two launch links to choose from.
    - Click the first link, the one at port 8080
    - add "/v1/user" to the url
    - If you see a 401 server error (as opposed to a 404 server error) you are properly configured. At this point, the server is denying your request only because you have not provided the appropriate token.
