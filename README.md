# Genius Music Platform
🎵 A Java-based music platform inspired by Genius.com 🎵

## Description
Built with **Java** using **OOP principles** (inheritance, polymorphism, encapsulation).

# 1. Starting the App (Main Menu)
When you open the app, you see:

```
Welcome to our System!

Options:
1. Register new account  
2. Login  
3. View all users  
4. Exit  
5. Admin Login 
```
* **Register** → Make a new account (as a listener or artist).
* **Login** → Sign in if you already have an account.
* **View all users** → See who else is using the app.
* **Admin Login** → Only for the "boss" (admin) who approves artists.

## Registering an Account

If you pick **1. Register**, you fill in:

* **Name** (Your real name)

* **Age** (How old you are)

* **Username** (Your nickname in the app)

* **Email** (Must look like name@email.com)

* **Password** (Must be strong: uppercase, lowercase, number, and symbol)

Then, it asks:
**"Are you an artist? (y/n)"**

* if **(y)**
    * Your request goes to the **admin** for approval.

    * You can’t upload songs until the admin says **"Yes!"**
    
* if **(n)**:

You become a **regular listener** and can start exploring music.


## Logging In
    
If you pick **2. Login**, you enter:

* **Username**

* **Password**

If correct, you go to your **personal menu** (different for listeners and artists).

## Regular User (Listener) Menu
Listeners see:

```
=== Regular User Menu ===  
1. Search content  
2. View followed artists  
3. View profile  
4. View top songs  
5. Comment on a song  
6. View comments  
7. Logout  
```
**What can listeners do?**
* ✅ Search for songs/artists – Find music by name.
* ✅ Follow/unfollow artists – Like subscribing to a singer.
* ✅ Comment on songs – Write thoughts about a song.
* ✅ See top songs – The most popular songs in the app.

## Artist Dashboard
If you’re an **approved artist**, you see:

```
=== Artist Dashboard ===  
Welcome, [Your Name]!  
Followers: 100  
Albums: 2  
Total Songs: 10  
Total Streams: 5000  

Options:  
1. Add Album  
2. Add Single Song  
3. View Profile  
4. Logout  
```
**What can artists do?**
* 🎤 **Add Albums** – Group songs together (like a music book).
* 🎵 **Add Singles** – Upload one song at a time.
* 📊 **See stats** – How many followers, streams, etc.

## Admin Controls (The "Boss")

Only the **admin** can:

* **Approve/reject artist requests** (like a manager deciding who gets to perform).

* **See all users** – Check who’s registered.

**Admin Menu**
```
=== Admin Dashboard ===  
1. View artist requests  
2. View all users  
3. Logout   
```

* If someone wants to be an artist, the admin checks their details and says **"Yes"** or **"No"**.

# UserInfoRepository
* This is a repository class that handles storage/retrieval of user information

* It extends BaseRepository which provides common database operations

* Uses generics:

    * UserInfo - the type of objects it stores

    * String - the type of ID used (username in this case)
    
## Constructor
* When created, it:

    1. Calls parent class (BaseRepository) constructor

    2. Specifies the data will be stored in "userInfo.json" file

    3. Provides UserInfo.class so the repository knows what type of objects to work with

## getId() Method

* Overrides parent class method to specify how to get an object's ID

* For UserInfo objects, the ID is the username

* This helps the repository uniquely identify and locate users

## verifyArtist() Method

* Special method to verify artist accounts

* Steps:

    1. Finds user by username using findById()

    2. If user exists:

        * Sets their verified status to true

        * This likely enables special artist privileges

    3. If user doesn't exist, does nothing
    

## How It Would Be Used
1. When someone registers as an artist

2. Admin calls verifyArtist(username)

3. Repository:

    * Loads user data from JSON

    * Updates verification status

    * Saves back to JSON

4. Now the user has artist privileges

# ArtistRepository

The ArtistRepository class handles the storage and retrieval of artist information using JSON files

1. **Fields**

    * JSON_FILE: Constant defining the JSON file name ("artists_info.json")

    *objectMapper: Jackson ObjectMapper for JSON serialization/deserialization

    * artistsInfoMap: Map storing artist information with usernames as keys

2. **Constructor**

    * Initializes the ObjectMapper with:

        * JavaTimeModule for Java 8 date/time support

        * Pretty-printing (INDENT_OUTPUT)

    * Loads existing artist data from JSON file
    
