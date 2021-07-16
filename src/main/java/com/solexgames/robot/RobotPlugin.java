package com.solexgames.robot;

import com.github.kaktushose.jda.commands.entities.JDACommandsBuilder;
import com.solexgames.core.CorePlugin;
import com.solexgames.lib.commons.redis.JedisBuilder;
import com.solexgames.robot.command.PanelCommand;
import com.solexgames.robot.listener.ChannelListener;
import com.solexgames.robot.listener.ReactionRolesListener;
import com.solexgames.robot.redis.RedisListener;
import com.solexgames.robot.task.StatusUpdateTask;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.Commands;
import me.lucko.helper.command.CommandInterruptException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 3/3/2021
 * <p>
 * Holds instances to anything important.
 */

@Getter
@Setter
public final class RobotPlugin extends JavaPlugin {

    private final Map<String, String> langMap = new HashMap<>();

    @Getter
    private static RobotPlugin instance;

    private JDA discord;

    private String mainHex;
    private String supportRole;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.loadConfigLangData();

        this.mainHex = this.getConfig().getString("settings.hex");
        this.supportRole = this.getConfig().getString("settings.support-role-name");

        try {
            this.discord = JDABuilder.createLight(this.getConfig().getString("settings.token"))
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_REACTIONS)
                    .setStatus(OnlineStatus.valueOf(this.getConfig().getString("settings.status").toUpperCase().replace(" ", "_")))
                    .setActivity(Activity.playing(this.getConfig().getString("settings.activity")))
                    .addEventListeners(
                            new ChannelListener(),
                            new ReactionRolesListener()
                    ).build();
        } catch (LoginException loginException) {
            this.getLogger().info("Could not log in to your Bot Client!");
            this.getLogger().info("Maybe double check your token?");

            System.exit(0);
        } finally {
            JDACommandsBuilder.start(this.discord, this.langMap.get("settings|prefix"));
        }

        Commands.create().assertPlayer().assertOp()
                .handler(h -> {
                    final String parsed = h.arg(0).parseOrFail(String.class);

                    this.discord.getGuilds().forEach(guild -> {
                        guild.loadMembers().get().stream()
                                .filter(member1 -> member1.getUser().getName().contains(parsed))
                                .forEach(member1 -> member1.kick().queueAfter(1L, TimeUnit.SECONDS, aVoid -> System.out.println("kicked someone")));
                    });
                }).register("purgerobots");

        new JedisBuilder().withChannel("robot")
                .withSettings(CorePlugin.getInstance().getDefaultJedisSettings())
                .withHandler(new RedisListener()).build();

        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new StatusUpdateTask(), 0L, 100L);
    }

    private void loadConfigLangData() {
        this.langMap.put("successful-sync|title", this.getString("successful-sync.title"));
        this.langMap.put("successful-sync|description", this.getString("successful-sync.description"));

        this.langMap.put("already-synced|title", this.getString("already-synced.title"));
        this.langMap.put("already-synced|description", this.getString("already-synced.description"));

        this.langMap.put("invalid-code|title", this.getString("invalid-code.title"));
        this.langMap.put("invalid-code|description", this.getString("invalid-code.description"));

        this.langMap.put("panel|title", this.getString("panel.title"));
        this.langMap.put("panel|description", this.getString("panel.description"));
        this.langMap.put("panel|image-url", this.getString("panel.image-url"));

        this.langMap.put("syncing|role", this.getString("syncing.role"));
        this.langMap.put("syncing|channel", this.getString("syncing.channel"));
        this.langMap.put("syncing|format", this.getString("syncing.username-format"));

        this.langMap.put("settings|prefix", this.getSetting("prefix"));
        this.langMap.put("settings|default", this.getSetting("server-default-rank"));
    }

    public String getString(String path) {
        return this.getConfig().getString("language." + path)
                .replace("<nl>", "\n");
    }

    public String getSetting(String path) {
        return this.getConfig().getString("settings." + path);
    }

    @Override
    public void onDisable() {
        if (this.discord != null) {
            this.discord.shutdown();
        }
    }
}
