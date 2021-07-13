package com.solexgames.robot.redis;

import com.solexgames.lib.commons.redis.annotation.Subscription;
import com.solexgames.lib.commons.redis.handler.JedisHandler;
import com.solexgames.lib.commons.redis.json.JsonAppender;
import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 7/9/2021
 */

public class RedisListener implements JedisHandler {

    @Subscription(action = "EMBED_SEND")
    public void onEmbedSend(JsonAppender jsonAppender) {
        final String channelId = jsonAppender.getParam("CHANNEL");
        final Guild guild = RobotPlugin.getInstance().getDiscord().getGuildById("839430242908504075");

        if (guild == null) {
            Logger.getGlobal().info("[Robot] [Jedis] PvPBar guild null? Update dRobot");
            return;
        }

        final TextChannel textChannel = guild.getTextChannelById(channelId);

        if (textChannel == null) {
            Logger.getGlobal().info("[Robot] [Jedis] Unknown text channel: " + channelId);
            return;
        }

        final String title = jsonAppender.getParam("TITLE");
        final String description = jsonAppender.getParam("DESCRIPTION");
        final boolean addTimestamp = jsonAppender.getParam("ADD_TIMESTAMP") != null;
        final boolean autoPing = jsonAppender.getParam("AUTO_PING") != null;
        final String image = jsonAppender.getParam("IMAGE");

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("**" + title + "**");
        embedBuilder.setDescription(description);

        if (image != null) {
            embedBuilder.setImage(image);
        }

        if (addTimestamp) {
            embedBuilder.setTimestamp(Instant.now());
        }

        if (autoPing) {
            textChannel.sendMessage("@everyone").queue(toDelete -> toDelete.delete().queueAfter(10L, TimeUnit.MILLISECONDS));
        }

        textChannel.sendMessage(embedBuilder.build()).queue(message -> {
            Logger.getGlobal().info("[Robot] [Jedis] Sent a message to (" + channelId + ") from jedis message.");
        });
    }
}
