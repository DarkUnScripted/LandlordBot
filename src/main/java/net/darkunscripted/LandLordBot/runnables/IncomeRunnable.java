package net.darkunscripted.LandLordBot.runnables;

import net.darkunscripted.LandLordBot.domain.Building;
import net.darkunscripted.LandLordBot.domain.UserProfile;
import net.darkunscripted.LandLordBot.managers.BuildingManager;
import net.darkunscripted.LandLordBot.managers.DataManager;
import net.darkunscripted.LandLordBot.managers.ProfileManager;
import net.dv8tion.jda.api.entities.GuildChannel;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.UUID;

public class IncomeRunnable extends TimerTask {

    @Override
    public void run() {
        for(UserProfile profile : ProfileManager.getInstance().getProfiles()){
            for(UUID buildingUUID : new ArrayList<>(profile.getShares().keySet())){
                Building building = BuildingManager.getInstance().getBuilding(buildingUUID);
                long moneyPerShare = (long) (building.getWorth() / building.getShares());
                profile.setMoney(profile.getMoney() + (long) ((moneyPerShare * profile.getShares().get(buildingUUID)) / 2));
            }
            DataManager.saveProfile(profile);
        }
        System.out.println("Gave everyone their payout");
    }
}
