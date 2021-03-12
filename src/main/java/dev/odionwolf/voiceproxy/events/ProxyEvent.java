package dev.odionwolf.voiceproxy.events;

import dev.odionwolf.voiceproxy.VoiceProxy;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

import static org.bukkit.Bukkit.getScheduler;

public class ProxyEvent implements Listener {

    public VoiceProxy voiceProxy;
    public HashSet<Player> playerArrayList = new HashSet<>();
    public HashMap<UUID, Location> playerLocationHashMap = new HashMap<>();

    public ProxyEvent(VoiceProxy voiceProxy) {
        Bukkit.getPluginManager().registerEvents(this, voiceProxy);
        this.voiceProxy = voiceProxy;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerArrayList.add(player);
        playerLocationHashMap.put(player.getUniqueId(), player.getLocation());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerArrayList.remove(player);
        playerLocationHashMap.remove(player.getUniqueId(), player.getLocation());
    }



    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Player players : Bukkit.getOnlinePlayers()) {
            getScheduler().scheduleSyncRepeatingTask(voiceProxy, () -> {
                if (player.getNearbyEntities(3, 3, 3).contains(players)) {
                    player.sendMessage(String.valueOf(playerArrayList));
                    //for (String listOfPlayers : voiceProxy.getConfigPlayer().getConfigurationSection("Players").getKeys(false)) {
                    //if (listOfPlayers.equalsIgnoreCase(String.valueOf(player.getUniqueId()))) {    // ADD a check to make sure they have their account synced
                    Guild guild = voiceProxy.jda.getGuildById(Objects.requireNonNull(voiceProxy.getConfigData().getString("GuildID")));
                    Member MemberID = guild.getMemberById(Objects.requireNonNull(
                            voiceProxy.getConfigPlayer().getString("Players." + player.getUniqueId() + "." + "discord")));


                    Member MemberIDSelf = guild.getMemberById(Objects.requireNonNull(
                            voiceProxy.getConfigPlayer().getString("Players." + player.getUniqueId() + "." + "discord")));
                    RestAction<Void> move = guild.moveVoiceMember(MemberID, guild.getVoiceChannelById(voiceProxy.getConfigData().get("Proxy").toString()));
                    RestAction<Void> moveSelf = guild.moveVoiceMember(MemberIDSelf, guild.getVoiceChannelById(voiceProxy.getConfigData().get("Proxy").toString()));
                    move.complete();
                    moveSelf.complete();
                }
            }, 0L, 40L);
        }
    }
}