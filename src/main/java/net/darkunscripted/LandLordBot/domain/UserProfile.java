package net.darkunscripted.LandLordBot.domain;

import java.util.HashMap;
import java.util.UUID;

public class UserProfile {

    private UUID id;
    private String userID;
    private long money;
    private long worth;
    private HashMap<UUID, Integer> shares = new HashMap<UUID, Integer>();

    public UserProfile(){

    }

    public UserProfile(String userID, long money){
        this.id = UUID.randomUUID();
        this.userID = userID;
        this.money = money;
    }

    public UserProfile(UUID id, String userID, long money, HashMap<UUID, Integer> shares){
        this.id = id;
        this.userID = userID;
        this.money = money;
        this.shares = shares;
    }

    public UUID getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public long getMoney() {
        return money;
    }

    public HashMap<UUID, Integer> getShares() {
        return shares;
    }

    public long getWorth() {
        return worth;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void setShares(HashMap<UUID, Integer> shares) {
        this.shares = shares;
    }

    public void setWorth(long worth) {
        this.worth = worth;
    }

    public void addShare(Building building, Integer amount){
        shares.put(building.getId(), amount);
    }

    public void removeShare(Building building){
        shares.remove(building);
    }
}
