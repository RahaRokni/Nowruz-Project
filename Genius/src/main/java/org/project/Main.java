package org.project;

import org.project.Accounts.Accounts;
import org.project.Accounts.Artist;
import org.project.Accounts.Regular;
import org.project.Repository.*;
import org.project.SongRelated.Album;
import org.project.SongRelated.Comment;
import org.project.SongRelated.LyricEditRequest;
import org.project.SongRelated.Song;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static RegularInfoRepository regularInfoRepo = new RegularInfoRepository();
    private static UserInfoRepository userRepo = new UserInfoRepository();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to our System!");

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Register new account");
            System.out.println("2. Login");
            System.out.println("3. View all users");
            System.out.println("5. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    loginAccount();
                    break;
                case 3:
                    displayAllUsers();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                case 5:
                    adminLogin();
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void adminLogin() {
        System.out.print("Admin username: ");
        String username = scanner.nextLine();
        System.out.print("Admin password: ");
        String password = scanner.nextLine();

        if ("admin".equals(username) && "admin123".equals(password)) {
            showAdminMenu();
        } else {
            System.out.println("Invalid admin credentials!");
        }
    }


    private static void showAdminMenu() {
    AdminRepository adminRepo = new AdminRepository();
    UserInfoRepository userRepo = new UserInfoRepository();

    while (true) {
        System.out.println("\n=== Admin Dashboard ===");
        System.out.println("1. View artist requests");
        System.out.println("2. View all users");
        System.out.println("3. Logout");
        System.out.print("Select option: ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                manageArtistRequests(adminRepo, userRepo);
                break;
            case 2:
                displayAllUsers();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid option!");
        }
    }
}

    private static void manageArtistRequests(AdminRepository adminRepo, UserInfoRepository userRepo) {
        List<ArtistRequest> requests = adminRepo.getPendingRequests();

        if (requests.isEmpty()) {
            System.out.println("No pending artist requests.");
            return;
        }

        System.out.println("\n=== Pending Artist Requests ===");
        for (int i = 0; i < requests.size(); i++) {
            ArtistRequest req = requests.get(i);
            System.out.println((i+1) + ". " + req.getName() + " (" + req.getUsername() + ")");
        }

        System.out.print("\nSelect request to review (0 to cancel): ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice > 0 && choice <= requests.size()) {
            ArtistRequest request = requests.get(choice - 1);

            System.out.println("\nRequest Details:");
            System.out.println("Name: " + request.getName());
            System.out.println("Username: " + request.getUsername());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Age: " + request.getAge());

            System.out.println("\n1. Approve");
            System.out.println("2. Reject");
            System.out.print("Select action: ");
            int action = Integer.parseInt(scanner.nextLine());

            if (action == 1) {

                UserInfo user = userRepo.findById(request.getUsername());
                if (user != null) {
                    user.setVerified(true);
                    userRepo.save(user);
                    adminRepo.removeRequest(request.getUsername());
                    System.out.println("Artist approved successfully!");
                } else {
                    System.out.println("Error: User not found in database");
                }
            } else if (action == 2) {

                adminRepo.removeRequest(request.getUsername());
                System.out.println("Artist request rejected.");
            }
        }
    }

    private static void registerAccount() {
        System.out.println("\n=== Account Registration ===");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String password;
        Accounts tempAccount = new Accounts(name, age, username, email, "");
        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (tempAccount.findValidPasswords(password) != 0) {
                break;
            }
            System.out.println("Password too weak! Must contain uppercase, lowercase, number, and special character.");
            System.out.println("Please try again.");
        }

        if (!tempAccount.validateEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        if (tempAccount.findValidPasswords(password) == 0) {
            System.out.println("Password too weak! Must contain uppercase, lowercase, number and special character.");
            return;
        }

        System.out.print("Are you an artist? (y/n): ");
        String accountType = scanner.nextLine();

        Accounts newAccount;
        if (accountType.equalsIgnoreCase("y")) {
            newAccount = new Artist(name, age, username, email, password);


            ArtistRequest request = new ArtistRequest(name, username, email, age);
            AdminRepository adminRepo = new AdminRepository();
            adminRepo.addArtistRequest(request);


            UserInfo userInfo = new UserInfo(name, age, username, email, password, "Artist", false);
            userInfo.setVerified(false);  
            userRepo.save(userInfo);

            System.out.println("Your artist account is pending verification by an admin.");
            return;
        }
        else {
            newAccount = new Regular(name, age, username, email, password);
            userRepo.save(new UserInfo(name, age, username, email, password, "Regular", false));
            regularInfoRepo.saveRegularInfo(new RegularInfo(username));
        }


        System.out.println("Registration successful!");
    }

    private static void loginAccount() {
        System.out.println("\n=== Account Login ===");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserInfo user = userRepo.findById(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful! Welcome, " + user.getName());

            Accounts currentUser;
            if (user.getAccountType().equals("Artist")) {
                ArtistRepository artistRepo = new ArtistRepository();
                ArtistInfo info = artistRepo.getArtistInfo(username);
                System.out.println(info);

                currentUser = new Artist(user.getName(), user.getAge(),
                        user.getUsername(), user.getEmail(), user.getPassword());

                if (info != null) {
                    ((Artist) currentUser).setFollowersCount(info.getFollowersCount());
                    ((Artist) currentUser).getAlbumCount();
                    ((Artist) currentUser).loadFromArtistInfo(info);
                }
                showArtistDashboard((Artist) currentUser);
            } else {
                currentUser = new Regular(user.getName(), user.getAge(),
                        user.getUsername(), user.getEmail(), user.getPassword());
                RegularInfo regularInfo = regularInfoRepo.getRegularInfo(user.getUsername());
                if (regularInfo == null) {
                    regularInfo = new RegularInfo(user.getUsername());
                    regularInfoRepo.saveRegularInfo(regularInfo);
                }
                showRegularUserMenu(user, currentUser, regularInfo);
            }
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    private static void showRegularUserMenu(UserInfo user, Accounts currentUser, RegularInfo regularInfo) {
        ArtistRepository artistRepo = new ArtistRepository();
        Regular regularUser = (Regular) currentUser;

        while (true) {
            System.out.println("\n=== Regular User Menu ===");
            System.out.println("1. Search content");
            System.out.println("2. View followed artists (" + regularInfo.getFollowedArtists().size() + ")");
            System.out.println("3. View profile");
            System.out.println("4. View top songs");
            System.out.println("5. Comment on a song");
            System.out.println("6. view comments");
            System.out.println("7. Logout");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    searchUserByUsername(user, regularInfo);
                    break;
                case 2:
                    viewFollowedArtists(regularInfo, artistRepo);
                    break;
                case 3:
                    viewUserProfile(user, regularInfo);
                    break;
                case 4:
                    System.out.print("Enter number of top songs to display: ");
                    int count = Integer.parseInt(scanner.nextLine());
                    regularUser.viewTopSongs(count);
                    break;
                case 5:
                    System.out.print("Enter song title to comment on: ");
                    String songTitle = scanner.nextLine();
                    Song songToComment = findSongByTitle(songTitle);
                    if (songToComment != null) {
                        System.out.print("Enter your comment: ");
                        String comment = scanner.nextLine();
                        regularUser.commentOnSong(songToComment, comment);

                        UserInfo artistUser = userRepo.findById(songToComment.getArtist());
                        if (artistUser != null) {
                            Artist artist = new Artist(
                                    artistUser.getName(),
                                    artistUser.getAge(),
                                    artistUser.getUsername(),
                                    artistUser.getEmail(),
                                    artistUser.getPassword()
                            );

                            ArtistInfo artistInfo = artistRepo.getArtistInfo(artistUser.getUsername());
                            if (artistInfo != null) {
                                artist.loadFromArtistInfo(artistInfo);

                                boolean commentAdded = false;

                                for (Song single : artist.getSingles()) {
                                    if (single.getTitle().equals(songToComment.getTitle())) {
                                        single.addComment(regularUser.getUsername(), comment);
                                        commentAdded = true;
                                        break;
                                    }
                                }

                                if (!commentAdded) {
                                    for (Album album : artist.getAlbums()) {
                                        for (Song track : album.getTrackList()) {
                                            if (track.getTitle().equals(songToComment.getTitle())) {
                                                track.addComment(regularUser.getUsername(), comment);
                                                commentAdded = true;
                                                break;
                                            }
                                        }
                                        if (commentAdded) break;
                                    }
                                }

                                artistRepo.saveArtistInfo(artist);
                                System.out.println("Comment added and saved successfully!");
                            }
                        }
                    } else {
                        System.out.println("Song not found!");
                    }
                    break;
                case 6:
                    System.out.print("Enter song title to view comments: ");
                    songTitle = scanner.nextLine();
                    Song songToView = findSongByTitle(songTitle);
                    if (songToView != null) {
                        songToView.displayComments();
                    } else {
                        System.out.println("Song not found!");
                    }
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static Song findSongByTitle(String title) {
        ArtistRepository artistRepo = new ArtistRepository();
        List<UserInfo> allUsers = userRepo.findAll();

        for (UserInfo user : allUsers) {
            if (user.isArtist()) {
                ArtistInfo artistInfo = artistRepo.getArtistInfo(user.getUsername());
                if (artistInfo != null) {

                    if (artistInfo.getSingleDetails() != null) {
                        for (ArtistInfo.SongDetails single : artistInfo.getSingleDetails()) {
                            if (single.getTitle().equalsIgnoreCase(title)) {
                                Song song = new Song(
                                        single.getTitle(),
                                        artistInfo.getName(),
                                        single.getLyrics(),
                                        single.getGenre(),
                                        single.getReleaseDate()
                                );

                                if (single.getComments() != null) {
                                    for (Comment comment : single.getComments()) {
                                        song.addComment(comment.getUsername(), comment.getCommentText());
                                    }
                                }
                                return song;
                            }
                        }
                    }


                    if (artistInfo.getAlbumDetails() != null) {
                        for (ArtistInfo.AlbumDetails album : artistInfo.getAlbumDetails()) {
                            if (album.getTracks() != null) {
                                for (ArtistInfo.SongDetails track : album.getTracks()) {
                                    if (track.getTitle().equalsIgnoreCase(title)) {
                                        Song song = new Song(
                                                track.getTitle(),
                                                artistInfo.getName(),
                                                track.getLyrics(),
                                                track.getGenre(),
                                                track.getReleaseDate()
                                        );
                                        if (track.getComments() != null) {
                                            for (Comment comment : track.getComments()) {
                                                song.addComment(comment.getUsername(), comment.getCommentText());
                                            }
                                        }
                                        return song;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void showArtistDashboard(Artist artist) {
        ArtistRepository artistRepo = new ArtistRepository();
        UserInfo user = userRepo.findById(artist.getUsername());
        if (user == null || !user.isVerified()) {
            System.out.println("Your artist account is still pending verification.");
            System.out.println("Please wait for admin approval.");
            return;
        }
        System.out.println("\n=== Artist Dashboard ===");
        System.out.println("Welcome, " + artist.getName() + "!");

        ArtistInfo info = artistRepo.getArtistInfo(artist.getUsername());

        if (info != null) {
            System.out.println("Followers: " + info.getFollowersCount());
            System.out.println("Albums: " + info.getAlbumsCount());
            System.out.println("Total Songs: " + info.getSongsCount());
            System.out.println("Total Streams: " + info.getTotalStreams());
        }

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Add Album");
            System.out.println("2. Add Single Song");
            System.out.println("3. View Profile");
            System.out.println("4. Logout");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter album name: ");
                    String albumName = scanner.nextLine();
                    System.out.print("Enter album genre: ");
                    String albumGenre = scanner.nextLine();
                    System.out.print("Enter release date: ");
                    String releaseDate = scanner.nextLine();
                    Album newAlbum = artist.createAlbum(albumName, releaseDate, albumGenre);
                    if (newAlbum != null) {
                        System.out.println("Album added successfully!");
                        System.out.println("Would you like to add tracks to this album now? (y/n)");
                        String addTracks = scanner.nextLine();
                        if (addTracks.equalsIgnoreCase("y")) {
                            while (true) {
                                System.out.print("Enter song title (or 'done' to finish): ");
                                String songTitle = scanner.nextLine();
                                if (songTitle.equalsIgnoreCase("done")) {
                                    break;
                                }

                                System.out.print("Enter song lyrics (press enter twice to finish): ");
                                StringBuilder lyrics = new StringBuilder();
                                String line;
                                while (!(line = scanner.nextLine()).isEmpty()) {
                                    lyrics.append(line).append("\n");
                                }
                                System.out.print("Enter song genre: ");
                                String songGenre = scanner.nextLine();
                                System.out.print("Enter release date: ");
                                String songDate = scanner.nextLine();

                                Song newSong = new Song(songTitle, artist.getName(), lyrics.toString(), songGenre, songDate);
                                artist.addSongToAlbum(albumName, newSong);
                                System.out.println("Song added to album!");
                            }
                        }
                        artistRepo.saveArtistInfo(artist);
                    }
                    break;
                case 2:
                    System.out.print("Enter song name: ");
                    String songName = scanner.nextLine();
                    System.out.print("Enter song lyrics (press enter twice to finish): ");
                    StringBuilder lyrics = new StringBuilder();
                    String line;
                    while (!(line = scanner.nextLine()).isEmpty()) {
                        lyrics.append(line).append("\n");
                    }
                    System.out.print("Enter song genre: ");
                    String songGenre = scanner.nextLine();
                    System.out.print("Enter release date: ");
                    String songDate = scanner.nextLine();
                    artist.createSingle(songName, lyrics.toString(), songGenre, songDate);
                    System.out.println("Song added successfully!");
                    artistRepo.saveArtistInfo(artist);
                    break;
                case 3:
                    displayArtistProfile(artistRepo, artist.getUsername());
                    System.out.println("wedrft   :   " + artist.getAlbumCount());
                    break;
                case 4:
                    artistRepo.saveArtistInfo(artist);
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void displayArtistProfile(ArtistRepository artistRepo, String username) {
        ArtistInfo info = artistRepo.getArtistInfo(username);
        System.out.println(info);
        if (info != null) {
            System.out.println("\n=== Artist Profile ===");
            System.out.println("Name: " + info.getName());
            System.out.println("Username: " + info.getUsername());
            System.out.println("Followers: " + info.getFollowersCount());
            System.out.println("Albums: " + info.getAlbumsCount());
            System.out.println("Total Songs: " + info.getSongsCount());
            System.out.println("Total Streams: " + info.getTotalStreams());

            if (info.getSingleDetails() != null && !info.getSingleDetails().isEmpty()) {
                System.out.println("\nSingles:");
                for (int i = 0; i < info.getSingleDetails().size(); i++) {
                    System.out.println((i + 1) + ". " + info.getSingleDetails().get(i).getTitle() +
                            " (" + info.getSingleDetails().get(i).getGenre() + ")");
                }

                System.out.print("\nEnter single number to view details (0 to go back): ");
                int singleChoice = Integer.parseInt(scanner.nextLine());
                if (singleChoice > 0 && singleChoice <= info.getSingleDetails().size()) {
                    viewSingleDetails(info.getSingleDetails().get(singleChoice - 1));
                }
            }

            if (info.getAlbumDetails() != null && !info.getAlbumDetails().isEmpty()) {
                System.out.println("\nAlbums:");
                for (int i = 0; i < info.getAlbumDetails().size(); i++) {
                    System.out.println((i + 1) + ". " + info.getAlbumDetails().get(i).getTitle() +
                            " (" + info.getAlbumDetails().get(i).getGenre() + ")");
                }

                System.out.print("\nEnter album number to view tracks (0 to go back): ");
                int albumChoice = Integer.parseInt(scanner.nextLine());
                if (albumChoice > 0 && albumChoice <= info.getAlbumDetails().size()) {
                    viewAlbumTracks(info.getAlbumDetails().get(albumChoice - 1));
                }
            }
        } else {
            System.out.println("No profile information found for this artist.");
        }

    }

    private static void viewSingleDetails(ArtistInfo.SongDetails single) {
        System.out.println("\n=== " + single.getTitle() + " Details ===");
        System.out.println("Genre: " + single.getGenre());
        System.out.println("Release Date: " + single.getReleaseDate());
        System.out.println("\nLyrics:");
        System.out.println(single.getLyrics());

        Song song = new Song(
                single.getTitle(),
                "Artist Name",
                single.getLyrics(),
                single.getGenre(),
                single.getReleaseDate()
        );
        song.displayComments();


        System.out.println("\nPress enter to go back...");
        scanner.nextLine();
    }

    private static void viewAlbumTracks(ArtistInfo.AlbumDetails album) {
        System.out.println("\n=== " + album.getTitle() + " Tracks ===");
        for (int i = 0; i < album.getTracks().size(); i++) {
            System.out.println((i + 1) + ". " + album.getTracks().get(i).getTitle() +
                    " (" + album.getTracks().get(i).getGenre() + ")");
        }

        System.out.print("\nEnter track number to view lyrics (0 to go back): ");
        int trackChoice = Integer.parseInt(scanner.nextLine());
        if (trackChoice > 0 && trackChoice <= album.getTracks().size()) {
            viewSongLyrics(album.getTracks().get(trackChoice - 1));
        }
    }



    private static void viewSongLyrics(ArtistInfo.SongDetails song) {
        System.out.println("\n=== " + song.getTitle() + " Lyrics ===");
        System.out.println(song.getLyrics());


        System.out.println("\nPress enter to go back...");
        scanner.nextLine();
    }

    private static void displayAllUsers() {
        System.out.println("\n=== All Registered Users ===");
        List<UserInfo> users = userRepo.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (UserInfo user : users) {
                System.out.println(user.toString());
            }
        }
    }

    private static void viewFollowedArtists(RegularInfo regularInfo, ArtistRepository artistRepo) {
        System.out.println("\n=== Followed Artists ===");
        if (regularInfo.getFollowedArtists().isEmpty()) {
            System.out.println("You're not following any artists yet.");
            return;
        }

        for (String artistUsername : regularInfo.getFollowedArtists()) {
            ArtistInfo info = artistRepo.getArtistInfo(artistUsername);
            if (info != null) {
                System.out.println("- " + info.getName() + " (" + info.getFollowersCount() + " followers)");
                System.out.println("  1. View profile");
                System.out.println("  2. Unfollow");
                System.out.print("  Select option (0 to skip): ");
                int option = Integer.parseInt(scanner.nextLine());

                if (option == 1) {
                    displayArtistProfile(artistRepo, artistUsername);
                } else if (option == 2) {

                    regularInfoRepo.updateFollowedArtists(regularInfo.getUsername(), artistUsername, false);


                    info.setFollowersCount(info.getFollowersCount() - 1);

                    Artist artist = new Artist(
                            info.getName(),
                            info.getAge(),
                            info.getUsername(),
                            info.getEmail(),
                            info.getPassword()
                    );
                    artist.loadFromArtistInfo(info);
                    artist.setFollowersCount(info.getFollowersCount());
                    artistRepo.saveArtistInfo(artist);

                    System.out.println("You've unfollowed " + info.getName());
                }
            }
        }
    }

    private static void suggestLyricEdit(ArtistInfo.SongDetails song, String username) {
        System.out.println("\nCurrent lyrics:");
        System.out.println(song.getLyrics());

        System.out.println("Enter your suggested lyrics (press enter twice to finish):");
        StringBuilder newLyrics = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            newLyrics.append(line).append("\n");
        }

        LyricEditRequest request = new LyricEditRequest(
                song.getTitle(),
                song.getLyrics(),
                newLyrics.toString(),
                username
        );

        regularInfoRepo.addLyricEditRequest(username, request);
        System.out.println("Lyric edit suggestion submitted!");
    }

    private static void viewUserProfile(UserInfo user, RegularInfo regularInfo) {
        System.out.println("\n=== Your Profile ===");
        System.out.println("Name: " + user.getName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Following: " + regularInfo.getFollowedArtists().size() + " artists");

        if (!regularInfo.getLyricEditRequests().isEmpty()) {
            System.out.println("\nYour pending lyric edit requests:");
            for (LyricEditRequest request : regularInfo.getLyricEditRequests()) {
                System.out.println("- " + request.getSongTitle());
            }
        }
    }
    private static void searchUserByUsername(UserInfo currentUser, RegularInfo regularInfo) {
        System.out.print("\nEnter username to search: ");
        String searchUsername = scanner.nextLine();

        if (searchUsername.equals(currentUser.getUsername())) {
            System.out.println("You cannot search your own profile here. Use 'View profile' instead.");
            return;
        }

        UserInfo foundUser = userRepo.findById(searchUsername);
        if (foundUser == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("\n=== User Profile ===");
        System.out.println("Name: " + foundUser.getName());
        System.out.println("Username: " + foundUser.getUsername());
        System.out.println("Account Type: " + foundUser.getAccountType());

        if (foundUser.isArtist()) {
            ArtistRepository artistRepo = new ArtistRepository();
            ArtistInfo artistInfo = artistRepo.getArtistInfo(foundUser.getUsername());

            if (artistInfo != null) {
                System.out.println("Followers: " + artistInfo.getFollowersCount());
                System.out.println("Albums: " + artistInfo.getAlbumsCount());
                System.out.println("Songs: " + artistInfo.getSongsCount());

                boolean isFollowing = regularInfo.isFollowing(foundUser.getUsername());

                if (isFollowing) {
                    System.out.println("\nYou are following this artist");
                    System.out.println("1. Unfollow");
                } else {
                    System.out.println("\n1. Follow this artist");
                }
                System.out.println("2. View artist details");
                System.out.println("0. Back");
                System.out.print("Select option: ");

                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        boolean follow = !regularInfo.isFollowing(foundUser.getUsername());
                        regularInfoRepo.updateFollowedArtists(
                                currentUser.getUsername(),
                                foundUser.getUsername(),
                                follow
                        );
                        System.out.println(follow ? "You are now following " + foundUser.getName()
                                : "You have unfollowed " + foundUser.getName());


                        artistInfo.setFollowersCount(follow ?
                                artistInfo.getFollowersCount() + 1 :
                                artistInfo.getFollowersCount() - 1);

                        System.out.println(artistInfo.getFollowersCount());

                        Artist artist = new Artist(
                                artistInfo.getName(),
                                artistInfo.getAge(),
                                artistInfo.getUsername(),
                                artistInfo.getEmail(),
                                artistInfo.getPassword()
                        );
                        artist.loadFromArtistInfo(artistInfo);
                        artist.setFollowersCount(artistInfo.getFollowersCount());
                        artistRepo.saveArtistInfo(artist);
                        break;
                    case 2:
                        displayArtistProfile(artistRepo, foundUser.getUsername());
                        break;
                }
            }
        } else {
            System.out.println("\n(Regular user profile)");
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        }
    }
}

