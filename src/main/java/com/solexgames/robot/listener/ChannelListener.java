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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class ChannelListener extends ListenerAdapter {

    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private static final List<Integer> MEMBER_BROADCASTS = Arrays.asList(
            100, 200, 300, 400, 500, 600, 700, 800, 900, 1000,
            1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900,
            2000, 2100, 2200, 2300, 2400, 2500, 2600, 2700, 2800,
            2900, 3000
    );


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String channelName = event.getChannel().getName();
        final String rawMessage = event.getMessage().getContentRaw();

        if (channelName.equals(RobotPlugin.getInstance().getLangMap().get("syncing|channel")) && !event.getMember().getUser().isBot()) {
            event.getMessage().delete().queueAfter(2L, TimeUnit.SECONDS);
            return;
        }

        if (channelName.equals("suggestions")) {
            event.getMessage().addReaction("✅").queue();
            event.getMessage().addReaction("\uD83D\uDEAB").queue();
        }

        if (channelName.equals("counting")) {
            try {
                final Integer integer = Integer.parseInt(rawMessage);

                event.getChannel().getHistory().retrievePast(2)
                        .map(messages -> messages.size() == 0 ? "" : messages.get(1).getContentRaw())
                        .queue(message -> {
                            if (message.equals("")) {
                                return;
                            }

                            final Integer beforeNumber = Integer.parseInt(message);

                            if (beforeNumber + 1 != integer) {
                                event.getMessage().delete().queue();
                            }
                        });
            } catch (Exception ignored) {
                event.getMessage().delete().queue();
            }
        }

        final boolean isFiltered = CorePlugin.getInstance().getFilterManager().isMessageFiltered(null, rawMessage);

        if (isFiltered) {
            event.getMessage().delete().queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final MessageChannel channel = event.getGuild().getTextChannelsByName("arrivals", true).get(0);

        if (channel != null) {
            channel.sendMessage("Welcome to the PvPBar Discord Server, " + event.getMember().getAsMention() + "! <:pvpbar:866698106351386674>").queue();

            CompletableFuture.supplyAsync(() -> event.getGuild().loadMembers().get())
                    .whenCompleteAsync((list, error) -> {
                        if (error != null) {
                            error.printStackTrace();
                            return;
                        }

                        if (ChannelListener.MEMBER_BROADCASTS.contains(list.size())) {
                            final MessageChannel memberLogChannel = event.getGuild().getTextChannelsByName("members-reached", true).get(0);

                            if (memberLogChannel != null) {
                                memberLogChannel.sendMessage("We've reached **" + list.size() + "** Discord Members! Good job!").queue();
                            }
                        }
                    });
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getChannel().getName().equals("verification")) {
            final Role role = event.getGuild().getRolesByName("Player", false).get(0);

            if (role == null) {
                event.getReaction().removeReaction().queue();
                return;
            }

            if (event.getMember().getRoles().contains(role)) {
                event.getReaction().removeReaction().queue();
                return;
            }

            event.getGuild().addRoleToMember(event.getMember(), role).queue();
            event.getMember().getUser().openPrivateChannel().submit()
                    .thenCompose(channel -> channel.sendMessage("You've been verified in the **PvPBar Discord**, have fun!").submit())
                    .whenComplete((message, error) -> {
                        if (error != null) {
                            message.addReaction("\uD83C\uDF89").queue();
                        }
                    });

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
