package com.solexgames.robot.command.impl;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.ServerType;
import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.util.EmbedUtil;

public class ServersCommand extends Command {

    public ServersCommand() {
        this.name = "servers";
        this.help = "Get information of all available servers.";
        this.aliases = new String[] {"serverinfo", "ip", "online", "server"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        final ServerType serverType = CorePlugin.getInstance().getServerManager().getNetwork();

        event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Server IP**", "The server IP is: " + serverType.getWebsiteLink(), java.awt.Color.decode(RobotPlugin.getInstance().getMainHex())));
    }
}
