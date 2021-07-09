package com.solexgames.robot.command.moderation;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.annotations.Concat;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.util.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 7/2/2021
 */

@CommandController
public class ChangeLogCommand {

    @Command(value = "changelog", name = "Change log command", desc = "Release a change log!", usage = "{prefix}changelog <message...>", category = "Developers")
    public void onCommand(CommandEvent commandEvent, @Concat String description) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        if (!RoleUtil.isDeveloper(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        final EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Changelog**");
        builder.setTimestamp(Instant.now());
        builder.setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl());
        builder.setDescription(description);
        builder.setColor(Color.ORANGE);

        commandEvent.reply(builder);
        commandEvent.getMessage().delete().queue();
    }
}
