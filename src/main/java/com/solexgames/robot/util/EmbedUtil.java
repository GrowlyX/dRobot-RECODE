package com.solexgames.robot.util;

import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.OffsetDateTime;

/**
 * @author GrowlyX
 * @since 3/3/2021
 * <p>
 * Utility to produce embeds easily.
 */

public final class EmbedUtil {

    private EmbedUtil() {
        throw new IllegalStateException("You cannot instantiate a utility class.");
    }

    public static EmbedBuilder getEmbed(User user, String title, String value) {
        return new EmbedBuilder().appendDescription(value).setTitle(title).setColor(Color.ORANGE).setTimestamp(OffsetDateTime.now()).setFooter(user.getAsTag(), user.getAvatarUrl());
    }

    public static void sendEmbed(User user, String title, String value, TextChannel channel) {
        EmbedBuilder embed = new EmbedBuilder().appendDescription(value).setTitle(title).setColor(Color.ORANGE).setTimestamp(OffsetDateTime.now()).setFooter(user.getAsTag(), user.getAvatarUrl());
        channel.sendMessage(embed.build()).queue();
    }

    public static void sendEmbed(User user, String title, String value, String channel, Color color) {
        EmbedBuilder embed = new EmbedBuilder().appendDescription(value).setTitle(title).setColor(color).setTimestamp(OffsetDateTime.now());
        if (user != null) {
            embed.setFooter(user.getAsTag(), user.getAvatarUrl());
        }
        TextChannel textChannel = null;
        try {
            textChannel = RobotPlugin.getInstance().getDiscord().getTextChannelsByName(channel, true).get(0);
        } catch (Exception ignored) {}

        if (textChannel != null) textChannel.sendMessage(embed.build()).queue();
    }

    public static void sendEmbed(User user, String title, String value, TextChannel channel, Color color) {
        EmbedBuilder embed = new EmbedBuilder().appendDescription(value).setTitle(title).setColor(color).setTimestamp(OffsetDateTime.now());
        if (user != null) {
            embed.setFooter(user.getAsTag(), user.getAvatarUrl());
        }
        channel.sendMessage(embed.build()).queue();
    }

    public static Message sendEmbedWithReturn(User user, String title, String value, TextChannel channel, Color color) {
        EmbedBuilder embed = new EmbedBuilder().appendDescription(value).setTitle(title).setColor(color).setTimestamp(OffsetDateTime.now());
        if (user != null) embed.setFooter(user.getAsTag(), user.getAvatarUrl());
        return new MessageBuilder(embed).build();
    }

    public static Message sendEmbedWithReturn(User user, String title, String value, String footer, Color color) {
        EmbedBuilder embed = new EmbedBuilder().appendDescription(value).setTitle(title).setColor(color).setFooter(footer);
        return new MessageBuilder(embed).build();
    }

    public static void sendEmbed(User user, String title, String value, TextChannel channel, Color color, String url) {
        EmbedBuilder embed = new EmbedBuilder().appendDescription(value).setTitle(title).setColor(color).setTimestamp(OffsetDateTime.now()).setThumbnail(url);
        if (user != null) {
            embed.setFooter(user.getAsTag(), user.getAvatarUrl());
        }
        channel.sendMessage(embed.build()).queue();
    }
}
