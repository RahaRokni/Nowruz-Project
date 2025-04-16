package org.project.Repository;

public class UserInfoRepository extends BaseRepository <UserInfo, String> {
    public UserInfoRepository() {
        super("userInfo.json",UserInfo.class);
    }


    @Override
    protected String getId(UserInfo entity) {
        return entity.getUsername();
    }

    public void verifyArtist(String username) {
        UserInfo user = findById(username);
        if (user != null) {
            user.setVerified(true);
        }
    }
}
