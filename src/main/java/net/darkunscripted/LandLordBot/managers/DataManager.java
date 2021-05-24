package net.darkunscripted.LandLordBot.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.darkunscripted.LandLordBot.Bot;
import net.darkunscripted.LandLordBot.domain.Building;
import net.darkunscripted.LandLordBot.domain.Settings;
import net.darkunscripted.LandLordBot.domain.UserProfile;
import okhttp3.OkHttpClient.Builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataManager {

    public static void loadSettings(){
        try {
            ClassLoader cL = Thread.currentThread().getContextClassLoader();
            File file = new File("./Settings/settings.yml");
            ObjectMapper objM = new ObjectMapper(new YAMLFactory());
            Settings settings = objM.readValue(file, Settings.class);
            Bot.prefix = settings.getPrefix();
            for (String admin : settings.getAdmins()) {
                Bot.admins.add(admin);
            }
            Bot.fileDirectory = settings.getFileDirectory();
            System.out.println("Prefix settings " + settings.getPrefix());
        }catch (IOException e){
            System.out.println("Could not find file!");
        }
    }

    public static void loadBuildings(){
        try{
            File dir = new File("./Buildings/");
            File[] directoryListing = dir.listFiles();
            System.out.println(directoryListing.length);
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    ObjectMapper om = new ObjectMapper(new YAMLFactory());
                    Building building = om.readValue(child, Building.class);
                    BuildingManager.getInstance().addBuilding(building);
                    System.out.println("Building info:\n- ID: " + building.getId() + ",\n- Name: " + building.getName() + ",\n- Shares: " + building.getShares() + ",\n- Worth: " + building.getWorth());
                }
            }
        }catch (IOException e){
            System.out.println("Could not find file!");
        }
    }

    public static void saveBuildings(){
        try {
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            for (Building building : BuildingManager.getInstance().getBuildings()) {
                om.writeValue(new File("./Buildings/" + building.getName() + ".yml"), building);
            }
        }catch (IOException e){
            System.out.println("Error with saving to file!" + e);
            e.printStackTrace();
        }
    }

    public static void saveBuilding(Building building){
        try {
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            om.writeValue(new File( "./Buildings/" + building.getName() + ".yml"), building);
        }catch (IOException e){
            System.out.println("Error with saving to file!" + e);
            e.printStackTrace();
        }
    }

    public static void loadProfiles(){
        try{
            File dir = new File("./Profiles/");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    ObjectMapper om = new ObjectMapper(new YAMLFactory());
                    UserProfile profile = om.readValue(child, UserProfile.class);
                    ProfileManager.getInstance().addProfile(profile);
                    System.out.println("Profile info:  userID: " + profile.getUserID());
                    System.out.println("Shares:");
                    for(UUID buildingUUID : new ArrayList<UUID>(profile.getShares().keySet())){
                        Building building = BuildingManager.getInstance().getBuilding(buildingUUID);
                        System.out.println("building: " + building.getName());
                        System.out.println("amount: " + profile.getShares().get(buildingUUID));
                    }
                }
            }else{
                System.out.println("There are no Profile files!");
            }
        }catch (IOException e){
            System.out.println("Could not find Profiles file!");
        }
    }

    public static void saveProfiles(){
        try {
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            for (UserProfile profile : ProfileManager.getInstance().getProfiles()) {
                om.writeValue(new File("./Profiles/" + profile.getId() + ".yml"), profile);
            }
        }catch (IOException e){
            System.out.println("Error with saving to file!");
        }
    }

    public static void saveProfile(UserProfile profile) {
        try {
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            om.writeValue(new File("./Profiles/" + profile.getId() + ".yml"), profile);
        } catch (IOException e) {
            System.out.println("Error with saving to file!");
        }
    }
}
