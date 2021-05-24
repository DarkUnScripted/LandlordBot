package net.darkunscripted.LandLordBot.managers;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class EmbedManager {

    private static EmbedManager em = new EmbedManager();

    public static EmbedManager getInstance(){
        return em;
    }

    public EmbedBuilder createEmbed(String title, Color color, String footer, String description){
        EmbedBuilder embed = new EmbedBuilder();
        if(title != null){
            embed.setTitle(title);
        }
        if(color != null){
            embed.setColor(color);
        }
        if(footer != null){
            embed.setFooter(footer);
        }
        if(description != null){
            embed.setDescription(description);
        }
        return embed;
    }

}
