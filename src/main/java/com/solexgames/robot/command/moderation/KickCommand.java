package com.solexgames.robot.command.moderation;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.annotations.Optional;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.task.MessageDeleteTask;
import com.solexgames.robot.util.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;

/**
 * @author GrowlyX
 * @since 7/2/2021
 */

@CommandController
public class KickCommand {

    @Command(value = "kick", name = "Kick command", desc = "Kick a player!", usage = "{prefix}kick <player> [-s]", category = "Moderation")
    public void onCommand(CommandEvent commandEvent, String id, @Optional String silent) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> new MessageDeleteTask(message, 40L));
            return;
        }

        if (!RoleUtil.isModerator(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> new MessageDeleteTask(message, 40L));
            return;
        }

        final Member target = commandEvent.getGuild().getMemberByTag(id);

        if (target == null) {
            commandEvent.reply("No player matching " + id + " is online this server.", message -> new MessageDeleteTask(message, 40L));
            return;
        }

        target.kick().queue();

        if (silent == null) {
            final EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("**Kicked**");
            builder.setTimestamp(Instant.now());
            builder.setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl());
            builder.setDescription(target.getAsMention() + " has been kicked by " + member.getAsMention());
            builder.setColor(Color.GREEN);

            commandEvent.reply(builder);
        }

        commandEvent.getMessage().delete().queue();
    }
}
