package net.darkunscripted.LandLordBot.managers;

import net.darkunscripted.LandLordBot.domain.Building;
import net.darkunscripted.LandLordBot.domain.UserProfile;

import java.nio.file.attribute.UserPrincipal;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    private static ProfileManager pm = new ProfileManager();

    private List<UserProfile> profiles = new ArrayList<>();

    public static ProfileManager getInstance(){
        return pm;
    }

    public UserProfile createProfile(String userID){
        UserProfile profile = new UserProfile(userID, 50000);
        profiles.add(profile);
        DataManager.saveProfile(profile);
        return profile;
    }
    public UserProfile resetProfile(String id){
        for(UserProfile userProfile : profiles){
            if(userProfile.getId().equals(UUID.fromString(id))){
                userProfile.setMoney(50000);
                userProfile.setShares(new HashMap<UUID, Integer>());
                return userProfile;
            }
        }
        return null;
    }

    public List<UserProfile> getProfiles() {
        return profiles;
    }

    public UserProfile getProfile(String userID){
        for(UserProfile profile : profiles){
            if(profile.getUserID().equalsIgnoreCase(userID)){
                return profile;
            }
        }
        return null;
    }

    public boolean hasProfile(String userID){
        for(UserProfile profile: profiles){
            if(profile.getUserID().equalsIgnoreCase(userID)){
                return true;
            }
        }
        return false;
    }

    public void addProfile(UserProfile profile){
        profiles.add(profile);
    }
}