3. **Core Methods**

    * loadArtistsInfo()

        * Loads artist data from JSON file

        * Returns empty HashMap if file doesn't exist or error occurs

        * Uses Jackson's type factory to properly deserialize the JSON into a Map

    * saveArtistsInfo()

        *Saves current artist data to JSON file

        * Handles IO exceptions gracefully
        
    * getArtistInfo(String username)

        * Retrieves artist info by username

    * getAllArtistsInfo()

        * Returns list of all artist info objects

    * updateArtistInfo(Artist artist)

        * Updates artist info if artist exists in repository
        
    
    * saveArtistInfo (two overloads)

        * Saves artist info from either Artist or ArtistInfo object

        * Validates required fields (name and username)

        * Merges existing data with new data

        * Handles null cases for collections

    * updateFollowersCount(String username, int change)

        * Updates follower count for specified artist

        * Applies delta (positive or negative) to current count

    * updateSongLyrics(String songTitle, String newLyrics)

        * Updates lyrics for a song across all artists

        * Searches both singles and album tracks

        * Saves changes when updates are found
        
# ArtistInfo
Represents detailed information about an artist, including their discography.

1. **Fields**

    * Basic info: username, name, age, email, password

    * Statistics: songsCount, albumsCount, followersCount, totalStreams

    * Discography:

        * singles: List of single titles with genres

        * albums: List of album titles

        * albumDetails: Detailed album information

        * singleDetails: Detailed single information

2. **Constructor**

    * Initializes all collections to empty ArrayLists
    
3. **Core Methods**

    * fromArtist(Artist artist)

        * Factory method to create ArtistInfo from Artist object

        * Copies basic info

        * Converts songs to SongDetails objects

        * Converts albums to AlbumDetails objects

        * Calculates statistics
        
4. **Inner Classes**

    * AlbumDetails

        * Represents an album with:

            * Title

            * Genre

            * List of tracks (as SongDetails)

    * SongDetails

        * Represents a song with:

            * Title

            * Lyrics

            * Genre

            * Release date

            * Comments

5. **Getter/Setter Methods**

    * Standard accessors and mutators for all fields
    
# RegularInfoRepository
 Manages storage and retrieval of regular user information with JSON persistence.
 
1. **Fields**

    * JSON_FILE: Path to "regular_users_info.json" storage file

    * objectMapper: Jackson ObjectMapper for JSON serialization

    * regularInfoMap: In-memory map of username → RegularInfo

2. **Constructor**

    * Initializes ObjectMapper with pretty printing

    * Loads existing data from JSON file

3. **Core Methods**

    * loadRegularInfo()

        * Loads data from JSON file into Map

        * Returns empty HashMap if file doesn't exist

        * Handles deserialization errors gracefully

    * saveRegularInfo()

        * Writes current state to JSON file

        * Error messages printed to stderr

    * getRegularInfo(String username)

        * Retrieves user by username

    * saveRegularInfo(RegularInfo info)

        * Validates input

        * Stores new/updated user info

        * Persists changes

        * Business Logic Methods

    * updateFollowedArtists()

        * Adds/removes artist from user's followed list

        * Creates new RegularInfo if user doesn't exist

    * getAllRegularInfos()

        * Alternative loading method returning List
        

# RegularInfo
Represents a regular user's profile data and activity.

1. **Fields**

    * username: Unique identifier

    * followedArtists: List of artist usernames

    
2. **Constructors**

    * Default: Initializes empty collections

    * Username-based: Sets username + initializes collections
    
    
3. **Core Methods**

    * Artist Following

        * followArtist(): Adds if not already following

        * unfollowArtist(): Removes artist

        * isFollowing(): Checks follow status
        
4. **Getters/Setters**

    * Standard accessors with defensive copying

    * Jackson @JsonProperty for JSON serialization
    

# ArtistRequest
Represents a request to become an artist in the system.

1. **Fields**

    * name: The artist's display name

    * username: Unique identifier for the artist

    * email: Contact email address

    * age: Artist's age (for age verification)

2. **Constructors**

    * @JsonCreator constructor:

        * Used by Jackson for deserialization

        * Each parameter annotated with @JsonProperty

    * Default no-arg constructor:

        * Also needed for Jackson serialization

3. **Getters/Setters**

    * Standard accessor methods for all fields

    * Allows both programmatic creation and JSON population

4. **JSON Support**

    *Annotations enable seamless JSON serialization/deserialization

    * Works with AdminRepository's persistence
    
# AdminRepository
Manages the lifecycle of artist requests with JSON persistence.

1. **Fields**

    * REQUESTS_FILE: Path to "artist_requests.json" storage

    * objectMapper: Jackson ObjectMapper for JSON handling

    * pendingRequests: In-memory list of active requests

2. **Constructor**

    * Initializes ObjectMapper

    * Loads existing requests from JSON file

