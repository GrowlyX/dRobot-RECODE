package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.punishment.Punishment;
import com.solexgames.core.util.UUIDUtil;
import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

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
        String[] args = event.getArgs().split(" ");

        Bukkit.getScheduler().runTaskAsynchronously(RobotPlugin.getInstance(), () -> {
            if (event.getArgs().isEmpty()) {
                event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Usage**", this.name + " " + this.arguments, java.awt.Color.RED));
            } else {
                try {
                    Map.Entry<UUID, String> map = UUIDUtil.getUUID(args[0]);
                    Document document = CorePlugin.getInstance().getPlayerManager().getDocumentByName(map.getValue()).orElse(null);

                    if (document != null) {
                        EmbedBuilder builder = EmbedUtil.getProfile(
                                event.getAuthor(),
                                "**" + map.getValue() + "'s Profile:**",
                                java.awt.Color.decode(RobotPlugin.getInstance().getMainHex()),
                                "https://visage.surgeplay.com/head/512/" + map.getKey().toString()
                        );

                        builder.addField("Rank", document.getString("rankName"), true);
                        builder.addField("Discord", (document.getString("syncDiscord") != null ? document.getString("syncDiscord") : "Not Synced"), true);
                        builder.addField("Online", (document.getBoolean("currentlyOnline") ? "Yes" : "No"), true);
                        builder.addField("Last Joined", document.getString("lastJoined"), true);
                        builder.addField("First Joined", document.getString("lastJoined"), true);
                        builder.addField("Prefix", document.getString("appliedPrefix"), true);

                        event.reply(builder.build());
                    } else {
                        event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Invalid Player**", "That player's profile does not exist!", java.awt.Color.RED));
                    }
                } catch (Exception ignored) {
                    event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Invalid Player**", "That player does not exist!", java.awt.Color.RED));
                }
            }
        });
    }
}
