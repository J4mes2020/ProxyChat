package dev.odionwolf.voiceproxy.discordbot;

import dev.odionwolf.voiceproxy.VoiceProxy;
import dev.odionwolf.voiceproxy.commands.ProxyCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;

public class DiscordBotCommandSync extends ListenerAdapter {

    private final VoiceProxy voiceProxy;
    private final ProxyCommand proxyCommand;

    public DiscordBotCommandSync(VoiceProxy voiceProxy, ProxyCommand proxyCommand) {
        this.voiceProxy = voiceProxy;
        this.proxyCommand = proxyCommand;
    }


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        Message message = event.getMessage().;
        User sender = message.getAuthor();
        MessageChannel channel = event.getChannel();
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args.length == 1 && args[0].equalsIgnoreCase(voiceProxy.getConfigData().getString("Prefix") + "sync")) {
            channel.sendMessage("You're missing the code sent on minecraft").queue();
            return;
        }

        if (args[0].equalsIgnoreCase(voiceProxy.getConfigData().getString("Prefix") + "sync")) {
            if (args[1].equalsIgnoreCase(proxyCommand.playerSyncHash.get(proxyCommand.playerUUID).toString())) {
                embedBuilder.setTitle("Success!");
                embedBuilder.setColor(new Color(38, 255, 0));
                embedBuilder.addField("Successfully linked accounts", "", false);
                embedBuilder.addField("", proxyCommand.playerName + " ======== " + sender.getName(), false);
                channel.sendMessage(embedBuilder.build()).queue();
                voiceProxy.getConfigPlayer().set("Players" + "." + proxyCommand.playerUUID + ".discord", sender.getId());
                voiceProxy.getConfigPlayer().set("Players" + "." + proxyCommand.playerUUID + ".minecraft", proxyCommand.playerName);
                proxyCommand.playerSyncHash.remove(proxyCommand.playerUUID);
                try {
                    voiceProxy.saveConfigPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                embedBuilder.setTitle("ERROR!");
                embedBuilder.setColor(new Color(173, 0, 0));
                embedBuilder.addField("That is not a valid code for that account sorry!", "", false);
                channel.sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}