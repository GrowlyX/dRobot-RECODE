package com.solexgames.robot.listener;

import com.solexgames.core.CorePlugin;
import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.task.MessageDeleteTask;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public class ChannelListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String channelName = event.getChannel().getName();
        final String rawMessage = event.getMessage().getContentRaw();

        if (channelName.equals(RobotPlugin.getInstance().getLangMap().get("syncing|channel")) && !event.getMember().getUser().isBot()) {
            new MessageDeleteTask(event.getMessage(), 40L);
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
        final Emote emote = event.getGuild().getEmoteById("849719227249065984");

        if (channel != null) {
            channel.sendMessage("Welcome to the PvPBar Discord Server, " + event.getMember().getAsMention() + "! " + emote.getAsMention());
        }
    }

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
        final MessageChannel channel = event.getGuild().getTextChannelsByName("boosts", true).get(0);
        final Emote emote = event.getGuild().getEmoteById("839714027423793182");

        if (channel != null) {
            channel.sendMessage("Thanks for boosting our discord server, " + event.getMember().getAsMention() + "! " + emote.getAsMention());
        }
    }
}
