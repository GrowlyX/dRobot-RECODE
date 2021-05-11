package com.solexgames.robot.command;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

@CommandController
public class SyncPanelCommand {

    @Command(value = {"syncpanel"}, name = "Sync panel generator", usage = "{prefix}syncpanel", category = "Staff")
    public void onSync(CommandEvent event) {
        if (event.getMember() != null && event.getMember().getId().equals(RobotPlugin.getInstance().getConfig().getString("settings.owner-id"))) {
            event.getMessage().delete().queue();
            event.reply(new EmbedBuilder()
                    .setTitle("**Account Syncing**")
                    .setDescription(
                            "Use `/sync` in-game to receive your account syncing code and use `-sync` in this channel to sync your discord account!"
                    )
                    .setImage(RobotPlugin.getInstance().getConfig().getString("settings.sync-panel-bg"))
                    .setColor(Color.ORANGE)
            );
        }
    }
}
