# BookBae

## Project Overview:

## Layout:

The repository is primarily organized by functionality, with the main sections being frontend, backend, and database. A short description of each internal folder is provided below:

### Frontend:

The *Frontend* folder stores all code relevant to the frontend of the project. This includes JS files, CSS files, markup, and more. As a general rule, the *Frontend* folder contains the code that the client will interact with (e.g., GUI). The *Frontend* folder should **not** store any of the project's functional Backend, which the user does not see. 

### Backend:

The *Backend* folder stores all code relevant to the backend of the project. This code is more functional than the frontend and is not accessible to the user. Instead, the *Backend* folder contains the code necessary for the application to operate properly (e.g., user-matching algorithms). For this project, backend code is primarily written in Java. The *Backend* folder also contains subfolders separating the Java code from its test suites (see ./backend/README.md for more).

### Database:

The *Database* folder holds relevant code for the app to interact with user data. This includes SQL files, connecting to the database, and more.

### Prototypes:

The *Prototypes* folder stores semi-relevant code that demonstrates a core project function. For example, the "googleAPI-tutorial.js" file demonstrates how to use the Google Books API to fetch information used in the app. Although the exact implementations are not used directly in the app, they depict how the  necessary tools function. 

### Reports:

The *Reports* folder stores the weekly project report markdown files. These reports provide status updates for the TAs and outlines the next project meeting. Additionally, the reports have weekly goals, progress reports, and future goals for next week.

## Other:

### Branch Protection:

This project has implemented branch protection. Contributors to the main branch must submit requests for their code to be reviewed before merging into the main branch. 
