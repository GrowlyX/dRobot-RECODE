package com.solexgames.robot.listener;

import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.task.MessageDeleteTask;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public class ChannelListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String channelName = event.getChannel().getName();
        final String rawMessage = event.getMessage().getContentRaw();
        final String syncLabel = RobotPlugin.getInstance().getLangMap().get("settings|prefix") + "sync";
        final String syncLabel2 = RobotPlugin.getInstance().getLangMap().get("settings|prefix") + "link";

        if (channelName.equals(RobotPlugin.getInstance().getLangMap().get("syncing|channel")) && !rawMessage.equals(syncLabel) && !rawMessage.equals(syncLabel2) && !event.getMember().getUser().isBot()) {
            new MessageDeleteTask(event.getMessage(), 40L);
        }
    }
}
