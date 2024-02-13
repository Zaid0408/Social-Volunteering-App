# Social Volunteering App

Welcome to the Social Volunteering App, a dynamic platform designed to connect organizations, NGOs, and enthusiastic volunteers. Developed using Android Studio with XML for layout design and Kotlin for functionality, the app integrates seamlessly with Firebase for both authentication and real-time database storage.

## Key Features

### User Authentication

- **Sign Up and Login:**
  - Users can create accounts using their email and password.
  - The login page intelligently distinguishes between regular users, organizations, and administrators.

### Events Feed

- **Post and View Events:**
  - Organizations can seamlessly post upcoming events with detailed information.
  - Users can browse through the dynamic events feed, gaining insights into date, venue, location, and event descriptions.

### Volunteer Registration

- **Interactive Volunteer Form:**
  - Users interested in volunteering can fill out a comprehensive form, providing essential details such as their name and email.
  - Submitted forms are securely stored in the Firebase database, enabling event organizers to access the volunteer list.

### Organization Features

- **Event Management:**
  - Organizations have the privilege of adding new posts about upcoming events.
  - Admins can view detailed lists of volunteers for specific events and manage them by removing entries.

### Admin Privileges

- **Post and Account Management:**
  - Admins have the power to delete specific posts and user accounts, ensuring streamlined content moderation.

## Technology Stack

### Frontend

- **Android Development:**
  - Developed using Android Studio.
  - XML for expressive and responsive layout design.
  - Kotlin for robust and scalable application logic.

### Backend

- **Firebase Integration:**
  - Firebase Authentication ensures secure user, organization, and admin authentication.
  - Firebase Realtime Database handles the storage of account details, posts, and volunteer registrations.

## Implementation Details

- **Login Page:**
  - Users are authenticated based on their account type (user, organization, or admin).
  - Firebase Authentication verifies and securely manages user credentials.

- **Signup Page:**
  - Users and organizations can create accounts using the signup feature.
  - Account details are stored in Firebase Authentication and Realtime Database.

- **Events Feed:**
  - Event details are dynamically loaded from the Firebase database.
  - Users can click on a specific event to view detailed information.

- **Volunteer Registration:**
  - Users can fill out a form with their name and email to register as volunteers for specific events.
  - Volunteer registrations are stored in the Firebase database for organizational access.

- **Admin Panel:**
  - Admins have an intuitive panel to manage posts, volunteer lists, and user accounts.
  - Admins can delete posts, remove volunteers from events, and manage user accounts.

## Future Enhancements

- **Enhanced User Profiles:**
  - Implement user profiles with additional information to foster a sense of community.

- **Notifications:**
  - Incorporate push notifications to keep users, organizations, and admins informed about activity within the app.

## Contributers
Special thanks to the following contributors for their valuable contributions to the Social Volunteering App:

- [@Zaid0408](https://github.com/Zaid0408): Lead Developer, Firebase Integration
- [@Shreyas-1s](https://github.com/Shreyas-1s): UI/UX Design
- Vyshnavi : UI/UX Design
