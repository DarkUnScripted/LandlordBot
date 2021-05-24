package net.darkunscripted.LandLordBot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.darkunscripted.LandLordBot.commands.AccountCommand;
import net.darkunscripted.LandLordBot.commands.BuildingCommand;
import net.darkunscripted.LandLordBot.commands.MoneyCommand;
import net.darkunscripted.LandLordBot.managers.DataManager;
import net.darkunscripted.LandLordBot.runnables.IncomeRunnable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Bot {

    public static String prefix = "!";
    public static List<String> admins = new ArrayList<String>();
    public static String fileDirectory;

    public static void main(String[] args) throws LoginException {

        DataManager.loadSettings();
        DataManager.loadBuildings();
        DataManager.saveBuildings();
        DataManager.loadProfiles();
        DataManager.saveProfiles();

        JDA jda = JDABuilder.createDefault("<token>").build();

        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId("347642857764421664");
        builder.setPrefix(prefix);
        builder.setHelpWord("landlord");
        builder.addCommand(new BuildingCommand(waiter));
        builder.addCommand(new AccountCommand(waiter));
        builder.addCommand(new MoneyCommand(waiter));

        CommandClient client = builder.build();

        jda.addEventListener(client);
        jda.addEventListener(waiter);

        Timer timer = new Timer();
        timer.schedule(new IncomeRunnable(), 900000, 900000);
    }

}
