package com.solexgames.robot.command;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.util.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 5/19/2021
 */

@CommandController
public class AnnounceCommand {

    @Command(value = "announce", name = "Announce command", desc = "Create a new announcement!", usage = "{prefix}announce <message...> [-s]", category = "Management")
    public void onAnnounce(CommandEvent commandEvent, String[] args) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        if (!RoleUtil.isManager(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        final String msg = String.join(" ", args);
        final EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Announcement");
        embed.setColor(Color.ORANGE);
        embed.setTimestamp(Instant.now());
        embed.setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl());

        embed.setDescription(commandEvent.getChannel().getGuild().getPublicRole().getAsMention() + " " + msg.replace(" -s", ""));

        commandEvent.getChannel().sendMessage(embed.build()).queue();

        if (!msg.endsWith(" -s")) {
            commandEvent.getChannel().sendMessage("@everyone").queue(toDelete -> toDelete.delete().queueAfter(10L, TimeUnit.MILLISECONDS));
        }
    }
}
