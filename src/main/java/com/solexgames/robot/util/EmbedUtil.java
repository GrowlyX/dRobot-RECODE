package com.solexgames.robot.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.OffsetDateTime;

/**
 * @author GrowlyX
 * @since 3/3/2021
 * <p>
 * Utility to produce embeds easily.
 */

@UtilityClass
public final class EmbedUtil {

    public static EmbedBuilder getEmbed(User user, String title, String value, Color color) {
        return new EmbedBuilder().appendDescription(value).setTitle(title).setColor(color).setTimestamp(OffsetDateTime.now()).setFooter(user.getAsTag(), user.getAvatarUrl());
    }

    public static MessageEmbed getEmbed(User user, String title, String value, Color color, String img) {
        return new EmbedBuilder().setImage(img).appendDescription(value).setTitle(title).setColor(color).setTimestamp(OffsetDateTime.now()).setFooter(user.getAsTag(), user.getAvatarUrl()).build();
    }

    public static EmbedBuilder getProfile(User user, String title, Color color, String thumb) {
        return new EmbedBuilder().setThumbnail(thumb).setTitle(title).setColor(color).setTimestamp(OffsetDateTime.now()).setFooter(user.getAsTag(), user.getAvatarUrl());
    }
}
