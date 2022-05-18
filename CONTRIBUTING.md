# Developer Guidelines:

## How to Obtain Source Code:

Copy the link from the top-level directory to clone the repository on your local device. Follow the instructions in the top-level README.md document to setup the necessary frontend and backend dependencies. We recommend using Android Studio to run the application on an emulator. There are no extra/hidden repositories.

## Layout of Directory Structure:

The top-level directory of the repository stores relevant project code in its frontend, backend, and database subdirectories. See the top-level README.md file for a general overview of these directories. 

### Database:

The database folder outlines the structure of the SQL database being used, with helpful graphics/overviews in its README file. Depicts how application data is stored.

### Backend:

The backend folder contains the structure for the REST API that powers the project. 
- .gradle:
  - This folder contains the gradle files used to build the project
- build:
  - This file contains the files built by gradle, including the api.war file used in deploying the server and various testing logs
- gradle:
  - This folder contains the gradle wrapper used to build the project
- src/main/java/com/bookbae/server:
  - json: This folder contains the json classes used to send data to and from the server
  - security: This folder contains classes needed to implement security on the backend, like authentication tokens
  - service: This folder contains implementations of interfaces
  - Floating Files: The remaining floating files are our resource files, which contain the server functionality
- src/test/java: This folder contains our JUnit tests and mock files

### Frontend:

The frontend folder contains an immediate subdirectory titled 'BookBae' hosting the React Native development environment. Tests are stored in the "tests" folder, using Jest to test the components. The "android" folder builds the application, using the command "npx react-native run-android", on an emulator. The main BookBae frontend folder also has the App.js file, which outlines navigation between screens. The Client.js file provides the frontend development with connections to the backend/database. The "screens" folder stores the main app screens, with an extra folder dedicated to the onboarding process.

## How to Build/Test:

For the backend, navigate to the backend folder and run "./gradlew build" to build the api.war file which will be uploaded to Glassfish. Building the api.war file will automatically run the automatic test suite. For the frontend, navigate into the lower-level BookBae repository within the frontend folder. By running the application using "npx react-native start" and "npx react-native run-android" (in a separate terminal from the first command), the testing suite checks if the frontend components are functional and if the build is successful.

Building a release of the software does not require updating a version number as the software does not have defined versions. Although this feature is optional for developing users.

## Adding New Tests:

### Backend:
Back end tests:
Currently, the backend unit tests are spread out over several files within backend/src/test:
BookTest.java, 
ChatsTest.java, 
CreateAccountTest.java, 
EmailTest.java, 
LoginTest.java, 
UserTest.java, 
LikeTest.java, 
RecommendsTest.java, 
UserTest.java

To add a test, add a method containing your test to one of the above files and mark the method with an @Test annotation. For naming test files, simply name the class, followed by "Test". Within each Test class should be atomic unit tests of the class's functionality.

### Frontend:
Add new tests to /frontend/BookBae/__tests__. These tests should use the Jest testing framework. It is best to name the test files by appending "-test.js" to the name of the file being tested. 

