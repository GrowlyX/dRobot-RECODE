package com.solexgames.robot.task;

import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageDeleteTask extends BukkitRunnable {

    private final Message message;

    public MessageDeleteTask(Message message, long duration) {
        this.message = message;

        this.runTaskLater(RobotPlugin.getInstance(), duration);
    }

    @Override
    public void run() {
        try {
            if (this.message != null) {
                this.message.delete().queue();
            }
        } catch (Exception ignored) { }
    }
}
