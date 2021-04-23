package com.solexgames.robot.task;

import com.solexgames.core.CorePlugin;
import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 3/3/2021
 * <p>
 * JDA Activity Switch runnable.
 */

public class BotActivityTask extends BukkitRunnable {

    private final List<String> activities = new ArrayList<>();

    private int lastCount = -1;

    public BotActivityTask() {
        this.activities.addAll(RobotPlugin.getInstance().getConfig().getStringList("activity-scroll.strings"));

        this.runTaskTimerAsynchronously(RobotPlugin.getInstance(), 20L, 5L * 20L);
    }

    @Override
    public void run() {
        int newIndex = ++lastCount;

        if (newIndex > this.activities.size()) {
            newIndex = 0;
        }

        final String message = this.activities.get(newIndex);
        final Presence presence = RobotPlugin.getInstance().getDiscord().getPresence();
        final Activity activity = Activity.watching(message);

        presence.setActivity(activity);

        this.lastCount = newIndex;
    }
}
