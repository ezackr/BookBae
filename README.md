# BookBae

## Project Overview:
   
Our goal is to create a dating/friendship app that matches people based on their literary preferences, aptly named “BookBae.” A user’s profile on BookBae includes all the normal aspects of a dating profile, in addition to a “bookshelf.” A bookshelf is a collection of books the user selects to represent themselves. This lets users showcase their interests and personality in a unique way.

Our goal is for BookBae to be a well-rounded swipe-based dating app. Users are prompted to swipe on profiles of other users that like similar genres and meet their set parameters. Like most swipe-based dating apps, swiping right conveys interest, while swiping left conveys disinterest. If two users both swipe right on each other, they are "matched" and can begin a conversation in-app. Users are encouraged to draw on their similar literary preferences to start their conversation.
  
<br/>

## Layout:

The repository is primarily organized by functionality, with the main sections being frontend, backend, and database. A short description of each internal folder is provided below:

### Frontend:

The *Frontend* folder stores all code relevant to the frontend of the project. This includes JS files, CSS files, markup, and more. As a general rule, the *Frontend* folder contains the code that the client will interact with (e.g., GUI). The *Frontend* folder should **not** store any of the project's functional Backend, which the user does not see. See ./frontend/README.md for instructions on how to build and test the frontend code.

### Backend:

The *Backend* folder stores all code relevant to the backend of the project. This code is more functional than the frontend and is not accessible to the user. Instead, the *Backend* folder contains the code necessary for the application to operate properly (e.g., user-matching algorithms). For this project, backend code is primarily written in Java. The *Backend* folder also contains subfolders separating the Java code from its test suites. See ./backend/README.md for instructions on how to build and test the backend code.

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

### Continuous Integration:

This project uses GitHub Actions for continuous integration. Pull requests against main and pushes to main all must pass the CI tests. The continuous integration configuration is split into two parts, one for the backend and one for the frontend. The backend CI configuration ignores merges that effect only the frontend directory, and the frontend CI configuration ignores merges that effect only the backend. The backend CI uses GitHub's gradle integration to do a full build and test of the backend code. The frontend CI uses npm to run the Jest tests then builds a release of the app using the gradle configuration.
