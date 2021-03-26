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

public class DogCommand extends Command {

    public DogCommand() {
        this.name = "dog";
        this.help = "Get a picture of a cute dog!";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        Unirest.get("https://dog.ceo/api/breeds/image/random").asJsonAsync(new Callback<JsonNode>(){

            @Override
            public void completed(HttpResponse<JsonNode> hr) {
                event.reply(EmbedUtil.getEmbed(event.getAuthor(), "**Dog**", "Woof! Here's a very cute dog!", Color.ORANGE, hr.getBody().getObject().getString("message")));
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
