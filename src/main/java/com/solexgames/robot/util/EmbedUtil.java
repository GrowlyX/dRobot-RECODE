package com.solexgames.robot.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
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
        return new EmbedBuilder()
                .appendDescription(value)
                .setTitle(title).setColor(color)
                .setTimestamp(OffsetDateTime.now())
                .setFooter(user.getAsTag(), user.getAvatarUrl());
    }
}
