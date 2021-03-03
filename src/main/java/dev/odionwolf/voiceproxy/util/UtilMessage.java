package dev.odionwolf.voiceproxy.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

public class UtilMessage {

    public static void sendmessage(CommandSender p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendClickablemessage(CommandSender p, String message) {
        TextComponent msg = new TextComponent();
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, msg.toString()));
        p.spigot().sendMessage(msg);
    }

}
