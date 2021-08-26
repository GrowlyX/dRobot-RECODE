package com.solexgames.kiwi.command;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.global.NetworkPlayer;
import com.solexgames.core.player.ranks.Rank;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.RedisUtil;
import com.solexgames.kiwi.KiwiSpigotPlugin;
import com.solexgames.kiwi.commons.annotation.RegisteredCommand;
import com.solexgames.kiwi.commons.impl.PublicCommonsUser;
import com.solexgames.kiwi.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

@RegisteredCommand
public class SyncCommand {

    @CommandMethod("sync|link <code>")
    @CommandDescription("Sync your discord to your in-game account")
    public void onSyncCommand(
            final PublicCommonsUser publicCommonsUser,
            final @Argument("code") String code
    ) {
        final NetworkPlayer networkPlayer = CorePlugin.getInstance().getPlayerManager().getPlayerFromSyncCode(code);

        if (networkPlayer == null) {
            publicCommonsUser.reply(EmbedUtil.getEmbed(publicCommonsUser.getUser(), KiwiSpigotPlugin.getInstance().getLangMap().get("invalid-code|title"), KiwiSpigotPlugin.getInstance().getLangMap().get("invalid-code|description"), java.awt.Color.RED),
                    message -> publicCommonsUser.getMessage().delete().queueAfter(5L, TimeUnit.SECONDS)
            );
            return;
        }

        if (networkPlayer.isSynced()) {
            publicCommonsUser.reply(EmbedUtil.getEmbed(publicCommonsUser.getUser(), KiwiSpigotPlugin.getInstance().getLangMap().get("already-synced|title"), KiwiSpigotPlugin.getInstance().getLangMap().get("already-synced|description"), java.awt.Color.RED),
                    message -> message.delete().queueAfter(5L, TimeUnit.SECONDS)
            );
            return;
        }

        final Role role = publicCommonsUser.getTextChannel().getGuild().getRolesByName(KiwiSpigotPlugin.getInstance().getLangMap().get("syncing|role"), false).get(0);

        if (role == null) {
            publicCommonsUser.reply("Something went wrong while trying to execute that command. Please contact a server admin asap.");
            publicCommonsUser.reply("Result: **\"The role with the name " + KiwiSpigotPlugin.getInstance().getLangMap().get("syncing|role") + " does not exist.\"**");
            return;
        }

        publicCommonsUser.getMessage().delete().queue();
        publicCommonsUser.reply(EmbedUtil.getEmbed(publicCommonsUser.getUser(), KiwiSpigotPlugin.getInstance().getLangMap().get("successful-sync|title"), KiwiSpigotPlugin.getInstance().getLangMap().get("successful-sync|description").replace("<playerName>", networkPlayer.getName()), java.awt.Color.GREEN),
                message -> message.delete().queueAfter(5L, TimeUnit.SECONDS)
        );

        RedisUtil.publishAsync(RedisUtil.syncDiscord(publicCommonsUser.getUser().getAsTag(), networkPlayer.getName()));

        final Rank rank = Rank.getByName(networkPlayer.getRankName());
        final String prefix = ChatColor.stripColor(Color.translate(rank.getPrefix()));
        final String rankName = rank.getName();

        if (rankName.equalsIgnoreCase(KiwiSpigotPlugin.getInstance().getLangMap().get("settings|default")) || prefix == null) {
            publicCommonsUser.getMember().modifyNickname(KiwiSpigotPlugin.getInstance().getLangMap().get("syncing|format").replace("<playerName>", networkPlayer.getName())).queue();
        } else {
            publicCommonsUser.getMember().modifyNickname(prefix + networkPlayer.getName()).queue();
        }

        try {
            publicCommonsUser.getTextChannel().getGuild().addRoleToMember(publicCommonsUser.getMember(), role).queue();
        } catch (Throwable throwable) {
            System.out.println("[Discord] Couldn't add the role to " + publicCommonsUser.getMember().getAsMention() + " because of " + throwable.getMessage() + "!");
        }
    }
}
