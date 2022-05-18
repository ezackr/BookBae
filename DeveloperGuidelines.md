# Developer Guidelines:

## How to Obtain Source Code:

Copy the link from the top-level directory to clone the repository on your local device. Follow the instructions in the top-level README.md document to setup the necessary frontend and backend dependencies. We recommend using Android Studio to run the application on an emulator. There are no extra/hidden repositories.

## Layout of Directory Structure:

The top-level directory of the repository stores relevant project code in its frontend, backend, and database subdirectories. See the top-level README.md file for a general overview of these directories. 

### Database:

The database folder outlines the structure of the SQL database being used, with helpful graphics/overviews in its README file. Depicts how application data is stored.

### Backend:

The backend folder contains the structure for the REST API that powers the project. The folder also has the gradle files used to build the project. 

### Frontend:

The frontend folder contains an immediate subdirectory titled 'BookBae' hosting the React Native development environment. Tests are stored in the "tests" folder, using Jest to test the components. The "android" folder builds the application, using the command "npx react-native run-android", on an emulator. The main BookBae frontend folder also has the App.js file, which outlines navigation between screens. The Client.js file provides the frontend development with connections to the backend/database. The "screens" folder stores the main app screens, with an extra folder dedicated to the onboarding process.

## How to Build:

## How to Test:

## Adding New Tests:

## How to Build a Release:
