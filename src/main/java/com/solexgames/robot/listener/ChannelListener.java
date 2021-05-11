package com.solexgames.robot.listener;

import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ChannelListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannel().getName().contains("sync")) {
            if (!event.getMessage().getContentRaw().equals("-linkaccount") || !event.getMessage().getContentRaw().equals("-sync") || !event.getMessage().getContentRaw().equals("-link")) {
                Bukkit.getScheduler().runTaskLater(RobotPlugin.getInstance(), () -> event.getMessage().delete().queue(), 40L);
            }
        }
    }
}
