package net.darkunscripted.LandLordBot.domain;

import java.util.UUID;

public class Building {

    private UUID id;
    private String name;
    private int shares;
    private Long worth;
    private int purchasedShares;

    public Building(UUID id, String name, int shares, Long worth){
        this.id = id;
        this.name = name;
        this.shares = shares;
        this.worth = worth;
        this.purchasedShares = 0;
    }

    public Building(UUID id, String name, int shares, Long worth, int purchasedShares){
        this.id = id;
        this.name = name;
        this.shares = shares;
        this.worth = worth;
        this.purchasedShares = purchasedShares;
    }

    public Building(){

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getShares() {
        return shares;
    }

    public Long getWorth() {
        return worth;
    }

    public int getPurchasedShares() {
        return purchasedShares;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public void setWorth(Long worth) {
        this.worth = worth;
    }

    public void setPurchasedShares(int purchasedShares) {
        this.purchasedShares = purchasedShares;
    }
}
