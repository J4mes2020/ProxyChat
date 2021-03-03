package dev.odionwolf.voiceproxy.events;

import dev.odionwolf.voiceproxy.VoiceProxy;
import dev.odionwolf.voiceproxy.commands.ProxyCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class ProxyEvent implements Listener {

    public ProxyCommand proxyCommand;
    public VoiceProxy voiceProxy;

    public ProxyEvent(VoiceProxy voiceProxy) {
        Bukkit.getPluginManager().registerEvents(this, voiceProxy);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (player.getNearbyEntities(5, 5, 5).contains(players)) {
                Guild guild = voiceProxy.jda.getGuildById(Objects.requireNonNull(voiceProxy.getConfigData().getString("GuildID")));
                VoiceChannel channels = guild.getVoiceChannelById(Objects.requireNonNull(voiceProxy.getConfigData().getString("Proxy")));
                guild.moveVoiceMember(Objects.requireNonNull(guild.getMemberById("125014159275327488")), channels.getGuild().getVoiceChannelById(proxyCommand.proxy));
            }
        }
    }
}
