package com.solexgames.robot.command;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.core.server.NetworkServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 7/9/2021
 */

@CommandController
public class TPSCommand {

    @Command(value = "clear", name = "Clear command", desc = "Clear a channel!", usage = "{prefix}clear <amount> [-s]", category = "Moderation")
    public void onCommand(CommandEvent commandEvent, String server) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        final NetworkServer networkServer = NetworkServer.getByName(server);

        if (networkServer == null) {
            commandEvent.reply("That server doesn't exist.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        final EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**TPS**");
        builder.setTimestamp(Instant.now());
        builder.setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl());
        builder.setDescription("The tps of " + server + " is currently at " + "**" + networkServer.getTicksPerSecondSimplified() + "**.");
        builder.setColor(Color.ORANGE);

        commandEvent.reply(builder);
    }
}
