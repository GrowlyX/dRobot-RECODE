package com.solexgames.kiwi.listener;

import com.solexgames.core.listener.custom.ServerDeleteEvent;
import com.solexgames.core.listener.custom.ServerRetrieveEvent;
import com.solexgames.kiwi.KiwiSpigotPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * @author GrowlyX
 * @since 7/15/2021
 */

public class ReactionRolesListener extends ListenerAdapter implements Listener {

    @EventHandler
    public void onServerOnline(ServerRetrieveEvent event) {
        final TextChannel textChannel = KiwiSpigotPlugin.getInstance().getDiscord().getGuilds().get(0)
                .getTextChannelsByName("server-update", true).get(0);

        if (textChannel != null) {
            textChannel.sendMessage("[Connect] **" + event.getServer().getServerName() + "** is now online.").queue();
        }
    }

    @EventHandler
    public void onServerOffline(ServerDeleteEvent event) {
        final TextChannel textChannel = KiwiSpigotPlugin.getInstance().getDiscord().getGuilds().get(0)
                .getTextChannelsByName("server-update", true).get(0);

        if (textChannel != null) {
            textChannel.sendMessage("[Connect] **" + event.getServer().getServerName() + "** is now offline.").queue();
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getChannel().getName().equals("roles")) {
            final Role role = event.getGuild().getRolesByName("UHC-Alerts", true).get(0);

            if (role != null) {
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        final Member member = event.getMember();

        if (event.getChannel().getName().equals("roles") && member != null) {
            final Role role = event.getGuild().getRolesByName("UHC-Alerts", true).get(0);

            if (role != null) {
                event.getGuild().removeRoleFromMember(member, role).queue();
            }
        }
    }
}
