# CineSphere
Domain: Movie Recommender and Movie Ticket Booking System
A desktop application that recommends movies to users based on customizable filters and book cinema tickets. 
It allows users to view reviews, manage a personal watchlist by adding and marking movies as watched. 
In addition to personalized movie discovery, the application enables users to book cinema tickets for currently playing movies and select their preferred seats.

**User Stories:**

  1. As a user, I want to securely log in or sign up to the application, so that my watchlist, preferences, and bookings are saved under my account.
  2. As a user I want to search for a movie to see its ratings and reviews.
  3. As a user, I want to be recommended movies based on criteria such as rating, genre, and release date, so that I can find movies that match my preferences and view their reviews from other users.
  4. As a user, I want to view trending or top-rated, upcoming and popular movies.
  5. As a user, I want to add movies to a personal watchlist and mark them as watched, so that I can track what I plan to watch and what I’ve already seen.
  6. As a user, I want to book tickets for movies currently playing in cinemas and select my seats without visiting the theatre.
  7. As a user, I want to sort movies in my watchlist based on the features I want (release date, rating) so I can see which movies I want to see the most. 

**Use Cases**

**Use Case 1: User Authentication (Log in/Sign up)**
- Main Flow: 
  - User opens the application and selects Log in/ Sign up. 
  
  - System prompts the user to enter username and password. 
  
  - If user selects “Log in”, system confirms user authentication and loads the main page. 
  
  - If user selects “Sign up”, system validates input and stores user information and loads the main page. 
  

- Alternative Flows: 
  - If incorrect information when “Log in” → system displays “invalid username/password”. 
  
  - If account already exists when “Sign up” → system displays “account exists”. 


**Use Case 2: Search Movies**
- Main Flow: 

  - User enters a movie title in the Search bar. 
  
  - System retrieves the information using an external API. 
  
  - System displays movies with title, poster, rating. 
  
      User can view more detailed information about the movie:
        - Movie title 
        - Release year 
        - Director 
        - Rating 
        - Genre 
        - Description 
        - Cast? Optional 

- Alternative Flows: 

  - If no movies found → system shows “No movies found” 

**Use Case 3: Get Recommended Movies**
- Main Flow: 

  - User clicks “Recommend Movies”. 
  
  - System displays different filters such as rating, genre, and release date. 
  
  - User selects these filters.
  
  - System displays movies with title, poster, rating. 

- Alternative Flows:
    - If with the selected filters there are no results → system displays “no matches found”
  
  User can view more detailed information about the movie (same as US 2)

**Use Case 4: Browse Movies**
- Main Flow: 

  - User navigates “Trending” tab.
  
  - User selects between top-rated, upcoming and popular categories. 
  
  - System retrieves trending using API call. 
  
  - System displays movies with title, poster, rating. 


- Alternative Flows: 
  - If there are no trending movies at the time, the system displays the message “No movies available” 

User can view more detailed information about the movie (same as US 2)


**Use Case 5: Manage Watchlist**
- Main Flow: 

  - Whenever a movie is displayed to a user, there is the button “Add to watchlist” 
  
  - User clicks “Add to Watchlist”. 
  
  - System adds the movie to the user’s watchlist. 
  
  - User can open the watchlist to view watchlisted movies and mark movies as “Watched”. 
  

- Alternative Flows: 
  - If a movie already exists in the watchlist → system displays “Already in Watchlist”. 


**Use Case 6: Book Movie Tickets**
- Main Flow: 

  - On the booking page, user selects a cinema. 
  
  - A list of movies playing at this cinema is displayed. 
  
  - System shows date and time and the seat map. 
  
  - User selects the cinema, movie, date and time, and seats. 
  
  - User clicks “Book”. 
  
  - Payment done. System displays a booking confirmation. 


- Alternative Flows: 
  - If selected seats are not available → system prompts to select different seats. 

**Use Case 7: Sort Watchlist**
- Main Flow: 

  - User goes to their watchlist
  
  - User selects a filter that they want to use (e.g., ratings, release year, alphabetical order) 
  
  - System displays sorted movies in the watchlist 

- Alternative Flows: 
  - If there are no filters selected, the default watchlist is presented when the user goes to their watchlist, in the order the movies were added 




**MVP**

The Minimum Viable Product for the CineSphere application will focus on delivering a functional movie recommendation and booking experience that allows users to:
- Log in 
- Search for movies 
- Get recommended movies 
- Manage a personal watchlist 
- Make simple ticket reservations in cinemas 

_Use Cases 4, and 7 having a trending movie list and sorting the watchlist
are not part of the MVP but are planned for future development if time allows. 
??_

Proposed Entities 
Format:
Class: ClassName (Relations: Used by: Class2, Class3, etc..  Used in: Class2, Class3, etc…)
Instance_variable1: type
Instance_variable2: type

Class: User (Used by: Reservation, WatchlistItem)
userId: UUID
email: String
passwordHash: String
displayName: String
createdAt: LocalDateTime

Class: Movie (Used by: Showtime, WatchlistItem) 
movieId: UUID
externalId: String
title: String
overview: String
posterUrl: String
genres: List<String>	(could use Set<String> as well for no duplicates)
runtimeMinutes: int
releaseDate: LocalDate
rating: double

Class: Cinema (Used by: Showtime, Seat)
cinemaId: UUID
name: String
address: String
city: String
capacity: int

// Optional (can add as a list instead in Movie class)
Class: Showtime (Used by: Reservation, ReservedSeat	Used in: Movie, Cinema)
showtimeId: UUID
movieId: UUID	(Movie.movieId)
cinemaId: UUID	(Cinema.cinemaId)
startTime: LocalDateTime
availableSeats: int

Class: Reservation (Used by: ReservedSeat	Used in: User, Showtime)
reservationId: UUID
userID: UUID		(User.userId)
showtimeId: UUID	(Showtime.showtimeId)
qty: int
status: boolean     (Might change to String if we add more statuses like: pending, expired, etc..)
createdAt: LocalDateTime

Class: WatchlistItem (Used in: User, Movie)
watchllistItemId: UUID
userId: UUID		(User.userId)
movieId: UUID	(Movie.movieId)
addedAt: LocalDateTime
watched: boolean


Class: Seat (Used by: ReservedSeat	Used in: Cinema)
seatId: UUID
cinemaId: UUID	(Cinema.cinemaId)
rowLabel: String
seatNumber: int
Reserved: boolean
Proposed APIs:
The Movie Database (TMDB) (https://developer.themoviedb.org/reference/getting-started)
Generates a list of movies according to filters, along with their metadata
Status: Successfully tested with OKHttp in Java
Geoapify (https://www.geoapify.com/)
Uses your device’s IP address to determine the approximate geographic location of the device which typically includes the country, region, city, and even the latitude and longitude coordinates of the device.
Status: Successfully tested with OKHttp in Java
MovieGlu (https://developer.movieglu.com/v2/api-index/cinemasnearby/)
Latitude and longitude from the API above is passed to this API to get a list of the cinemas nearby to be used when booking tickets.
Status: Successfully tested with OKHttp in Java

Nov 3 
UI Layout of Landing Page
Nov 7
Functionality of Landing Page

Nov 25
Unit Testing
Nov 28
