package com.solexgames.robot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.solexgames.robot.command.PunishmentInfoCommand;
import com.solexgames.robot.command.ServersCommand;
import com.solexgames.robot.command.SyncCommand;
import com.solexgames.robot.task.BotActivityTask;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

/**
 * @author GrowlyX
 * @since 3/3/2021
 * <p>
 * Holds instances to anything important.
 */

@Getter
@Setter
public final class RobotPlugin extends JavaPlugin {

    @Getter
    private static RobotPlugin instance;

    private JDA discord;
    private CommandClientBuilder commandClient;
    private EventWaiter waiter;

    @Override
    public void onEnable() {
        instance = this;

        this.waiter = new EventWaiter();
        this.commandClient = new CommandClientBuilder();

        this.commandClient.setOwnerId(this.getConfig().getString("settings.owner-id"));
        this.commandClient.setCoOwnerIds(this.getConfig().getStringList("settings.admin-ids").toArray(new String[]{}));
        this.commandClient.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        this.commandClient.setPrefix(this.getConfig().getString("settings.prefix"));

        this.commandClient.addCommands(
                new PunishmentInfoCommand(),
                new ServersCommand(),
                new SyncCommand()
        );

        try {
            this.discord = JDABuilder.createDefault("token")
                    .setStatus(OnlineStatus.valueOf(this.getConfig().getString("settings.status").toUpperCase().replace(" ", "_")))
                    .setActivity(Activity.playing(this.getConfig().getString("settings.activity")))
                    .addEventListeners(this.waiter, this.commandClient.build())
                    .build();
        } catch (LoginException loginException) {
            this.getLogger().info("Could not log in to your Bot Client!");
            this.getLogger().info("Maybe double check your token?");

            this.getServer().shutdown();
        }

        if (this.getConfig().getBoolean("activity-scroll.enabled")) {
            new BotActivityTask();
        }
    }

    @Override
    public void onDisable() {
        if (this.discord != null) {
            this.discord.shutdown();
        }
    }
}
