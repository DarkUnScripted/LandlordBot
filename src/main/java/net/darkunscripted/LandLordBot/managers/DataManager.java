package net.darkunscripted.LandLordBot.managers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.darkunscripted.LandLordBot.Bot;
import net.darkunscripted.LandLordBot.domain.Building;
import net.darkunscripted.LandLordBot.domain.UserProfile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class DataManager {

    public static void loadSettings(){
        try (FileReader reader = new FileReader("D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Settings/config.json")){
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONObject settingsObj = (JSONObject) obj;
            JSONArray adminArray = (JSONArray) settingsObj.get("admins");
            adminArray.forEach(admin -> {
                Bot.admins.add((String) admin);
                System.out.println((String) admin);
            });
            Bot.prefix = settingsObj.get("prefix").toString();
            Bot.token = settingsObj.get("token").toString();
            System.out.println(Bot.prefix);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public static void loadBuildings(){
        try{
            File dir = new File("D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Buildings/");
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
            ObjectMapper om = new ObjectMapper(new JsonFactory());
            om.enable(SerializationFeature.INDENT_OUTPUT);
            for (Building building : BuildingManager.getInstance().getBuildings()) {
                JSONObject job = building.toJson();
                System.out.println(job);
                om.writeValue(new File("D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Buildings/" + building.getName() + ".json"), job);
            }
        }catch (IOException e){
            System.out.println("Error with saving to file!" + e);
            e.printStackTrace();
        }
    }

    public static void saveBuilding(Building building){
        try {
            ObjectMapper om = new ObjectMapper(new JsonFactory());
            om.enable(SerializationFeature.INDENT_OUTPUT);
            om.writeValue(new File( "D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Buildings/" + building.getName() + ".json"), building.toJson());
        }catch (IOException e){
            System.out.println("Error with saving to file!" + e);
            e.printStackTrace();
        }
    }

    public static void deleteBuilding(Building building){
        File file = new File("D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Buildings/" + building.getName() + ".json");
        if(file.exists()){
            file.delete();
        }
    }

    public static void loadProfiles(){
        try{
            File dir = new File("D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Profiles/");
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
            ObjectMapper om = new ObjectMapper(new JsonFactory());
            om.enable(SerializationFeature.INDENT_OUTPUT);
            for (UserProfile profile : ProfileManager.getInstance().getProfiles()) {
                om.writeValue(new File("D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Profiles/" + profile.getId() + ".json"), profile.toJson());
            }
        }catch (IOException | ParseException e){
            System.out.println("Error with saving to file!");
            e.printStackTrace();
        }
    }

    public static void saveProfile(UserProfile profile) {
        try {
            ObjectMapper om = new ObjectMapper(new JsonFactory());
            om.enable(SerializationFeature.INDENT_OUTPUT);
            om.writeValue(new File("D:/Discord Bot/DarkDevelopment/LandlordBot/src/main/resources/Profiles/" + profile.getId() + ".json"), profile.toJson());
        } catch (IOException | ParseException e) {
            System.out.println("Error with saving to file!");
            e.printStackTrace();
        }
    }
}
