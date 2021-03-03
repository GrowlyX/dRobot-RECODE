package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.ServerType;
import com.solexgames.core.server.NetworkServer;
import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.util.EmbedUtil;
import com.solexgames.robot.util.ServerUtil;
import net.dv8tion.jda.api.entities.Member;

public class ServersCommand extends Command {

    public ServersCommand() {
        this.name = "servers";
        this.help = "Get information of all available servers.";
        this.aliases = new String[] {"serverinfo", "ip", "online", "server"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        ServerType serverType = CorePlugin.getInstance().getServerManager().getNetwork();

        if (serverType.equals(ServerType.POTCLUBVIP)) {
            NetworkServer euProxy = null;
            try {
                euProxy = NetworkServer.getByName("eu-practice");
            } catch (Exception ignored) {
            }
            NetworkServer naProxy = null;
            try {
                naProxy = NetworkServer.getByName("na-practice");
            } catch (Exception ignored) {
            }
            NetworkServer asProxy = null;
            try {
                asProxy = NetworkServer.getByName("as-practice");
            } catch (Exception ignored) {
            }

            event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Network Proxies**", ServerUtil.getServerBuilder(naProxy, asProxy, euProxy), java.awt.Color.decode(RobotPlugin.getInstance().getMainHex())));
        } else {
            event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Server IP**", "The server IP is: " + serverType.getWebsiteLink(), java.awt.Color.decode(RobotPlugin.getInstance().getMainHex())));
        }
    }
}
