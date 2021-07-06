package com.solexgames.robot.command.moderation;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.annotations.Concat;
import com.github.kaktushose.jda.commands.annotations.Optional;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.task.MessageDeleteTask;
import com.solexgames.robot.util.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;

/**
 * @author GrowlyX
 * @since 7/2/2021
 */

@CommandController
public class SneakPeakCommand {

    @Command(value = "sneakpeak", name = "Sneak peak command", desc = "Release a sneak peak!", usage = "{prefix}sneakpeak <url>", category = "Developers")
    public void onCommand(CommandEvent commandEvent, String url, @Concat String description) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> new MessageDeleteTask(message, 40L));
            return;
        }

        if (!RoleUtil.isDeveloper(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> new MessageDeleteTask(message, 40L));
            return;
        }

        final EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Sneak Peak**");
        builder.setTimestamp(Instant.now());
        builder.setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl());
        builder.setDescription(description);
        builder.setImage(url);
        builder.setColor(Color.ORANGE);

        commandEvent.reply(builder);
        commandEvent.getMessage().delete().queue();
    }
}
