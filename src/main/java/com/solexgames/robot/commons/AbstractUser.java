package com.solexgames.robot.commons;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

/**
 * @author GrowlyX
 * @since 8/2/2021
 */

public abstract class AbstractUser {

    private final MessageReceivedEvent event;
    private final User user;
    private final MessageChannel channel;

    /**
     * Construct a user
     *
     * @param event   The message received event
     * @param user    Sending user
     * @param channel Channel that the message was sent in
     */
    protected AbstractUser(
            final @Nullable MessageReceivedEvent event,
            final @NonNull User user,
            final @NonNull MessageChannel channel
    ) {
        this.event = event;
        this.user = user;
        this.channel = channel;
    }

    /**
     * Get the message received event
     *
     * @return Optional of the message received event
     */
    public final @NonNull Optional<MessageReceivedEvent> getEvent() {
        return Optional.ofNullable(this.event);
    }

    /**
     * Get the user that sent the message
     *
     * @return Sending user
     */
    public final @NonNull User getUser() {
        return this.user;
    }

    /**
     * Get the channel the message was sent in
     *
     * @return Message channel
     */
    public final @NonNull MessageChannel getChannel() {
        return this.channel;
    }

    /**
     * Get the channel the message was sent in
     *
     * @return Message channel
     */
    public final @NonNull Message getMessage() {
        return this.event.getMessage();
    }

}