3. **Core Methods**

    * loadRequests()

        * Loads requests from JSON file

        * Returns empty list if file doesn't exist

        * Handles deserialization errors gracefully

        * Uses Jackson's type factory for proper List<ArtistRequest> typing

    * saveRequests()

        * Writes current requests to JSON file

        * Prints error messages to stderr on failure

        * Business Logic Methods

    * addArtistRequest(ArtistRequest request)

        * Adds new request to pending list

        * Immediately persists changes

    * getPendingRequests()

        * Returns defensive copy of current requests

        * Prevents external modification of internal state

    * removeRequest(String username)

        * Removes request by username

        * Uses removeIf with username matching

        * Persists changes after removal
        
    
## JSON File Structure

```
[
  {
    "name": "John Doe",
    "username": "johndoe",
    "email": "john@example.com",
    "age": 25
  },
  {
    "name": "Jane Smith",
    "username": "janesmith",
    "email": "jane@example.com",
    "age": 30
  }
]
```

# User

The User interface defines a contract for user-related functionality in the system. It specifies two key methods that any implementing class must provide for email validation and password strength checking.

# Accounts
The Accounts class implements the User interface and represents a user account in the system. It handles core account information and provides validation for emails and passwords.

* Stores fundamental account information:

    * name: User's full name

    * age: User's age

    * username: Unique identifier

    * email: Contact email

    * password: Securely stored password

## **validateEmail(String email)** 

* ^[a-zA-Z0-9]: Must start with alphanumeric

* (?!.*\\.\\.): No consecutive dots

* [a-zA-Z0-9._-]*: Allows letters, numbers, dots, underscores, hyphens

* [a-zA-Z0-9]@: Must end with alphanumeric before @

* [a-zA-Z0-9]+: Domain name (letters/numbers)

* ([.-][a-zA-Z0-9]+)*: Optional subdomains

* \\.[a-zA-Z]{2,}$: Top-level domain (2+ letters)

## findValidPasswords(String password)

* At least one uppercase letter (?=.*[A-Z])

* At least one lowercase letter (?=.*[a-z])

* At least one digit (?=.*[0-9])

* At least one special character (?=.*[!@#$%^&*])

* No whitespace (^(?!.*\\s))

* Minimum 8 characters (.{8,}$)

# Admin
This class represents an **Admin** user in a system that manages **accounts, song lyrics, and artist verification**. The Admin class extends the Accounts class, inheriting basic user properties (name, age, username, email, password) while adding administrative functionalities.

1. **Class Definition** 

    * Inherits from Accounts
    
    * Additional Admin-Specific Methods
    
2. **Constructor**

    * super(): Calls the parent (Accounts) constructor to set the basic user details.
    
3. **Core Methods**

    * approveLyricEdit()
    
        Approves a lyric edit request and updates the song lyrics.
        
    * rejectLyricEdit()
    
        Rejects a lyric edit request.
        
    * getUnverifiedArtists()
        
        Fetches all users (userRepo.findAll()).

        Filters for users who are artists (isArtist()) but not verified (!isVerified()).

        Returns the filtered list.
        
    * verifyArtist()
    
        Calls userRepo.verifyArtist() to mark the artist as verified.

        Prints a confirmation message.
        
    
# Regular

This class represents a **Regular User** in a music-related system, extending the Accounts class and adding functionalities like **following artists, viewing lyrics, suggesting edits, searching content, and more.**
    
## Class Structure

1. **Inheritance and Fields**

    * Extends Accounts to inherit basic user properties

    * Contains:

        * regularInfo: User-specific data (following, requests)

        * Collections of available music content
        
2. **Constructor**

    * Initializes user account with parent constructor

    * Creates empty collections for music content

    * Sets up RegularInfo with username
    
3. **Core Methods**

    * Artist Interaction
    
        * Manages artist following relationships

        * Uses RegularInfo for persistence

        * Includes validation and user feedback

    * Content Viewing
    
        * Displays formatted content information

        * Tracks song views via incrementViews()

        * Null-checking for safety
        

# Artist

The Artist class represents an artist account in the music application system, extending the base Accounts class. It provides specialized functionality for music creation, album management, and fan engagement.

1. **Complete Music Management**:

    * Single and album creation

    * Song organization

    * Metadata updates

2. **Data Integrity**:

    * Input validation

    * Duplicate prevention

    * Null safety

3. **Performance Tracking**:

    * Stream counts

    * Follower metrics

4. **Persistence Integration**:

    * Bi-directional conversion with ArtistInfo

    * Complete state restoration
    
    
# Comment

The Comment class represents a user comment on a song in the music application system. It handles comment data storage, formatting, and JSON serialization/deserialization.


# Album

The Album class represents a music album in the system, containing metadata (title, artist, release date, genre) and a list of tracks (Song objects).


# Song

The Song class represents a music track in the system, storing song metadata (title, artist, genre, release date), lyrics, view count, and user comments.
