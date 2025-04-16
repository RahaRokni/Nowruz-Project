package org.project.Accounts;

import org.project.Repository.UserInfo;
import org.project.SongRelated.LyricEditRequest;
import org.project.Repository.ArtistRepository;
import org.project.Repository.RegularInfoRepository;
import org.project.Repository.UserInfoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class Admin extends Accounts {

    public Admin(String name, int age, String username, String email, String password) {
        super(name, age, username, email, password);
    }

    public List<LyricEditRequest> getPendingLyricEdits(RegularInfoRepository regularInfoRepo) {
        return regularInfoRepo.getAllPendingLyricEdits();
    }

    public void approveLyricEdit(LyricEditRequest request, RegularInfoRepository regularInfoRepo,
                                 ArtistRepository artistRepo, UserInfoRepository userRepo) {

        UserInfo artistUser = userRepo.findById(request.getSongArtist());
        if (artistUser != null && artistUser.isArtist()) {

            artistRepo.updateSongLyrics(request.getSongTitle(), request.getSuggestedLyrics());


            regularInfoRepo.removeLyricEditRequest(request.getRequesterUsername(), request);

            System.out.println("Lyric edit approved and updated!");
        } else {
            System.out.println("Could not find the artist for this song.");
        }
    }

    public void rejectLyricEdit(LyricEditRequest request, RegularInfoRepository regularInfoRepo) {
        regularInfoRepo.removeLyricEditRequest(request.getRequesterUsername(), request);
        System.out.println("Lyric edit request rejected.");
    }

    public List<UserInfo> getUnverifiedArtists(UserInfoRepository userRepo) {
        return userRepo.findAll().stream()
                .filter(user -> user.isArtist() && !user.isVerified())
                .collect(Collectors.toList());
    }

    public void verifyArtist(UserInfo artist, UserInfoRepository userRepo) {
        userRepo.verifyArtist(artist.getUsername());
        System.out.println("Artist " + artist.getName() + " has been verified!");
    }
}