**Team Report**
Goals From Last Week (5/4 - 5/11): Last week, we finished the basic components needed for the beta release. We added endpoints for chat, likes, and recommendations, as well as tests for these. We also added screens in the app for each step in the onboarding process, as well as a home screen with a navigation bar.

This Week (5/12 - 5/18): This week, we polished our components from the Beta, added more functionality and tests, and set up Azure Blob Storage. This involved adding endpoints to the server to check whether an email is in use, and storing the user's books and preferences. We  added methods to Client.js to make requests to these endpoints, which are called from the appropriate app screens. We also added unit tests for the additional methods in Client.js and for all the react components (login screen, swiping screen, etc.) as well as some integration tests. Finally, we set up a photos endpoint to connect to Azure Blob Storage.

Goals For Next Week (5/19 - 5/25): Our goals for next week are to implement input validation for onboarding, make the user profile screen, implement image uploading on the frontend, improve the recommendation algorithm, continue working on coding and improve code style.

**Individual Contributions**

Goals From Last Week (5/4 - 5/11):
-Isabelle: Finished creating onboarding sequence.
-Emily: Added endpoints to server (chat, recommends, like).
-Helena: Added code to make frontend make requests to server and wrote unit tests.
-Joshua: Added endpoints to server and helped troubleshoot frontend connection to server.
-Elliott: Added home screen, book search screen, and navigation. Tested app on phone.

This Week (5/11 - 5/18):
-Isabelle: Due to unexpected illness, I will need to postpone my goals to next week.
-Emily: Added endpoints to check whether an email is in use, to store/retrieve the user's books and to save preferences and unit tests to test them.
-Helena: Improved onboarding experience, added methods to Client.js to connect frontend to new endpoints, wrote tests for Client.js and each onboarding component
-Joshua: Created endpoint to post/get photos and worked on integration testing
-Elliott: Make screen to load user profiles to swipe on fully functional.

Goals For Next Week (5/19 - 5/25):
-Isabelle: Make user profile screen, including an edit mode. Add input validation checks to onboarding.
-Emily: Clean up endpoints code to reduce duplicated code, improve style, add more unit tests, improve recommendations algorithm.
-Helena: Implement image uploading for front end
-Joshua: Continue working on integration testing, improve existing documentation
-Elliott: 