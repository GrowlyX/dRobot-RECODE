package com.solexgames.robot.command.moderation;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.annotations.Optional;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.task.MessageDeleteTask;
import com.solexgames.robot.util.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.time.Instant;
import java.util.List;

/**
 * @author GrowlyX
 * @since 7/2/2021
 */

@CommandController
public class ClearCommand {

    @Command(value = "clear", name = "Clear command", desc = "Clear a channel!", usage = "{prefix}clear <amount> [-s]", category = "Moderation")
    public void onCommand(CommandEvent commandEvent, int amount, @Optional String silent) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> new MessageDeleteTask(message, 40L));
            return;
        }

        if (!RoleUtil.isModerator(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> new MessageDeleteTask(message, 40L));
            return;
        }

        final List<Message> messages = commandEvent.getChannel().getHistory().retrievePast(amount).complete();
        commandEvent.getChannel().deleteMessages(messages).queue();

        if (silent == null) {
            final EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("**Cleared**");
            builder.setTimestamp(Instant.now());
            builder.setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl());
            builder.setDescription("This channel's been cleared by a moderator.");
            builder.setColor(Color.GREEN);

            commandEvent.reply(builder);
        }

        commandEvent.getMessage().delete().queue();
    }
}
