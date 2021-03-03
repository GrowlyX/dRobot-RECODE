package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
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
        } else {
            String[] args = event.getArgs().split(" ");
            Member member = event.getMember();

            if (CorePlugin.getInstance().getPlayerManager().getAllSyncCodes().containsKey(args[0])) {
                if (CorePlugin.getInstance().getPlayerManager().getPlayer(CorePlugin.getInstance().getPlayerManager().getAllSyncCodes().get(args[0])) != null) {
                    PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(CorePlugin.getInstance().getPlayerManager().getAllSyncCodes().get(args[0]));
                    if (!potPlayer.isSynced()) {
                        Role role = event.getGuild().getRolesByName("Verified", false).get(0);

                        potPlayer.setSynced(true);
                        potPlayer.setSyncDiscord(member.getUser().getAsTag());
                        potPlayer.getMedia().setDiscord(member.getUser().getAsTag());
                        potPlayer.getAllPrefixes().add("Verified");

                        potPlayer.getPlayer().sendMessage(new String[]{"  ", Color.translate("&aThanks for syncing your account! You have been given the &2✔ &7(Verified) &atag!"), Color.translate("&aYour account has been synced to &b" + member.getUser().getAsTag() + "&a."), "  "});

                        String prefix = ChatColor.stripColor(Color.translate(potPlayer.getActiveGrant().getRank().getPrefix()));
                        if ((potPlayer.getActiveGrant().getRank().getName().equals("Default")) || prefix == null) {
                            member.modifyNickname(ChatColor.stripColor("[Verified] " + potPlayer.getName())).queue();
                        } else {
                            member.modifyNickname(prefix + potPlayer.getName()).queue();
                        }

                        event.reply(EmbedUtil.getEmbed(member.getUser(), "**Synced**", "Successfully synced your account!\n\nSynced to: **" + potPlayer.getName() + "**", java.awt.Color.GREEN));

                        event.getGuild().addRoleToMember(member, role).queue();
                    } else {
                        event.reply(EmbedUtil.getEmbed(member.getUser(), "**Already Synced**", "You are already synced! Use /unsync ingame to unsync your account.", java.awt.Color.RED));
                    }
                } else {
                    event.reply(EmbedUtil.getEmbed(member.getUser(), "**Not Online**", "You must be online " + CorePlugin.getInstance().getServerManager().getNetwork().getWebsiteLink() + " to sync your account.", java.awt.Color.RED));
                }
            } else {
                event.reply(EmbedUtil.getEmbed(member.getUser(), "**Not Online**", "You must be online " + CorePlugin.getInstance().getServerManager().getNetwork().getWebsiteLink() + " to sync your account.", java.awt.Color.RED));
            }
        }
    }
}
