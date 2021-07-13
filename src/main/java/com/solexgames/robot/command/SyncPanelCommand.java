package com.solexgames.robot.command;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.annotations.Optional;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

@CommandController
public class SyncPanelCommand {

    @Command(value = {"panel"}, name = "Sync panel generator.", usage = "{prefix}panel", category = "Management")
    public void onSync(CommandEvent event, @Optional String t) {
        if (event.getMember() != null && event.getMember().getId().equals(RobotPlugin.getInstance().getConfig().getString("settings.owner-id"))) {
            if (t != null) {
                event.reply(new EmbedBuilder()
                        .setTitle("**Verification**")
                        .setDescription("React with the shield emoji to receive access to the discord server.")
                        .setImage(RobotPlugin.getInstance().getLangMap().get("panel|image-url"))
                        .setColor(Color.ORANGE), message -> {
                            message.addReaction("\uD83D\uDEE1").queue();
                        }
                );
                return;
            }

            event.reply(new EmbedBuilder()
                    .setTitle(RobotPlugin.getInstance().getLangMap().get("panel|title"))
                    .setDescription(RobotPlugin.getInstance().getLangMap().get("panel|description"))
                    .setImage(RobotPlugin.getInstance().getLangMap().get("panel|image-url"))
                    .setColor(Color.ORANGE)
            );

            event.getMessage().delete().queue();
        }
    }
}
