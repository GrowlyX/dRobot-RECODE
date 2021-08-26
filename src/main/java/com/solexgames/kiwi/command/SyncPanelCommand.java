package com.solexgames.kiwi.command;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.solexgames.kiwi.KiwiSpigotPlugin;
import com.solexgames.kiwi.commons.annotation.RegisteredCommand;
import com.solexgames.kiwi.commons.impl.PublicCommonsUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

/**
 * @author GrowlyX
 * @since 8/16/2021
 */

@RegisteredCommand
public class SyncPanelCommand {

    @CommandMethod("syncpanel|panel")
    @CommandDescription("Sync your discord to your in-game account")
    @CommandPermission("Owner|Developer")
    public void onSyncCommand(final PublicCommonsUser publicCommonsUser) {
        final MessageEmbed embedBuilder = new EmbedBuilder()
                .setDescription(KiwiSpigotPlugin.getInstance().getLangMap().get("panel|description"))
                .setTitle(KiwiSpigotPlugin.getInstance().getLangMap().get("panel|title"))
                .setColor(Color.GREEN).build();

        publicCommonsUser.getTextChannel().sendMessage(embedBuilder).queue();
    }
}
