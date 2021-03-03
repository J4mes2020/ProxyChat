package dev.odionwolf.voiceproxy.discordbot;

import dev.odionwolf.voiceproxy.VoiceProxy;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBotCommandSync extends ListenerAdapter {

    private VoiceProxy voiceProxy;

    public DiscordBotCommandSync(VoiceProxy voiceProxy) {
        this.voiceProxy = voiceProxy;
    }

    public void onCommandSync(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (content.equals(voiceProxy.getConfigData().getString("Prefix") + "sync")) {
            User sender = message.getAuthor();
        }

    }
}
