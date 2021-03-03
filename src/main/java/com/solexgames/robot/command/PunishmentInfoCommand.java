package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class PunishmentInfoCommand extends Command {

    public PunishmentInfoCommand() {
        this.name = "punishmentinfo";
        this.help = "Get information of a punishment via an id.";
        this.arguments = "<id>";
        this.aliases = new String[] {"punishinfo", "punishid"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {

    }
}
