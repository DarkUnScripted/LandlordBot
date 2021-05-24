package net.darkunscripted.LandLordBot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.darkunscripted.LandLordBot.Bot;
import net.darkunscripted.LandLordBot.managers.DataManager;
import net.darkunscripted.LandLordBot.managers.EmbedManager;
import net.darkunscripted.LandLordBot.managers.ProfileManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class MoneyCommand extends Command {

    private EventWaiter waiter;

    public MoneyCommand(EventWaiter waiter){
        this.name = "money";
        this.aliases = new String[]{"balance", "bal"};
        this.waiter = waiter;
        this.help = "`" + Bot.prefix + "money help`";
    }

    @Override
    protected void execute(CommandEvent e) {
        if(e.getArgs().length() == 0){
            EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Money Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + ">'s balance");
            embed.addField("**Your Balance is:**", "" + ProfileManager.getInstance().getProfile(e.getMessage().getAuthor().getId()).getMoney(), false);
            e.getMessage().getChannel().sendMessage(embed.build()).queue();
        }else{
            String[] args = e.getArgs().split(" ");
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Money Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Commands**");
                embed.addField("**Help Command**", "*" + Bot.prefix + "money help*", false);
                embed.addField("**Show Command**", "*" + Bot.prefix + "money*", false);
                embed.addField("**Give Command**", "*" + Bot.prefix + "money give <tag member>*", false);
                embed.addField("**Set Command**", "*" + Bot.prefix + "money set <tag member>*", false);
                embed.addField("**Remove Command**", "*" + Bot.prefix + "money remove <tag member>*", false);
                e.getMessage().getChannel().sendMessage(embed.build()).queue();
            }else if(args.length == 1 && args[0].equalsIgnoreCase("show")){
                EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Money Help**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + ">'s balance");
                embed.addField("**Your Balance is:**", "" + ProfileManager.getInstance().getProfile(e.getMessage().getAuthor().getId()).getMoney(), false);
                e.getMessage().getChannel().sendMessage(embed.build()).queue();
            }else if(args.length == 2 && args[0].equalsIgnoreCase("give")){
                if(Bot.admins.contains(e.getMessage().getAuthor().getId())) {
                    if (!(e.getMessage().getMentionedMembers().size() == 0)) {
                        Member member = e.getMessage().getMentionedMembers().get(0);
                        EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Money Give**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + ">\nHow much money do you wanna give <@" + member.getId() + ">?");
                        e.getMessage().getChannel().sendMessage(embed.build()).queue();
                        waiter.waitForEvent(GuildMessageReceivedEvent.class, amountGiveEvent -> amountGiveEvent.getMessage().getAuthor().equals(e.getMessage().getAuthor()) && amountGiveEvent.getChannel().equals(e.getChannel()), (amountGiveEvent) -> {
                            try {
                                long amount = Long.parseLong(amountGiveEvent.getMessage().getContentRaw());
                                EmbedBuilder confirmGiveEmbed = EmbedManager.getInstance().createEmbed("**Money Give**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + ">\nAre you sure you wanna give <@" + member.getId() + "> " + amount + " money?");
                                e.getMessage().getChannel().sendMessage(confirmGiveEmbed.build()).queue((message) -> {
                                    message.addReaction("✅").queue();
                                    waiter.waitForEvent(GuildMessageReactionAddEvent.class, confirmedGiveEvent -> confirmedGiveEvent.getMessageId().equals(message.getId()) && confirmedGiveEvent.getMember().getId().equals(e.getMessage().getAuthor().getId()) && confirmedGiveEvent.getChannel().equals(message.getChannel()), confirmedGiveEvent -> {
                                        ProfileManager.getInstance().getProfile(member.getId()).setMoney(ProfileManager.getInstance().getProfile(member.getId()).getMoney() + amount);
                                        DataManager.saveProfile(ProfileManager.getInstance().getProfile(member.getId()));
                                        EmbedBuilder confirmedGiveEmbed = EmbedManager.getInstance().createEmbed("**Money Give**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "<@" + e.getMessage().getAuthor().getId() + ">\nGave <@" + member.getId() + "> " + amount + " money!");
                                        e.getMessage().getChannel().sendMessage(confirmedGiveEmbed.build()).queue();
                                    }, 15, TimeUnit.SECONDS, () -> {
                                        EmbedBuilder cancelledEmbed = EmbedManager.getInstance().createEmbed("**Money Give**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Transaction has been cancelled, <@" + e.getMessage().getAuthor().getId() + ">!**");
                                        e.getMessage().getChannel().sendMessage(cancelledEmbed.build()).queue();
                                    });
                                });
                            } catch (Exception exception) {
                                amountGiveEvent.getMessage().reply("Something went wrong try again!").queue();
                            }
                        }, 15, TimeUnit.SECONDS, () -> {
                            EmbedBuilder cancelledEmbed = EmbedManager.getInstance().createEmbed("**Money Give**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "**Transaction has been cancelled, <@" + e.getMessage().getAuthor().getId() + ">!**");
                            e.getMessage().getChannel().sendMessage(cancelledEmbed.build()).queue();
                        });
                    } else {
                        EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Money Give**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "You need to mention a player, <@" + e.getMessage().getAuthor().getId() + ">!");
                        e.getMessage().getChannel().sendMessage(embed.build()).queue();
                    }
                }else{
                    EmbedBuilder embed = EmbedManager.getInstance().createEmbed("**Money Give**", Color.BLUE, e.getMessage().getGuild().getName() + " ♢ " + e.getJDA().getSelfUser().getName(), "Only a admin can give money, <@" + e.getMessage().getAuthor().getId() + ">!");
                    e.getMessage().getChannel().sendMessage(embed.build()).queue();
                }
            }
        }
    }
}
