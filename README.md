 Movie Application

Overview
The Movie Application is a Kotlin-based Android app developed using Android Studio. The application allows users to register, log in, and search for movies conveniently through an interactive and modern user interface. 

The app integrates Firebase Authentication for secure user login and registration, and utilizes an API key to fetch real-time movie information from an external movie database.


Features

User Authentication
- Users can log in using their registered email and password.  
- New users can sign up easily through the registration page.  
- All passwords are securely stored and managed using Firebase Authentication.

 Startup Screen
- Displays the app logo upon startup.  
- Prompts the user to enter their email address and password.  
- Provides a “Register” option for new users.

 Movie Search
- After logging in, users are taken to the Movie Search Page.  
- Users can search for any movie by entering the title.  
- The application displays movie results fetched using the Movie API key.

Firebase Integration
- Firebase Database is used to store user data securely.  
- Supports real-time synchronization and cloud-based data access.


Technologies Used

| Component | Technology |
|------------|-------------|
| Language | Kotlin |
| IDE | Android Studio |
| Database | Firebase Realtime Database |
| Authentication | Firebase Authentication |
| API Integration | Movie Database API (via API key) |
| UI Design | XML Layouts and Material Design Components |

Setup & Installation

1. Clone the Repository
   ```bash
   git clone https://github.com/st10176153/MovieApp.git
   ```
2. Open Project in Android Studio
   - Open Android Studio → *File* → *Open* → Select the project folder.

3. Configure Firebase
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new Firebase project.
   - Add your Android app’s package name.
   - Download the `google-services.json` file and place it inside your app directory.

4. Add API Key
   - Obtain your Movie Database API key.
   - Store it securely in your app (e.g., `local.properties` or `gradle.properties`).

5. Build and Run the App
   - Connect your Android device or emulator.
   - Click Run in Android Studio.


 App Flow

1. Splash Screen - App logo display  
2. Login Page - Enter credentials or register  
3. Registration Page - Create a new user account  
4. Movie Search Page - Search and display movie details  


Security Notes
- Passwords are encrypted using Firebase Authentication.
- Sensitive information like API keys should never be hardcoded in the source code.
- Always use environment variables or secure storage for credentials.
