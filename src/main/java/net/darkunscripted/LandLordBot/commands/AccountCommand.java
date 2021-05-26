package net.darkunscripted.LandLordBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.darkunscripted.LandLordBot.Bot;
import net.darkunscripted.LandLordBot.domain.Building;
import net.darkunscripted.LandLordBot.domain.UserProfile;
import net.darkunscripted.LandLordBot.managers.BuildingManager;
import net.darkunscripted.LandLordBot.managers.EmbedManager;
import net.darkunscripted.LandLordBot.managers.ProfileManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class AccountCommand extends Command {

    private EventWaiter waiter;

    public AccountCommand(EventWaiter waiter){
        this.name = "profile";
        this.aliases = new String[]{"accounts", "account", "profiles"};
        this.waiter = waiter;
        this.help = "`" + Bot.prefix + "profile help`";
    }

    @Override
    protected void execute(CommandEvent e) {
        if(e.getArgs().length() == 0){
            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Commands**");
            embed.addField("**Help Command**", "*" + Bot.prefix + "profile help*", false);
            embed.addField("**Show Command**", "*" + Bot.prefix + "profile show <name>*", false);
            embed.addField("**Create Command**", "*" + Bot.prefix + "profile create*", false);
            embed.addField("**Reset Command**", "*" + Bot.prefix + "profile reset*", false);
            e.getMessage().getChannel().sendMessage(embed.build()).queue();
        } else {
            String[] args = e.getArgs().split(" ");
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Commands**");
                embed.addField("**Help Command**", "*" + Bot.prefix + "profile help*", false);
                embed.addField("**Show Command**", "*" + Bot.prefix + "profile show <name>*", false);
                embed.addField("**Create Command**", "*" + Bot.prefix + "profile create*", false);
                embed.addField("**Reset Command**", "*" + Bot.prefix + "profile reset*", false);
                e.getMessage().getChannel().sendMessage(embed.build()).queue();
            }else if(args.length == 1 && args[0].equalsIgnoreCase("show")){
                UserProfile profile = ProfileManager.getInstance().getProfile(e.getMessage().getAuthor().getId());
                if(profile != null){
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "" + e.getMessage().getAuthor().getName()+ "'s Profile");
                    embed.addField("**ID:**", "" + profile.getId(), false);
                    embed.addField("**userID:**", "" + profile.getUserID(), false);
                    embed.addField("**money:**", "" + profile.getMoney(), false);
                    if(profile.getShares().size() > 0) {
                        embed.addField("**Properties:**", "", false);
                        for (UUID buildingID : new ArrayList<>(profile.getShares().keySet())) {
                            Building building = BuildingManager.getInstance().getBuilding(buildingID);
                            embed.addField("**" + building.getName() + ":**", "" + profile.getShares().get(buildingID), false);
                        }
                    }
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }else{
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                    embed.addField("**You do not have a profile " + e.getMessage().getAuthor().getName() + "!**", "create one with: `" + Bot.prefix + "profile create`", false);
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }
            }else if(args.length == 1 && args[0].equalsIgnoreCase("create")){
                UserProfile profile = ProfileManager.getInstance().getProfile(e.getMessage().getAuthor().getId());
                if(profile == null){
                    ProfileManager.getInstance().createProfile(e.getMessage().getAuthor().getId());
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                    embed.addField("**Succesfully made a profile " + e.getMessage().getAuthor().getName() + "!**", "show it using `" + Bot.prefix + "profile show`", false);
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }else{
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), null);
                    embed.addField("**You already have a profile " + e.getMessage().getAuthor().getName() + "!**", "show it using `" + Bot.prefix + "profile show`", false);
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }
            }else if(args.length > 1 && args[0].equalsIgnoreCase("show")){
                if(!(e.getMessage().getMentionedMembers().size() == 0)) {
                    Member member = e.getMessage().getMentionedMembers().get(0);
                    if (member != null) {
                        UserProfile profile = ProfileManager.getInstance().getProfile(member.getId());
                        if (profile != null) {
                            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "" + member.getEffectiveName() + "'s Profile");
                            embed.addField("**ID:**", "" + profile.getId(), false);
                            embed.addField("**userID:**", "" + profile.getUserID(), false);
                            embed.addField("**money:**", "" + profile.getMoney(), false);
                            if (profile.getShares().size() > 0) {
                                embed.addField("**Properties:**", "", false);
                                for (UUID buildingID : new ArrayList<>(profile.getShares().keySet())) {
                                    Building building = BuildingManager.getInstance().getBuilding(buildingID);
                                    embed.addField("**" + building.getName() + ":**", "" + profile.getShares().get(buildingID), false);
                                }
                            }
                            e.getMessage().getChannel().sendMessage(embed.build()).queue();
                        } else {
                            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**That user doesn't have a profile, <@" + e.getMessage().getAuthor().getId() + ">!**");
                            e.getMessage().getChannel().sendMessage(embed.build()).queue();
                        }
                    } else {
                        EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Profile**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**You need to mention a user, <@" + e.getMessage().getAuthor().getId() + ">!**");
                        e.getMessage().getChannel().sendMessage(embed.build()).queue();
                    }
                }
            }
        }
    }
}