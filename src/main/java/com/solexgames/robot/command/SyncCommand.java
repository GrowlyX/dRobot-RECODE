package com.solexgames.robot.command;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.entities.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.global.NetworkPlayer;
import com.solexgames.core.player.ranks.Rank;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.RedisUtil;
import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

@CommandController
public class SyncCommand {

    @Command(value = {"sync", "link"}, name = "Sync Command", usage = "{prefix}sync", desc = "Sync your discord to your in-game account!", category = "Syncing")
    public void onSync(CommandEvent event, String player) {
        final Member member = event.getMember();

        if (member == null) {
            event.reply("Something went wrong ERR: 00");
            return;
        }

        final NetworkPlayer networkPlayer = CorePlugin.getInstance().getPlayerManager().getPlayerFromSyncCode(player);

        if (networkPlayer == null) {
            event.reply(EmbedUtil.getEmbed(member.getUser(), RobotPlugin.getInstance().getLangMap().get("invalid-code|title"), RobotPlugin.getInstance().getLangMap().get("invalid-code|description"), java.awt.Color.RED),
                    message -> message.delete().queueAfter(5L, TimeUnit.SECONDS)
            );
            return;
        }

        if (networkPlayer.isSynced()) {
            event.reply(EmbedUtil.getEmbed(member.getUser(), RobotPlugin.getInstance().getLangMap().get("already-synced|title"), RobotPlugin.getInstance().getLangMap().get("already-synced|description"), java.awt.Color.RED),
                    message -> message.delete().queueAfter(5L, TimeUnit.SECONDS)
            );
            return;
        }

        final Role role = event.getGuild().getRolesByName(RobotPlugin.getInstance().getLangMap().get("syncing|role"), false).get(0);

        if (role == null) {
            event.reply("Something went wrong while trying to execute that command. Please contact a server admin asap.");
            event.reply("Result: **\"The role with the name " + RobotPlugin.getInstance().getLangMap().get("syncing|role") + " does not exist.\"**");
            return;
        }

        event.getMessage().delete().queue();
        event.reply(EmbedUtil.getEmbed(member.getUser(), RobotPlugin.getInstance().getLangMap().get("successful-sync|title"), RobotPlugin.getInstance().getLangMap().get("successful-sync|description").replace("<playerName>", networkPlayer.getName()), java.awt.Color.GREEN),
                message -> message.delete().queueAfter(5L, TimeUnit.SECONDS)
        );

        RedisUtil.publishAsync(RedisUtil.syncDiscord(member.getUser().getAsTag(), networkPlayer.getName()));

        final Rank rank = Rank.getByName(networkPlayer.getRankName());
        final String prefix = ChatColor.stripColor(Color.translate(rank.getPrefix()));
        final String rankName = rank.getName();

        if (rankName.equalsIgnoreCase(RobotPlugin.getInstance().getLangMap().get("settings|default")) || prefix == null) {
            member.modifyNickname(RobotPlugin.getInstance().getLangMap().get("syncing|format").replace("<playerName>", networkPlayer.getName())).queue();
        } else {
            member.modifyNickname(prefix + networkPlayer.getName()).queue();
        }

        try {
            event.getGuild().addRoleToMember(member, role).queue();
        } catch (Throwable throwable) {
            System.out.println("[Discord] Couldn't add the role to " + member.getAsMention() + " because of " + throwable.getMessage() + "!");
        }
    }
}
