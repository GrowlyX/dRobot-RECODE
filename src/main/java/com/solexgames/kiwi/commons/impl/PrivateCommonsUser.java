package com.solexgames.kiwi.commons.impl;

import com.solexgames.kiwi.commons.AbstractUser;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author GrowlyX
 * @since 8/2/2021
 */

public final class PrivateCommonsUser extends AbstractUser {

    private final PrivateChannel privateChannel;

    /**
     * Construct a Private user
     *
     * @param event   The message received event
     * @param user    User that sent the message
     * @param channel Text channel that the message was sent in
     */
    public PrivateCommonsUser(final @Nullable MessageReceivedEvent event, final @NonNull User user, final @NonNull PrivateChannel channel) {
        super(event, user, channel);
        this.privateChannel = channel;
    }

    /**
     * Get the private channel the message was sent in
     *
     * @return Private channel
     */
    public @NonNull PrivateChannel getPrivateChannel() {
        return this.privateChannel;
    }

}
