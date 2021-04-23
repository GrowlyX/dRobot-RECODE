package com.solexgames.robot.command.impl;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProfileCommand extends Command {

    public ProfileCommand() {
        this.name = "profile";
        this.help = "Get in-game information of a player.";
        this.arguments = "<name>";
        this.aliases = new String[]{"userinfo"};
        this.hidden = true;
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        final String[] args = event.getArgs().split(" ");

        if (event.getArgs().isEmpty()) {
            event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Usage**", this.name + " " + this.arguments, java.awt.Color.RED));
            return;
        }

        final UUID uuid = CorePlugin.getInstance().getUuidCache().getUuidFromUsername(args[0]);

        if (uuid == null) {
            event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Invalid Player**", "The player with the name '" + args[0] + "' does not exist!", java.awt.Color.RED));
            return;
        }

        CompletableFuture.supplyAsync(() -> CorePlugin.getInstance().getPlayerManager().getDocumentByUuid(uuid).orElse(null))
                .thenAccept(document -> {
                    if (document != null) {
                        final EmbedBuilder builder = EmbedUtil.getProfile(
                                event.getAuthor(),
                                "**" + document.getString("name") + "'s Profile:**",
                                java.awt.Color.decode(RobotPlugin.getInstance().getMainHex()),
                                "https://visage.surgeplay.com/head/512/" + document.getString("name")
                        );

                        builder.addField("Rank", document.getString("rankName"), true);
                        builder.addField("Discord", (document.getString("syncDiscord") != null ? document.getString("syncDiscord") : "Not Synced"), true);
                        builder.addField("Online", (document.getBoolean("currentlyOnline") ? "Yes" : "No"), true);
                        builder.addField("Last Joined", document.getString("lastJoined"), true);
                        builder.addField("First Joined", document.getString("lastJoined"), true);
                        builder.addField("Prefix", document.getString("appliedPrefix"), true);
                        builder.addField("Banned", document.getBoolean("currentlyBanned").toString(), true);

                        event.reply(builder.build());
                    } else {
                        event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Invalid Player**", "The player with the name '" + args[0] + "' does not exist!", java.awt.Color.RED));
                    }
                });
    }
}
