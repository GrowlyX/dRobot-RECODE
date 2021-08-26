package com.solexgames.kiwi.commons.impl;

import com.solexgames.kiwi.commons.AbstractUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;

public final class PublicCommonsUser extends AbstractUser {

    private final Member member;
    private final TextChannel channel;

    /**
     * Construct a Guild user
     *
     * @param event   The message received event
     * @param member  Guild member that sent the message
     * @param channel Text channel that the message was sent in
     */
    public PublicCommonsUser(final @Nullable MessageReceivedEvent event, final @NonNull Member member, final @NonNull TextChannel channel) {
        super(event, member.getUser(), channel);
        this.member = member;
        this.channel = channel;
    }

    /**
     * Get the member that sent the message
     *
     * @return Sending member
     */
    public @NonNull Member getMember() {
        return this.member;
    }

    /**
     * Get the text channel the message was sent in
     *
     * @return Message channel
     */
    public @NonNull TextChannel getTextChannel() {
        return this.channel;
    }

    public void reply(String message) {
        this.channel.sendMessage(message).queue();
    }

    public void reply(EmbedBuilder message) {
        this.channel.sendMessage(message.build()).queue();
    }

    public void reply(EmbedBuilder message, Consumer<Message> consumer) {
        this.channel.sendMessage(message.build()).queue(consumer);
    }

}
