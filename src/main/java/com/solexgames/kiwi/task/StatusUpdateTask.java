package com.solexgames.kiwi.task;

//import com.solexgames.pear.task.GlobalStatusUpdateTask;
import com.solexgames.kiwi.KiwiSpigotPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Activity;

/**
 * @author GrowlyX
 * @since 7/15/2021
 */

public class StatusUpdateTask implements Runnable {

    private Status status = Status.FIRST;

    @Override
    public void run() {
        KiwiSpigotPlugin.getInstance().getDiscord().getPresence().setActivity(this.status.getActivity() == null ? Activity.watching(/*GlobalStatusUpdateTask.GLOBAL_PLAYERS +*/ "0 players online") : this.status.getActivity());

        this.status = Status.getNext(this.status);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {

        WEBSITE(Activity.watching("www.123.com")),
        STORE(Activity.watching("shop.123.com")),
        DISCORD(Activity.watching("www.123.com/discord")),
        TWITTER(Activity.watching("twitter.com/123")),

        ;

        private final Activity activity;

        public static final Status FIRST = Status.values()[0];

        public static Status getNext(Status before) {
            return Status.values().length == before.ordinal() + 1 ? Status.FIRST : Status.values()[before.ordinal() + 1];
        }
    }
}
