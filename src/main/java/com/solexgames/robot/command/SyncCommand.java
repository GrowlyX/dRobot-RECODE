package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SyncCommand extends Command {

    public SyncCommand() {
        this.name = "sync";
        this.help = "Sync your account to your in-game profile.";
        this.arguments = "<code>";
        this.aliases = new String[] {"link", "syncaccount", "linkaccount"};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {

    }
}
