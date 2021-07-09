package com.solexgames.robot.command.moderation;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.annotations.Optional;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.util.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 7/2/2021
 */

@CommandController
public class LockCommand {

    @Command(value = "lock", name = "Lock command", desc = "Lock the current channel.", usage = "{prefix}lock [-s]", category = "Management")
    public void onCommand(CommandEvent commandEvent, @Optional String silent) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        if (!RoleUtil.isManager(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        if (silent == null) {
            final EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("**Locked**");
            builder.setTimestamp(Instant.now());
            builder.setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl());
            builder.setDescription("This channel's been locked until further notice.");
            builder.setColor(Color.GREEN);

            commandEvent.reply(builder);
            commandEvent.getChannel().getRolePermissionOverrides().forEach(permissionOverride -> {
                final Role role = permissionOverride.getRole();

                if (role != null) {
                    if (!RoleUtil.MANAGEMENT_ROLES.contains(role.getName())) {
                        permissionOverride.delete().queue();
                    }
                }
            });
        }

        commandEvent.getMessage().delete().queue();
    }
}
