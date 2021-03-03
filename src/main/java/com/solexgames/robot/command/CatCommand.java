package com.solexgames.robot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.solexgames.robot.util.EmbedUtil;

import java.awt.*;

public class CatCommand extends Command {

    public CatCommand() {
        this.name = "cat";
        this.help = "Get a picture of a cute cat!";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {

        Unirest.get("https://aws.random.cat/meow").asJsonAsync(new Callback<JsonNode>(){

            @Override
            public void completed(HttpResponse<JsonNode> hr) {
                event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Cat**", "Meow! Here's a very cute cat!", Color.ORANGE, hr.getBody().getObject().getString("file")));
            }

            @Override
            public void failed(UnirestException ue) {
                event.reactError();
            }

            @Override
            public void cancelled() {
                event.reactError();
            }
        });
    }
}
