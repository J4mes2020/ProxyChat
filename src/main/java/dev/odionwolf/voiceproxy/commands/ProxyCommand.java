package dev.odionwolf.voiceproxy.commands;

import dev.odionwolf.voiceproxy.VoiceProxy;
import dev.odionwolf.voiceproxy.util.UtilMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ProxyCommand implements CommandExecutor {

    private final VoiceProxy voiceProxy;

    public String waiting;
    public String proxy;
    public UUID playerUUID;
    public UUID randomString;
    public String playerName;

    public HashMap<UUID, UUID> playerSyncHash = new HashMap<>();

    public ProxyCommand(VoiceProxy voiceProxy) {
        this.voiceProxy = voiceProxy;
        voiceProxy.getCommand("voiceproxy").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("voice.admin")) {
            if (args.length == 2 && args[0].equalsIgnoreCase("waiting")) {
                waiting = args[1];
                voiceProxy.getConfigData().set("Waiting", waiting);
                try {
                    voiceProxy.saveConfigData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("proxy")) {
                proxy = args[1];
                voiceProxy.getConfigData().set("Proxy", proxy);
                try {
                    voiceProxy.saveConfigData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
            if (args.length == 1 && args[0].equalsIgnoreCase("sync")) {

                if (playerSyncHash.containsKey(playerUUID)) {
                    UtilMessage.sendmessage(player, "&cYou already have a ongoing sync request");
                    UtilMessage.sendmessage(player, "&cDo &4/voiceproxy cancelsync &cto cancel the request"); // implement this
                    return true;
                }

                /*/for (String listOfPlayers : voiceProxy.getConfigPlayer().getConfigurationSection("Players").getKeys(true)) {
                    if (listOfPlayers.equalsIgnoreCase(String.valueOf(player.getUniqueId()))) {
                        UtilMessage.sendmessage(player, "&cYou already have a account synced!");
                    }

                 */

                //NEED TO FIX AUTH TO MAKE SURE THEY DON'T ALREADY HAVE A ACCOUNT

                playerUUID = player.getUniqueId();
                playerName = player.getName();
                randomString = UUID.randomUUID();
                playerSyncHash.put(playerUUID, randomString);
                TextComponent msg = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        "&3Type &b" + voiceProxy.getConfigData().getString("Prefix") +
                                "sync " + "&n" + randomString + "&r&3 to sync your account with discord."));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, randomString.toString()));
                player.spigot().sendMessage(msg);
                UtilMessage.sendmessage(player, "");
                UtilMessage.sendmessage(player, "&9&lCLICK THE CODE TO COPY");
                return true;

            }

        if (args.length == 2 && args[0].equalsIgnoreCase("move")) {

            Guild guild = voiceProxy.jda.getGuildById(Objects.requireNonNull(voiceProxy.getConfigData().getString("GuildID")));
            Member member = guild.getMemberById("514191339416453130"); //Change to the username and move user once synced
            RestAction<Void> move = guild.moveVoiceMember(
                    member, guild.getVoiceChannelById(voiceProxy.getConfigData().get("Proxy").toString()));
            move.complete();
            return true;

        }

        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            UtilMessage.sendmessage(player, "&5=====================================================");
            UtilMessage.sendmessage(player, "&d/voiceproxy sync - &7Allows you to sync with your discord");
            UtilMessage.sendmessage(player, "&d/voiceproxy proxy - &7Allows you to choose what channel for proximity chat");
            UtilMessage.sendmessage(player, "&d/voiceproxy waiting - &7Allows you to choose what channel for waiting chat");
            UtilMessage.sendmessage(player, "&c/voiceproxy move - &4Allows testing move.. DEV COMMAND!");
            UtilMessage.sendmessage(player, "&d/voiceproxy help - &7Shows this help guide");
            UtilMessage.sendmessage(player, "&5=====================================================");
            return true;
        }

        if (args.length == 0) {
            UtilMessage.sendmessage(player, "&5=====================================================");
            UtilMessage.sendmessage(player, "&d/voiceproxy sync - &7Allows you to sync with your discord");
            UtilMessage.sendmessage(player, "&d/voiceproxy proxy - &7Allows you to choose what channel for proximity chat");
            UtilMessage.sendmessage(player, "&d/voiceproxy waiting - &7Allows you to choose what channel for waiting chat");
            UtilMessage.sendmessage(player, "&c/voiceproxy move - &4Allows testing move.. DEV COMMAND!");
            UtilMessage.sendmessage(player, "&d/voiceproxy help - &7Shows this help guide");
            UtilMessage.sendmessage(player, "&5=====================================================");
            return true;
        } else {
            UtilMessage.sendmessage(player, "&cInvalid command!");
            UtilMessage.sendmessage(player, "&cUse /voiceproxy help for the list of commands");
        }
        return false;
    }
}