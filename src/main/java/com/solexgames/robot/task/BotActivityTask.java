package com.solexgames.robot.task;

import com.solexgames.core.CorePlugin;
import com.solexgames.robot.RobotPlugin;
import net.dv8tion.jda.api.entities.Activity;
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

    private int lastCount;

    public BotActivityTask() {
        this.activities.addAll(RobotPlugin.getInstance().getConfig().getStringList("activity-scroll.strings"));

        this.runTaskTimerAsynchronously(RobotPlugin.getInstance(), 20L, 10 * 20L);
    }

    @Override
    public void run() {
        int count = CorePlugin.RANDOM.nextInt(activities.size());
        RobotPlugin.getInstance().getDiscord().getPresence().setActivity(Activity.watching(this.activities.get(this.lastCount == count ? CorePlugin.RANDOM.nextInt(this.activities.size()) : count).replace("<online>", String.valueOf(Bukkit.getOnlinePlayers().size()))));
        this.lastCount = count;
    }
}
