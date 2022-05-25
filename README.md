# BookBae

<a href="https://github.com/ezackr/BookBae/blob/main/UserManual.md">User Manual</a>

<a href="https://github.com/ezackr/BookBae/blob/main/CONTRIBUTING.md">Developer Guidelines</a>

## Layout:

The repository is primarily organized by functionality, with the main sections being frontend, backend, and database. A short description of each internal folder is provided below:

### Frontend:
<a href="https://github.com/ezackr/BookBae/blob/main/frontend/README.md">Frontend Readme</a>

The *Frontend* folder stores all code relevant to the frontend of the project. This includes JS files, CSS files, markup, and more. As a general rule, the *Frontend* folder contains the code that the client will interact with (e.g., GUI). The *Frontend* folder should **not** store any of the project's functional Backend, which the user does not see. 

### Backend:
<a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a>

The *Backend* folder stores all code relevant to the backend of the project. This code is more functional than the frontend and is not accessible to the user. Instead, the *Backend* folder contains the code necessary for the application to operate properly (e.g., user-matching algorithms). For this project, backend code is primarily written in Java. The *Backend* folder also contains subfolders separating the Java code from its test suites (see ./backend/README.md for more).

### Database:
<a href="https://github.com/ezackr/BookBae/blob/main/database/README.md">Database Readme</a>

The *Database* folder holds SQL to manipulate the database. This includes a SQL file to create the database, files to test the database, and more.

### Prototypes:
<a href="https://github.com/ezackr/BookBae/blob/main/prototypes/README.md">Prototypes Readme</a>

The *Prototypes* folder stores semi-relevant code that demonstrates a core project function. For example, the "googleAPI-tutorial.js" file demonstrates how to use the Google Books API to fetch information used in the app. Although the exact implementations are not used directly in the app, they depict how the  necessary tools function. 

### Reports:

The *Reports* folder stores the weekly project report markdown files. These reports provide status updates for the TAs and outlines the next project meeting. Additionally, the reports have weekly goals, progress reports, and future goals for next week.
  
<br/>

## Other:

### Branch Protection:

This project has implemented branch protection. Contributors to the main branch must submit requests for their code to be reviewed before merging into the main branch. 

### Milestones:

The main BookBae project has a "Projects" tab which outlines the milestones within the course (e.g., System Architecture, Testing, etc.). Each project milestone will detail the individual/group tasks necessary, as well as a task's status.

### Set up Instructions (Backend)

1. Generate a new, project-specific ssh key
  - On macOS or Linux, use your favorite terminal to navigate to the directory `~/.ssh`
  - On Windows, use Powershell to navigate to `~/.ssh`
    - Regardless of what terminal environment is used on Windows (MinGW, WSL, Cygwin), for this specific use case, the native port of OpenSSH should be used via PowerShell
    - macOS and Linux operating systems provide their own ssh implementation which is almost certainly OpenSSH as well
  - `ssh-keygen -f bookbae`
    - If it asks if you wish to overwrite, you likely should decline and replace `bookbae` in the above command with another name - if you do so, note that name down for later
2. Configure your ssh
  - Open the ssh config file `~/.ssh/config` for editing
  - Append the following to the file
```
Host bookbae
Hostname <hostname>
IdentityFile ~/.ssh/bookbae
LocalForward 4848 127.0.0.1:4848
LocalForward 8080 127.0.0.1:8080
ExitOnForwardFailure yes
```
  - If you noted down an alternate name earlier, you should substitute it for "bookbae" in the line beginning with "IdentityFile", but not the line beginning "Host"
  - Contact the BookBae Team for the hostname
3. Connect
  - Open the file `~/.ssh/bookbae.pub`. This is your public key. If you noted down an alternate name, substitute it. Contact the BookBae team with the contents of this public key file so they may add it to the list of accepted keys. Once they have added it, you are free to move on to the next step
  - Type `ssh bookbae` into your favorite terminal or PowerShell, depending
  - Until you close the ssh connection, you will now be able to connect to the production GlassFish server as if it were running on your local machine! Visit http://localhost:4848 to view the Admin Console

### Set up Instructions (Frontend)
   
These are the setup instructions for the frontend components. These let a user run the application on an android device or an emulator in Android Studio.

Setup For Emulator:
   Create an Android Virtual Device (AVD) in Android Studio to run the application (recommended: Pixel_3a).
   
Setup for Real Device:
   Plug the device into your computer that you are using to run Android Studio. 

Starting the app:
1. From the top level "BookBae" in the repository, enter the frontend section by running "cd ./frontend/BookBae" in the command line.
   
2. In the command line, run "npm install" to add the necessary dependencies to the local repository. 
   
3. Run "npx react-native run-android" in the command line to start the application (should open emulator or switch to app screen on real device)
   
4. (Optional) Testing the application:
   - run "npm ci" to add the Jest testing setup.
   - run "npm test" to run tests.
   - Note: all tests run automatically when changes are pushed to branches/main via continuous integration, which is why testing in the local repository is optional.
  
5. (POSSIBLE ERROR) ERROR: JAVA_HOME is set to an invalid directory:
  
  The following error occurs if the JDK is not properly setup for the Android Studio environment. To fix this issue...
  
  For MacOS, if using Catalina and above...
  
  - In the command line, run "vim ~/.zshrc"
  - Add the following lines to the file:
  
      export ANDROID_SDK_ROOT=$HOME/Library/Android/sdk
  
      export PATH=$PATH:$ANDROID_SDK_ROOT/emulator
  
      export PATH=$PATH:$ANDROID_SDK_ROOT/platform-tools
  
  For other versions of MacOS, follow the instructions here: https://stackoverflow.com/questions/43211282/using-jdk-that-is-bundled-inside-android-studio-as-java-home-on-mac
  
  For Windows, ensure the Android Studio environment is properly setup with a valid path for JDK.
   
### Testing, and CI:

As briefly described above, the repository has implemented continuous integration to test commits/merges to the project. Pull requests and conflicts are resolved by other users (user who made pull request cannot also be the reviewer) to ensure all code is properly reviewed. See the optional testing sections above, and the testing portion of the living document, for further details regarding test types, build system, and more. The continuous integration uses GitHub Actions to catch any potential bugs within the code. In doing so, code is checked *immediately* before any further damage can be dealt.
   
### Functional Use Cases:
   
This section outlines which use cases described from the Living Document are operational in the current implementation.
   
1. User wishes to connect with prospective dates using BookBae: NOT OPERATIONAL
      Although all the necessary components have been outlined, they are not fully linked and operational to meet the definition of the use case.
   
2. User wishes to connect with new friends using BookBae: NOT OPERATIONAL
      Similarly to (1) the features have been established, but are not yet operational.
   
3. User wants to browse profiles, not necessarily trying to find a match: SEMI-OPERATIONAL
      The app currently provides fake sample data when logging in, displaying how real matches will be viewed in the future. The implementation should occur easily and quickly after the necessary endpoints are established in the database.
   
4. User wants to start a conversation with a match: NOT OPERATIONAL
      The messaging screen has been outlined, with established endpoints in the backend, but it has not yet been linked to its corresponding frontend components.

5. User wants to explore the app to see if they might like it: OPERATIONAL
      Onboarding process is fully implemented, allowing user to design their profiles and explore the various screens within the app. 



