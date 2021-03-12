package dev.odionwolf.voiceproxy;

import dev.odionwolf.voiceproxy.commands.ProxyCommand;
import dev.odionwolf.voiceproxy.discordbot.DiscordBotCommandSync;
import dev.odionwolf.voiceproxy.events.ProxyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class VoiceProxy extends JavaPlugin {

    public JDA jda;
    private ProxyCommand proxyCommand;
    private ProxyEvent proxyEvent;

    private File customConfigDataFile;
    private File customConfigPlayerFile;
    private FileConfiguration customConfigData;
    private FileConfiguration customConfigPlayer;


    @Override
    public void onEnable() {
        createConfigData();
        createConfigPlayer();
        proxyEvent = new ProxyEvent(this);
        proxyCommand = new ProxyCommand(this);
        List<GatewayIntent> gatewayIntents = new ArrayList<>();
        gatewayIntents.add(GatewayIntent.GUILD_VOICE_STATES);
        JDABuilder jdaBuilder = JDABuilder.createDefault(getConfigData().getString("Token"));
        jdaBuilder.addEventListeners(new DiscordBotCommandSync(this, proxyCommand));
        jdaBuilder.enableIntents(gatewayIntents);
        jdaBuilder.setActivity(Activity.listening("players"));
        jdaBuilder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        try {
            jda = jdaBuilder.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            getLogger().log(Level.SEVERE, "There is no token specified in" + customConfigDataFile, e);
        }
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Object values : proxyEvent.playerLocationHashMap.values()) {
                    if () //Loop through all players location to check if its less than a set number, if so
                        //add em
                }
            }
        }, 0L, 40L);
    }

    public FileConfiguration getConfigData() {
        return this.customConfigData;
    }


    private void createConfigData() {
        customConfigDataFile = new File(getDataFolder(), "data.yml");
        if (!customConfigDataFile.exists()) {
            customConfigDataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }

        customConfigData = new YamlConfiguration();
        try {
            customConfigData.load(customConfigDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void saveConfigData() throws IOException {
        customConfigData.save(customConfigDataFile);
    }


    public FileConfiguration getConfigPlayer() {
        return this.customConfigPlayer;
    }

    private void createConfigPlayer() {
        customConfigPlayerFile = new File(getDataFolder(), "player.yml");
        if (!customConfigPlayerFile.exists()) {
            customConfigPlayerFile.getParentFile().mkdirs();
            saveResource("player.yml", false);
        }

        customConfigPlayer = new YamlConfiguration();
        try {
            customConfigPlayer.load(customConfigPlayerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void saveConfigPlayer() throws IOException {
        customConfigPlayer.save(customConfigPlayerFile);
    }

    @Override
    public void onDisable() {
    }
}
