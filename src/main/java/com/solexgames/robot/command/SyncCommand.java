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
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@CommandController
public class SyncCommand {

    @Command(value = {"sync", "link", "linkaccount"}, name = "Sync Command", usage = "{prefix}sync", desc = "Sync your discord to your in-game account!", category = "Account Syncing")
    public void onSync(CommandEvent event, String player) {
        final Member member = event.getMember();

        if (member == null) {
            event.reply("Something went wrong ERR: 00");
            return;
        }

        final NetworkPlayer networkPlayer = CorePlugin.getInstance().getPlayerManager().getPlayerFromSyncCode(player);

        if (networkPlayer == null) {
            event.reply(EmbedUtil.getEmbed(member.getUser(), "**Invalid Code**", "I'm sorry, but that's not a valid code. Please double check your code or contact staff.", java.awt.Color.RED),
                    message -> Bukkit.getScheduler().runTaskLater(RobotPlugin.getInstance(), () -> message.delete().queue(), 100L)
            );
            return;
        }

        if (networkPlayer.isSynced()) {
            event.reply(EmbedUtil.getEmbed(member.getUser(), "**Already Synced**", "You're already synced to an account!\n\nType `/unsync` in chat to unsync your account and rejoin to setup syncing again.", java.awt.Color.RED),
                    message -> Bukkit.getScheduler().runTaskLater(RobotPlugin.getInstance(), () -> message.delete().queue(), 100L)
            );
            return;
        }

        final Role role = event.getGuild().getRolesByName("Verified", false).get(0);

        if (role == null) {
            event.reply("Something went wrong ERR: 01");
            return;
        }

        event.getMessage().delete().queue();
        event.reply(EmbedUtil.getEmbed(member.getUser(), "**Synced**", "You've successfully synced your discord account to the account with the username: `" + networkPlayer.getName() + "`! \n\nYou've also been given your **Verified** tag, which you can equip with the `/tags` command in-game!", java.awt.Color.GREEN),
                message -> Bukkit.getScheduler().runTaskLater(RobotPlugin.getInstance(), () -> message.delete().queue(), 80L)
        );

        RedisUtil.publishAsync(RedisUtil.syncDiscord(member.getUser().getAsTag(), networkPlayer.getName()));

        final Rank rank = Rank.getByName(networkPlayer.getRankName());
        final String prefix = ChatColor.stripColor(Color.translate(rank.getPrefix()));
        final String rankName = rank.getName();

        if (rankName.equalsIgnoreCase("Default") || prefix == null) {
            member.modifyNickname("[Verified] " + networkPlayer.getName()).queue();
        } else {
            member.modifyNickname(prefix + networkPlayer.getName()).queue();
        }

        try {
            event.getGuild().addRoleToMember(member, role).queue();
        } catch (HierarchyException exception) {
            System.out.println("[Discord] Couldn't add the role to " + member.getAsMention() + " because of " + exception.getMessage() + "!");
        }
    }
}
