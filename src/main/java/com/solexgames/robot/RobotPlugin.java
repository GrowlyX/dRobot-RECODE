package com.solexgames.robot;

import com.github.kaktushose.jda.commands.entities.JDACommandsBuilder;
import com.solexgames.robot.listener.ChannelListener;
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

    private String mainHex;
    private String supportRole;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        this.mainHex = this.getConfig().getString("settings.hex");
        this.supportRole = this.getConfig().getString("settings.support-role-name");

        try {
            this.discord = JDABuilder.createDefault(this.getConfig().getString("settings.token"))
                    .setStatus(OnlineStatus.valueOf(this.getConfig().getString("settings.status").toUpperCase().replace(" ", "_")))
                    .setActivity(Activity.playing(this.getConfig().getString("settings.activity")))
                    .addEventListeners(new ChannelListener()).build();
        } catch (LoginException loginException) {
            this.getLogger().info("Could not log in to your Bot Client!");
            this.getLogger().info("Maybe double check your token?");

            this.getServer().shutdown();
        }

        JDACommandsBuilder.start(this.discord, "-", true, true, true);
    }

    @Override
    public void onDisable() {
        if (this.discord != null) {
            this.discord.shutdown();
        }
    }
}
