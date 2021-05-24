package net.darkunscripted.LandLordBot.managers;

import net.darkunscripted.LandLordBot.domain.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildingManager {

    private static BuildingManager bm = new BuildingManager();
    private List<Building> buildings = new ArrayList<Building>();

    public static BuildingManager getInstance(){
        return bm;
    }

    public Building getBuilding(UUID id){
        for(Building building : buildings){
            if(building.getId().equals(id)){
                return building;
            }
        }
        return null;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public void addBuilding(Building building){
        buildings.add(building);
    }

    public Building createBuilding(String name, int shares, Long worth){
        Building building = new Building(UUID.randomUUID(), name, shares, worth);
        buildings.add(building);
        DataManager.saveBuilding(building);
        return building;
    }

    public boolean removeBuilding(String name){
        Building removeBuilding = null;
        for(Building building : buildings){
            if(building.getName().equals(name)){
                removeBuilding = building;
            }
        }
        if(removeBuilding != null){
            buildings.remove(removeBuilding);
            removeBuilding = null;
            return true;
        }
        return false;
    }

    public boolean removeBuilding(UUID id){
        Building removeBuilding = null;
        for(Building building : buildings){
            if(building.getId().equals(id)){
                removeBuilding = building;
            }
        }
        if(removeBuilding != null){
            buildings.remove(removeBuilding);
            removeBuilding = null;
            return true;
        }
        return false;
    }

}
