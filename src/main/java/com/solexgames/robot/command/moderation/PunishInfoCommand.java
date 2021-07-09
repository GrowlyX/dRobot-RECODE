package com.solexgames.robot.command.moderation;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.robot.util.RoleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 7/6/2021
 */

@CommandController
public class PunishInfoCommand {

    @Command(value = "punishment", name = "Punish information command", desc = "View ban information", usage = "{prefix}punishment <id>", category = "Moderation")
    public void onCommand(CommandEvent commandEvent, String id) {
        final Member member = commandEvent.getMember();

        if (member == null) {
            commandEvent.reply("Something went terrible wrong while trying to execute this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        if (!RoleUtil.isModerator(member)) {
            commandEvent.reply("I'm sorry, but you do not have permission to perform this command.", message -> message.delete().queueAfter(2L, TimeUnit.SECONDS));
            return;
        }

        CompletableFuture.supplyAsync(() -> CorePlugin.getInstance().getPunishmentManager().getPunishments().stream().filter(punishment -> punishment.getPunishIdentification().equals(id)).findFirst().orElse(null))
                .thenAcceptAsync(punishment -> {
                    if (punishment == null) {
                        commandEvent.reply("No punishment with the ID matching " + id + " was found.", message -> message.delete().queueAfter(5L, TimeUnit.SECONDS));
                        return;
                    }

                    final EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setTitle("Punishment " + id);
                    embedBuilder.setColor(Color.ORANGE);
                    embedBuilder.setDescription(String.join("\n",
                            "Reason: **" + punishment.getReason() + "**",
                            "Applied on: **" + CorePlugin.FORMAT.format(punishment.getCreatedAt()) + "**",
                            "Active: **" + (punishment.isValid() ? "Yes" : "No") + "**",
                            "",
                            "*This punishment is a " + punishment.getPunishmentType().getName() + " and is*",
                            "*" + (punishment.isPermanent() ? "" : "not ") + "permanent.*"
                    ));

                    commandEvent.reply(embedBuilder);
                });
    }
}
