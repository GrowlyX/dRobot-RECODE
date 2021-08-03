package com.solexgames.robot;

import com.github.kaktushose.jda.commands.entities.JDACommandsBuilder;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.util.UUIDUtil;
import com.solexgames.lib.commons.redis.JedisBuilder;
import com.solexgames.robot.command.PanelCommand;
import com.solexgames.robot.commons.AbstractUser;
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
import org.bukkit.ChatColor;
import org.bukkit.material.Gate;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author GrowlyX
 * @since 3/3/2021
 *         <p>
 *         Holds instances to anything important.
 */

@Getter
@Setter
public final class RobotPlugin extends JavaPlugin {

    @Getter
    private static RobotPlugin instance;
    private final Map<String, String> langMap = new HashMap<>();
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
                    .enableIntents(
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS,

                            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.DIRECT_MESSAGE_REACTIONS
                    )
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

//            final JDA4CommandManager<AbstractUser> commandManager = new JDA4CommandManager<>(
//                    jda,
//                    message -> "!",
//                    (sender, permission) -> permissionRegistry.hasPermission(sender.getUser().getIdLong(), permission),
//                    CommandExecutionCoordinator.simpleCoordinator(),
//                    sender -> {
//                        MessageReceivedEvent event = sender.getEvent().orElse(null);
//
//                        if (sender instanceof JDAPrivateSender) {
//                            JDAPrivateSender jdaPrivateSender = (JDAPrivateSender) sender;
//                            return new PrivateUser(event, jdaPrivateSender.getUser(), jdaPrivateSender.getPrivateChannel());
//                        }
//
//                        if (sender instanceof JDAGuildSender) {
//                            JDAGuildSender jdaGuildSender = (JDAGuildSender) sender;
//                            return new GuildUser(event, jdaGuildSender.getMember(), jdaGuildSender.getTextChannel());
//                        }
//
//                        throw new UnsupportedOperationException();
//                    },
//                    user -> {
//                        MessageReceivedEvent event = user.getEvent().orElse(null);
//                        if (user instanceof PrivateUser) {
//                            PrivateUser privateUser = (PrivateUser) user;
//                            return new JDAPrivateSender(event, privateUser.getUser(), privateUser.getPrivateChannel());
//                        }
//
//                        if (user instanceof GuildUser) {
//                            GuildUser guildUser = (GuildUser) user;
//                            return new JDAGuildSender(event, guildUser.getMember(), guildUser.getTextChannel());
//                        }
//
//                        throw new UnsupportedOperationException();
//                    }
//            );
        }

        Commands.create().assertConsole()
                .handler(h -> {
                    final String parsed = h.arg(0).parseOrFail(String.class);
                    final AtomicInteger atomicInteger = new AtomicInteger();

                    h.reply(ChatColor.GREEN + "Starting robot purge...");

                    this.discord.getGuilds().forEach(guild -> {
                        guild.loadMembers().get().stream()
                                .filter(member1 -> member1.getUser().getName().contains(parsed))
                                .forEach(member1 -> {
                                    member1.kick().queueAfter(1L, TimeUnit.SECONDS);
                                    atomicInteger.incrementAndGet();
                                });
                    });

                    h.reply(ChatColor.YELLOW + "Purging " + ChatColor.GREEN + atomicInteger.get() + ChatColor.YELLOW + " with the phrase " + ChatColor.GREEN + parsed + ChatColor.YELLOW + ".");
                }).register("purgerobots");

        Commands.create().assertConsole()
                .handler(h -> {
                    final String parsed = h.arg(0).parseOrFail(String.class);
                    final AtomicInteger atomicInteger = new AtomicInteger();

                    h.reply(ChatColor.GREEN + "Starting robot purge...");

                    this.discord.getGuilds().forEach(guild -> {
                        guild.loadMembers().get().stream()
                                .filter(member1 -> member1.getUser().getAvatarUrl() != null && member1.getUser().getAvatarUrl().contains(parsed))
                                .forEach(member1 -> {
                                    member1.kick().queueAfter(1L, TimeUnit.SECONDS);
                                    atomicInteger.incrementAndGet();
                                });
                    });

                    h.reply(ChatColor.YELLOW + "Purging " + ChatColor.GREEN + atomicInteger.get() + ChatColor.YELLOW + " with the same pfp " + ".");
                }).register("purgepfps");

        Commands.create().assertPlayer().assertArgument(0, t -> true).assertOp().handler(player -> {
            final String parsed = player.arg(0).parseOrFail(String.class);
            final String parsed2 = player.arg(1).parseOrFail(String.class);

            CompletableFuture.supplyAsync(() -> UUIDUtil.fetchUUID(parsed)).whenComplete((uuid, throwable) -> {
                if (uuid != null) {
                    CorePlugin.getInstance().getJedisManager().runCommand(jedis -> {
                        jedis.hset("discord_rewards", uuid.toString(), parsed2);

                        player.reply(ChatColor.GREEN + "Scheduled player to receive " + parsed2 + ".");
                    });
                } else {
                    player.reply(ChatColor.RED + "That player doesn't exist.");
                }
            });
        }).register("addreward");

        new JedisBuilder()
                .withChannel("robot")
                .withSettings(CorePlugin.getInstance().getDefaultJedisSettings())
                .withHandler(new RedisListener())
                .build();

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
