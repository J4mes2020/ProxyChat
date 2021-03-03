package dev.odionwolf.voiceproxy.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class UtilMessage {

    public static void sendmessage(CommandSender p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
