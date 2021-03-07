package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.robot.util.EmbedUtil;

import java.awt.*;

public class PersonCommand extends Command {

    public PersonCommand() {
        this.name = "person";
        this.help = "Get a picture of a person... that does not exist!";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Person**", "Here's a fake person!", Color.ORANGE, "https://thispersondoesnotexist.com/image"));
    }
}
