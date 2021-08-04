package com.solexgames.robot.listener;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.util.SaltUtil;
import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final Map<Long, String> MEMBER_CODE_MAP = new HashMap<>();

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        final String rawMessage = event.getMessage().getContentRaw();

        if (event.getAuthor() != null) {
            final String code = ChannelListener.MEMBER_CODE_MAP.get(event.getAuthor().getIdLong());
            final PrivateChannel privateChannel = event.getChannel();
            final Guild guild = event.getJDA().getGuildById("866098426805354497");

            if (rawMessage.equals(code)) {
                final Role role = guild.getRolesByName("Player", false).get(0);

                if (role != null) {
                    guild.addRoleToMember(event.getAuthor().getIdLong(), role).queue();

                    ChannelListener.MEMBER_CODE_MAP.remove(event.getAuthor().getIdLong());

                    privateChannel.sendMessage("You've been verified in the **PvPBar Discord**, have fun!").queue();
                } else {
                    privateChannel.sendMessage("Something went wrong during verification.").queue();
                }
            }
        }
    }

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

        final int size = event.getMessage().getMentionedUsers().size();

        if (size >= 10) {
            event.getMessage().delete().queue();

            final Role role = event.getGuild().getRolesByName("Muted", false).get(0);
            final Role playerRole = event.getGuild().getRolesByName("Player", false).get(0);

            if (role != null && playerRole != null) {
                event.getGuild().addRoleToMember(event.getMember(), role).queue(unused -> {
                    CorePlugin.getInstance().getJedisManager().runCommand(jedis -> {
                        jedis.hset("discord_muted", event.getMember().getId(), "");
                    });

                    event.getGuild().removeRoleFromMember(event.getMember(), playerRole).queue();
                    event.getChannel().sendMessage(event.getMember().getAsMention() + " has been permanently muted for `pinging more than ten users`.").queue();
                });
            }
        }

        final boolean isFiltered = CorePlugin.getInstance().getFilterManager().isMessageFiltered(null, rawMessage);

        if (isFiltered) {
            event.getMessage().delete().queue();
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        ChannelListener.MEMBER_CODE_MAP.remove(event.getMember().getIdLong());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final MessageChannel channel = event.getGuild().getTextChannelsByName("arrivals", true).get(0);

        if (channel != null) {
            channel.sendMessage("Welcome to the PvPBar Discord Server, " + event.getMember().getAsMention() + "! <:pvpbar:866698106351386674>").queue();

            CorePlugin.getInstance().getJedisManager().runCommand(jedis -> {
                final boolean isMuted = jedis.hget("discord_muted", event.getMember().getId()) != null;
                final Role role = event.getGuild().getRolesByName("Muted", false).get(0);

                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            });

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
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        final Role role = event.getGuild().getRolesByName("Muted", false).get(0);

        if (event.getRoles().contains(role)) {
            CorePlugin.getInstance().getJedisManager().runCommand(jedis -> {
                jedis.hdel("discord_muted", event.getMember().getId());
            });
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getChannel().getName().equals("verification")) {
            final Role role = event.getGuild().getRolesByName("Player", false).get(0);
            final Role muted = event.getGuild().getRolesByName("Muted", false).get(0);

            if (role == null || muted == null) {
                event.getReaction().removeReaction().queue();
                return;
            }

            if (event.getMember().getRoles().contains(role) || event.getMember().getRoles().contains(muted)) {
                event.getReaction().removeReaction().queue();
                return;
            }

            if (ChannelListener.MEMBER_CODE_MAP.get(event.getMember().getIdLong()) != null) {
                event.getChannel().sendMessage(event.getMember().getAsMention() + ", you were sent a message by our bot. Check your direct messages for the code.").queue(message -> {
                    message.delete().queueAfter(2L, TimeUnit.SECONDS);
                });

                return;
            }

            event.getMember().getUser().openPrivateChannel().submit()
                    .whenComplete((privateChannel, error) -> {
                        final String generatedCode = SaltUtil.getRandomSaltedString(10);

                        try {
                            privateChannel.sendMessage(new EmbedBuilder()
                                    .setTitle("**Verification**")
                                    .setColor(Color.ORANGE)
                                    .setDescription(String.join("\n",
                                            "Please reply with the following code to become verified in the PvPBar Discord Server:",
                                            "",
                                            "Verification Code:",
                                            "`" + generatedCode + "`"
                                    ))
                                    .build()
                            ).queue();
                        } catch (Exception ignored) {
                        }

                        ChannelListener.MEMBER_CODE_MAP.put(event.getMember().getIdLong(), generatedCode);
                    });
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
