package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ServersCommand extends Command {

    public ServersCommand() {
        this.name = "servers";
        this.help = "Get information of all available servers.";
        this.aliases = new String[] {"serverinfo", "ip", "online", "server"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {

    }
}
