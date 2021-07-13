package com.solexgames.robot.command;

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
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 7/12/2021
 */

@CommandController
public class KickRobotsCommand {

    @Command(value = "kickrobots", name = "Kick botting command", desc = "Kick bots from a botting!", usage = "{prefix}kickrobots <regex>", category = "Developers")
    public void onCommand(CommandEvent commandEvent, @Concat String regex) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        if (!RoleUtil.isDeveloper(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        commandEvent.getJDA().getGuildById("828353012752973834").getMembers().stream()
                .filter(member1 -> member1.getEffectiveName().contains(regex))
                .forEach(member1 -> member1.kick().queue());
    }
}
