package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.player.global.NetworkPlayer;
import com.solexgames.core.player.ranks.Rank;
import com.solexgames.core.util.Color;
import com.solexgames.robot.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.ChatColor;

public class SyncCommand extends Command {

    public SyncCommand() {
        this.name = "sync";
        this.help = "Sync your account to your in-game profile.";
        this.arguments = "<code>";
        this.aliases = new String[] {"link", "syncaccount", "linkaccount"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Usage**", this.name + " " + this.arguments, java.awt.Color.RED));
            return;
        }

        String[] args = event.getArgs().split(" ");
        Member member = event.getMember();

        if (member == null) {
            event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Cannot execute**", "You cannot execute this command.", java.awt.Color.RED));
            return;
        }

        NetworkPlayer networkPlayer = CorePlugin.getInstance().getPlayerManager().getPlayerFromSyncCode(args[0]);

        if (networkPlayer == null) {
            event.reply(EmbedUtil.getEmbed(member.getUser(), "**Not Online**", "You must be online " + CorePlugin.getInstance().getServerManager().getNetwork().getWebsiteLink() + " to sync your account.", java.awt.Color.RED));
            return;
        }

        Role role = event.getGuild().getRolesByName("Verified", false).get(0);

        if (role == null) {
            System.out.println("There is no verified role!");
            return;
        }

        Rank rank = Rank.getByName(networkPlayer.getRankName());
        String prefix = ChatColor.stripColor(Color.translate(rank.getPrefix()));
        String rankName = rank.getName();

        if ((rankName.equalsIgnoreCase("Default")) || prefix == null) {
            member.modifyNickname(ChatColor.stripColor("[Verified] " + networkPlayer.getName())).queue();
        } else {
            member.modifyNickname(prefix + networkPlayer.getName()).queue();
        }

        event.reply(EmbedUtil.getEmbed(member.getUser(), "**Synced**", "You've successfully synced your discord account to the account with the username: **" + networkPlayer.getName() + "**! \n\nYou've also been given your **Verified** tag, which you can equip with the `/prefixes` command in-game!", java.awt.Color.GREEN));
        event.getGuild().addRoleToMember(member, role).queue();
    }
}
