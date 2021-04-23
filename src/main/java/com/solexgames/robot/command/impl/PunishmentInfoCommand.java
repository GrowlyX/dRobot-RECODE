package com.solexgames.robot.command.impl;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.solexgames.core.player.punishment.Punishment;
import com.solexgames.core.util.UUIDUtil;
import com.solexgames.robot.RobotPlugin;
import com.solexgames.robot.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;

public class PunishmentInfoCommand extends Command {

    public PunishmentInfoCommand() {
        this.name = "punishmentinfo";
        this.help = "Get information of a punishment via an id.";
        this.arguments = "<id>";
        this.aliases = new String[]{"punishinfo", "punishid"};
        this.hidden = true;
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        final String[] args = event.getArgs().split(" ");
        final Member member = event.getMember();

        if (event.getAuthor() != null) {
            if (member.getRoles().contains(event.getGuild().getRolesByName(RobotPlugin.getInstance().getSupportRole(), false).get(0))) {
                if (event.getArgs().isEmpty()) {
                    event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Usage**", this.name + " " + this.arguments, java.awt.Color.RED));
                } else {
                    final Punishment punishment = Punishment.getByIdentification(args[0]);

                    if (punishment != null) {
                        event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Punishment Information**", "ID: **" + punishment.getPunishIdentification() + "**\nReason: **" + punishment.getReason() + "**\nTarget: **" + UUIDUtil.fetchName(punishment.getTarget()) + "**\nIssuer: **" + punishment.getIssuerName() + "**\nExpiring: **" + punishment.getExpirationString() + "**", java.awt.Color.decode(RobotPlugin.getInstance().getMainHex())));
                    } else {
                        event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Invalid Punishment**", "That punishment does not exist!", java.awt.Color.RED));
                    }
                }
            } else {
                event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Permission**", "No permission!", java.awt.Color.RED));
            }
        }
    }
}
