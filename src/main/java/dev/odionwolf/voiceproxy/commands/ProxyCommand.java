package dev.odionwolf.voiceproxy.commands;

import dev.odionwolf.voiceproxy.VoiceProxy;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ProxyCommand implements CommandExecutor {

    private VoiceProxy voiceProxy;

    public String waiting;
    public String proxy;

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
            String randomString = UUID.randomUUID().toString();
            player.sendMessage(randomString);
            String playerMC = player.getName();
            voiceProxy.getConfigPlayer().set("Players" + ".", playerMC);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("move")) {
            Guild guild = voiceProxy.jda.getGuildById(Objects.requireNonNull(voiceProxy.getConfigData().getString("GuildID")));
            Member member = guild.getMemberById("125014159275327488"); //Change to the username and move user once synced
            RestAction<Void> move = guild.moveVoiceMember(
                    member, guild.getVoiceChannelById(voiceProxy.getConfigData().get("Proxy").toString()));
            move.complete();

        }
        return false;
    }
}