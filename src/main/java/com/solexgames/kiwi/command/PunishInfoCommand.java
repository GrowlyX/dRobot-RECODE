package com.solexgames.kiwi.command;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.solexgames.core.CorePlugin;
import com.solexgames.kiwi.commons.annotation.RegisteredCommand;
import com.solexgames.kiwi.commons.impl.PublicCommonsUser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 7/6/2021
 */

@RegisteredCommand
public class PunishInfoCommand {

    @CommandMethod("punishinfo <id>")
    @CommandDescription("Sync your discord to your in-game account")
    @CommandPermission("Admin|Senior Mod|Owner|Developer")
    public void onPunishInfo(
            final PublicCommonsUser publicCommonsUser,
            final @Argument("id") String id
    ) {
        CompletableFuture.supplyAsync(() -> CorePlugin.getInstance().getPunishmentManager().getPunishments().stream().filter(punishment -> punishment.getPunishIdentification().equals(id)).findFirst().orElse(null)).thenAcceptAsync(punishment -> {
            if (punishment == null) {
                publicCommonsUser.reply("No punishment with the ID matching " + id + " was found.");
                return;
            }

            final EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Punishment " + id);
            embedBuilder.setColor(Color.CYAN);
            embedBuilder.setDescription(String.join("\n",
                    "Reason: **" + punishment.getReason() + "**",
                    "Applied on: **" + CorePlugin.FORMAT.format(punishment.getCreatedAt()) + "**",
                    "Active: **" + (punishment.isValid() ? "Yes" : "No") + "**",
                    "",
                    "*This punishment is a " + punishment.getPunishmentType().getName() + " and is*",
                    "*" + (punishment.isPermanent() ? "" : "not ") + "permanent.*"
            ));

            publicCommonsUser.reply(embedBuilder);
        });
    }
}
