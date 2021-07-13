package com.solexgames.robot.listener;

import com.solexgames.core.CorePlugin;
import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class ChannelListener extends ListenerAdapter {

    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String channelName = event.getChannel().getName();
        final String rawMessage = event.getMessage().getContentRaw();

        if (channelName.equals(RobotPlugin.getInstance().getLangMap().get("syncing|channel")) && !event.getMember().getUser().isBot()) {
            event.getMessage().delete().queueAfter(2L, TimeUnit.SECONDS);
            return;
        }

        final boolean isFiltered = CorePlugin.getInstance().getFilterManager().isMessageFiltered(null, event.getMessage().getContentRaw());

        if (isFiltered) {
            event.getMessage().delete().queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final MessageChannel channel = event.getGuild().getTextChannelsByName("arrivals", true).get(0);

        if (channel != null) {
            channel.sendMessage("Welcome to the PvPBar Discord Server, " + event.getMember().getAsMention() + "! <:pvpbar:849719227249065984>").queue();
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getChannel().getName().equals("verify")) {
            final Role role = event.getGuild().getRolesByName("Verified", false).get(0);

            if (role == null) {
                event.getReaction().removeReaction().queue();
                return;
            }

            if (event.getMember().getRoles().contains(role)) {
                event.getReaction().removeReaction().queue();
                return;
            }

            event.getGuild().addRoleToMember(event.getMember(), role).queue();

            try {
                event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("You've been verified in the **PvPBar Discord**, have fun!").queue();
                });
            } catch (Exception ignored) { }

            event.getReaction().removeReaction().queue();
        }
    }

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
        final MessageChannel channel = event.getGuild().getTextChannelsByName("boosts", true).get(0);

        if (channel != null) {
            channel.sendMessage("Thanks for boosting our discord server, " + event.getMember().getAsMention() + "! <:nitro:839714027423793182>").queue();
        }
    }
}
