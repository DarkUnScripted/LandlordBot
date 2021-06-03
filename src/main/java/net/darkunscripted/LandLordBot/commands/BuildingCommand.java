package net.darkunscripted.LandLordBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.darkunscripted.LandLordBot.Bot;
import net.darkunscripted.LandLordBot.domain.Building;
import net.darkunscripted.LandLordBot.domain.UserProfile;
import net.darkunscripted.LandLordBot.managers.BuildingManager;
import net.darkunscripted.LandLordBot.managers.DataManager;
import net.darkunscripted.LandLordBot.managers.EmbedManager;
import net.darkunscripted.LandLordBot.managers.ProfileManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.requests.Route;

import java.awt.*;
import java.sql.DatabaseMetaData;
import java.util.concurrent.TimeUnit;

public class BuildingCommand extends Command {

    private EventWaiter waiter;

    public BuildingCommand(EventWaiter waiter) {
        this.name = "building";
        this.aliases = new String[]{"buildings"};
        this.waiter = waiter;
        this.help = "`" + Bot.prefix + "building help`";
    }
    protected void execute(CommandEvent e) {
        if (e.getArgs().length() == 0) {
            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Commands**");
            embed.addField("**Help Command**", "*" + Bot.prefix + "building help*", false);
            embed.addField("**Show Command**", "*" + Bot.prefix + "building show <name>*", false);
            embed.addField("**Create Command**", "*" + Bot.prefix + "building create*", false);
            embed.addField("**Buy Command**", "*" + Bot.prefix + "building buy*", false);
            e.getMessage().getChannel().sendMessage(embed.build()).queue();
        } else {
            String[] args = e.getArgs().split(" ");
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Commands**");
                embed.addField("**Help Command**", "*" + Bot.prefix + "building help*", false);
                embed.addField("**Show Command**", "*" + Bot.prefix + "building show <name>*", false);
                embed.addField("**Create Command**", "*" + Bot.prefix + "building create*", false);
                embed.addField("**Buy Command**", "*" + Bot.prefix + "building buy*", false);
                e.getMessage().getChannel().sendMessage(embed.build()).queue();
            } else if (args.length == 1 && args[0].equalsIgnoreCase("show")) {
                if(BuildingManager.getInstance().getBuildings().size() > 0) {
                    for (Building building : BuildingManager.getInstance().getBuildings()) {
                        EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**" + building.getName() + "**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Building info:**");
                        embed.addField("id", "" + building.getId(), false);
                        embed.addField("shares", "" + building.getShares(), false);
                        embed.addField("worth", "" + building.getWorth(), false);
                        e.getChannel().sendMessage(embed.build()).queue();
                    }
                }else{
                    EmbedBuilder cancelEmbed = EmbedManager.getInstance().createEmbed("**Building Show**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "There are no buildings yet!");
                    e.getChannel().sendMessage(cancelEmbed.build()).queue();
                }
            } else if (args[0].equalsIgnoreCase("show")) {
                if(BuildingManager.getInstance().getBuildings().size() > 0) {
                    String name = "";
                    for (int i = 1; i < args.length; i++) {
                        name = name + args[i] + " ";
                    }
                    for (Building building : BuildingManager.getInstance().getBuildings()) {
                        if (building.getName().equalsIgnoreCase(name.strip())) {
                            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**" + building.getName() + "**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Building info:**");
                            embed.addField("id", "" + building.getId(), false);
                            embed.addField("shares", "" + building.getShares(), false);
                            embed.addField("worth", "" + building.getWorth(), false);
                            e.getChannel().sendMessage(embed.build()).queue();
                        }
                    }
                }else{
                    EmbedBuilder cancelEmbed = EmbedManager.getInstance().createEmbed("**Building Show**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "There are no buildings yet!");
                    e.getChannel().sendMessage(cancelEmbed.build()).queue();
                }
            }else if(args.length == 1 && args[0].equalsIgnoreCase("create")){

                if(Bot.admins.contains(e.getMessage().getAuthor().getId())){
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                    embed.addField("**What should I name the building <@" + e.getMessage().getAuthor().getName() + ">?**", "You have 15 seconds to answer", false);
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                    waiter.waitForEvent(GuildMessageReceivedEvent.class, nameEvent -> nameEvent.getAuthor().equals(e.getAuthor()) && nameEvent.getChannel().equals(e.getChannel()), nameEvent -> {
                        if(nameEvent.getMessage().getContentRaw().equalsIgnoreCase("cancel")){
                            EmbedBuilder cancelEmbed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "Building creation cancelled!");
                            nameEvent.getMessage().getChannel().sendMessage(cancelEmbed.build()).queue();
                        }else{
                            String name = nameEvent.getMessage().getContentRaw();
                            EmbedBuilder sharesEmbed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                            sharesEmbed.addField("**How many shares does this building have <@" + e.getMessage().getAuthor().getName() + ">?**", "You have 15 seconds to answer", false);
                            e.getMessage().getChannel().sendMessage(sharesEmbed.build()).queue();
                            waiter.waitForEvent(GuildMessageReceivedEvent.class, sharesEvent -> sharesEvent.getAuthor().equals(e.getAuthor()) && sharesEvent.getChannel().equals(e.getChannel()), sharesEvent -> {
                                if(sharesEvent.getMessage().getContentRaw().equalsIgnoreCase("cancel")){
                                    EmbedBuilder cancelEmbed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "Building creation cancelled!");
                                    nameEvent.getMessage().getChannel().sendMessage(cancelEmbed.build()).queue();
                                }else{
                                    try{
                                        int shares = Integer.parseInt(sharesEvent.getMessage().getContentRaw());
                                        EmbedBuilder worthEmbed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                                        worthEmbed.addField("**How much is this building worth <@" + e.getMessage().getAuthor().getName() +">?**", "You have 15 seconds to answer", false);
                                        sharesEvent.getMessage().getChannel().sendMessage(worthEmbed.build()).queue();
                                        waiter.waitForEvent(GuildMessageReceivedEvent.class, worthEvent -> worthEvent.getAuthor().equals(sharesEvent.getAuthor()) && worthEvent.getChannel().equals(sharesEvent.getChannel()), worthEvent -> {
                                            try{
                                                Long worth = Long.parseLong(worthEvent.getMessage().getContentRaw());
                                                BuildingManager.getInstance().createBuilding(name, shares, worth);
                                                EmbedBuilder createdEmbed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Building succesfully created <@" + e.getMessage().getAuthor().getId() + ">!**");
                                                worthEvent.getMessage().getChannel().sendMessage(createdEmbed.build()).queue();
                                            }catch (Exception exception){
                                                exception.printStackTrace();
                                                sharesEvent.getMessage().getChannel().sendMessage("Something went wrong try again!").queue();
                                            }
                                        });
                                    }catch (Exception exception){
                                        exception.printStackTrace();
                                        nameEvent.getMessage().reply("Something went wrong try again!").queue();
                                    }
                                }
                            }, 15, TimeUnit.SECONDS, () -> {
                                EmbedBuilder lateEmbed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                                lateEmbed.addField("**You answered to late " + e.getMessage().getAuthor().getName() +"!**", "creation is cancelled!", false);
                                e.getMessage().getChannel().sendMessage(lateEmbed.build()).queue();
                            });
                        }
                    }, 15, TimeUnit.SECONDS, () -> {
                        EmbedBuilder lateEmbed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                        lateEmbed.addField("**You answered to late " + e.getMessage().getAuthor().getName() +"!**", "creation is cancelled!", false);
                        e.getMessage().getChannel().sendMessage(lateEmbed.build()).queue();
                    });
                }else{
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Create**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + "> you do not have permission for this!");
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }

            }else if(args[0].equalsIgnoreCase("buy")){
                if(ProfileManager.getInstance().hasProfile(e.getMessage().getAuthor().getId())) {
                    String name = "";
                    for (int i = 1; i < args.length; i++) {
                        name = name + args[i] + " ";
                    }
                    for (Building building : BuildingManager.getInstance().getBuildings()) {
                        if (building.getName().equalsIgnoreCase(name.strip())) {
                            UserProfile profile = ProfileManager.getInstance().getProfile(e.getMessage().getAuthor().getId());
                            int shares = building.getShares();
                            long worth = building.getWorth();
                            double singleShare = worth / shares;
                            int availableShares = building.getShares() - building.getPurchasedShares();
                            int affordable = (int) Math.floor(profile.getMoney() / singleShare);
                            if(affordable == 0){
                                EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Buy**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**No more shares available, <@" + e.getMessage().getAuthor().getId() + ">!**");
                                e.getMessage().getChannel().sendMessage(embed.build()).queue();
                            }else {
                                EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**" + building.getName() + "**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Building Cost:**");
                                embed.addField("All available Shares", "" + (building.getShares() - building.getPurchasedShares()), false);
                                embed.addField("Worth per share", "" + singleShare, false);
                                if(availableShares < affordable){
                                    embed.addField("Max affordable", "" + (building.getShares() + building.getPurchasedShares()), false);
                                }else {
                                    embed.addField("Max affordable", "" + affordable, false);
                                }
                                e.getChannel().sendMessage(embed.build()).queue(message -> {
                                    message.addReaction("✅").queue();
                                    waiter.waitForEvent(GuildMessageReactionAddEvent.class, reactionEvent -> reactionEvent.getUser().equals(e.getAuthor()) && reactionEvent.getChannel().equals(e.getChannel()) && reactionEvent.getReactionEmote().getName().equals("✅") && reactionEvent.getMessageId().equals(message.getId()), reactionEvent -> {
                                        EmbedBuilder amountPurchaseEmbed = EmbedManager.getInstance().createEmbed("**Building Buy**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**How many shares do you wanna buy?**");
                                        e.getChannel().sendMessage(amountPurchaseEmbed.build()).queue();
                                        waiter.waitForEvent(GuildMessageReceivedEvent.class, sharesEvent -> sharesEvent.getMessage().getAuthor().equals(e.getMessage().getAuthor()) && sharesEvent.getMessage().getChannel().equals(message.getChannel()), sharesEvent -> {
                                            if(sharesEvent.getMessage().getContentRaw().equalsIgnoreCase("cancel")){
                                                EmbedBuilder canNotAffordEmbed = EmbedManager.getInstance().createEmbed("**Building Buy**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Purchase has been cancelled, <@" + e.getMessage().getAuthor().getId() + ">!**");
                                                e.getMessage().getChannel().sendMessage(canNotAffordEmbed.build()).queue();
                                            }else{
                                                try {
                                                    int amount = Integer.parseInt(sharesEvent.getMessage().getContentRaw());
                                                    if (amount <= affordable && !(amount > availableShares)) {
                                                        EmbedBuilder confirmEmbed = EmbedManager.getInstance().createEmbed("**" + building.getName() + "**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                                                        confirmEmbed.addField("**Are you sure " + sharesEvent.getMessage().getAuthor().getName() + "?**", amount + " shares for " + (amount * singleShare), false);
                                                        e.getChannel().sendMessage(confirmEmbed.build()).queue(confirmMessage -> {
                                                            confirmMessage.addReaction("✅").queue();
                                                            waiter.waitForEvent(GuildMessageReactionAddEvent.class, confirmedEvent -> confirmedEvent.getUser().equals(sharesEvent.getAuthor()) && confirmedEvent.getChannel().equals(sharesEvent.getChannel()) && confirmedEvent.getReactionEmote().getName().equals("✅") && confirmedEvent.getMessageId().equals(confirmMessage.getId()), confirmedEvent -> {
                                                                UserProfile purchasedUserProfile = ProfileManager.getInstance().getProfile(confirmedEvent.getUser().getId());
                                                                if (!purchasedUserProfile.getShares().containsKey(building.getId())) {
                                                                    purchasedUserProfile.getShares().put(building.getId(), amount);
                                                                } else {
                                                                    int ownedAmount = purchasedUserProfile.getShares().get(building.getId());
                                                                    purchasedUserProfile.getShares().put(building.getId(), ownedAmount + amount);
                                                                }
                                                                purchasedUserProfile.setMoney(purchasedUserProfile.getMoney() - (long) Math.floor(singleShare * amount));
                                                                purchasedUserProfile.setWorth(purchasedUserProfile.getWorth() + (long) Math.floor(singleShare * amount));
                                                                building.setPurchasedShares(building.getPurchasedShares() + amount);
                                                                DataManager.saveBuilding(building);
                                                                DataManager.saveProfile(purchasedUserProfile);
                                                                EmbedBuilder purchasedEmbed = EmbedManager.getInstance().createEmbed("**" + building.getName() + "**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "Shares purchased <@" + sharesEvent.getMessage().getAuthor().getId() + ">!");
                                                                e.getChannel().sendMessage(purchasedEmbed.build()).queue();
                                                            }, 15, TimeUnit.SECONDS, () -> {
                                                                EmbedBuilder canNotAffordEmbed = EmbedManager.getInstance().createEmbed("**Building Buy**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Purchase has been cancelled, <@" + e.getMessage().getAuthor().getId() + ">!**");
                                                                e.getMessage().getChannel().sendMessage(canNotAffordEmbed.build()).queue();
                                                            });
                                                        });
                                                    } else {
                                                        EmbedBuilder canNotAffordEmbed = EmbedManager.getInstance().createEmbed("**Building Buy**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**You dont have enough money <@" + e.getMessage().getAuthor().getId() + ">!**");
                                                        e.getMessage().getChannel().sendMessage(canNotAffordEmbed.build()).queue();
                                                    }
                                                } catch (Exception exception) {
                                                    sharesEvent.getMessage().reply("Something went wrong try again!").queue();
                                                }
                                            }
                                        }, 15, TimeUnit.SECONDS, () -> {
                                            EmbedBuilder canNotAffordEmbed = EmbedManager.getInstance().createEmbed("**Building Buy**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Purchase has been cancelled, <@" + e.getMessage().getAuthor().getId() + ">!**");
                                            e.getMessage().getChannel().sendMessage(canNotAffordEmbed.build()).queue();
                                        });
                                    }, 15, TimeUnit.SECONDS, () -> {
                                        EmbedBuilder canNotAffordEmbed = EmbedManager.getInstance().createEmbed("**Building Buy**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Purchase has been cancelled, <@" + e.getMessage().getAuthor().getId() + ">!**");
                                        e.getMessage().getChannel().sendMessage(canNotAffordEmbed.build()).queue();
                                    });
                                });
                            }
                        }
                    }
                }else{
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                    embed.addField("**You do not have a profile " + e.getMessage().getAuthor().getName() + "!**", "create one with: `" + Bot.prefix + "profile create`", false);
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }
            }else if(args[0].equalsIgnoreCase("sell")){
                if(args.length == 1){

                }else{

                }
            }else if(args[0].equalsIgnoreCase("delete")){
                if(Bot.admins.contains(e.getAuthor().getId())){
                    if(!(args.length < 2)) {
                        String name = "";
                        for (int i = 1; i < args.length; i++) {
                            name = name + args[i] + " ";
                        }
                        final String buildingName = name.strip();
                        System.out.println(buildingName);
                        final Building building = BuildingManager.getInstance().getBuildings().stream().filter(b -> b.getName().equalsIgnoreCase(buildingName)).findFirst().orElse(null);
                        if (building != null) {
                            ProfileManager.getInstance().getProfiles().forEach(p -> {
                                if (p.getShares().containsKey(building.getId())) {
                                    p.getShares().remove(building.getId());
                                }
                            });
                            BuildingManager.getInstance().removeBuilding(building.getId());
                            DataManager.deleteBuilding(building);
                            DataManager.saveProfiles();
                            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Delete**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "Building Succesfully deleted, <@" + e.getMessage().getAuthor().getId() + ">");
                            e.getMessage().getChannel().sendMessage(embed.build()).queue();
                        } else {
                            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Delete**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + ">, no building found with that name!");
                            e.getMessage().getChannel().sendMessage(embed.build()).queue();
                        }
                    }else{
                        EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Delete**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + ">, Wrong use of command! `" + Bot.prefix + "building delete <name>`");
                        e.getMessage().getChannel().sendMessage(embed.build()).queue();
                    }
                }else{
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Delete**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + "> you do not have permission for this!");
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }
            }else if(args[0].equalsIgnoreCase("remove")){

            }else{
                EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Building Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**No Command found! Try This:**");
                embed.addField("**Help Command**", "*" + Bot.prefix + "building help*", false);
                embed.addField("**Show Command**", "*" + Bot.prefix + "building show <name>*", false);
                embed.addField("**Create Command**", "*" + Bot.prefix + "building create*", false);
                embed.addField("**Buy Command**", "*" + Bot.prefix + "building buy*", false);
                embed.addField("**Sell Command**", "*" + Bot.prefix + "building sell <name>", false);
                e.getMessage().getChannel().sendMessage(embed.build()).queue();
            }
        }
    }

}
