package com.solexgames.robot.task;

import com.solexgames.hub.task.GlobalStatusUpdateTask;
import com.solexgames.robot.RobotPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Arrays;
import java.util.List;

/**
 * @author GrowlyX
 * @since 7/15/2021
 */

public class StatusUpdateTask implements Runnable {

    private Status status = Status.FIRST;

    @Override
    public void run() {
        RobotPlugin.getInstance().getDiscord().getPresence().setActivity(this.status.getActivity() == null ? Activity.watching(GlobalStatusUpdateTask.GLOBAL_PLAYERS + " players online") : this.status.getActivity());

        this.status = Status.getNext(this.status);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {

        WEBSITE(Activity.watching("www.pvp.bar")),
        STORE(Activity.watching("store.pvp.bar")),
        DISCORD(Activity.watching("discord.gg/pvpbar")),
        TWITTER(Activity.watching("twitter.com/PvPBarMC")),
        ONLINE(null),

        ;

        private final Activity activity;

        public static final Status FIRST = Status.values()[0];

        public static Status getNext(Status before) {
            return Status.values().length == before.ordinal() + 1 ? Status.FIRST : Status.values()[before.ordinal() + 1];
        }
    }
}
