# Api Integration V5

A simple Android application developed in Kotlin as part of a Fullstack Assignment at GritAcademy.

The application demonstrates authentication, FireBase Realtime Database, REST API integration, Fragment navigation and weather + country information.

## Features

- User registration and login using Firebase Authentication
- User profile stored in Firebase Realtime Database
- View and edit profile information
- Weather search using the Open-Meteo API
- Country information using the Countries.dev API
- Navigation between multiple Fragments
- Logout functionality
- Support for portrait and landscape orientation
- Kotlin-based Android application

## APIs 

### Open-Meteo

The application uses Open-Meteo to retrieve weather information based on selected city.

Information displayed includes:

- City
- Temperature
- Weather condition
- Wind speed

API:
https://open-meteo.com/


### Countries.dev

The application also uses Countries.dev to retrieve information about countries.

Information displayed includes:

- Country name
- Capital
- Population
- Region

API:
https://countries.dev/


## Firebase

Firebase is used for user authentication and storing user profile information.

Firebase Authentication handles:

- Email
- Password
- User authentication
- Login and logout

Firebase Realtime Database stores profile information such as:

- Username
- Full name
- Email
- Gender
- Date of birth

Passwords are handled by Firebase Authentication and are not stored manually in the database.
